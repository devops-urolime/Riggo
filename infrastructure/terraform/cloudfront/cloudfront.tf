locals {
  s3_origin_id = "s3-${var.client_app_name}"
}

resource "aws_cloudfront_origin_access_identity" "origin_access_identity" {
  comment = "${var.client_app_name}.${terraform.workspace}.s3.us-west-2.amazonaws.com"
}


data "aws_iam_policy_document" "origin" {
  statement {
    actions = [
    "s3:GetObject"]
    resources = [
    "arn:aws:s3:::$${bucket_name}$${origin_path}*"]

    principals {
      type = "AWS"
      identifiers = [
      "${aws_cloudfront_origin_access_identity.origin_access_identity.iam_arn}"]
    }
  }

  statement {
    actions = [
    "s3:ListBucket"]
    resources = [
    "arn:aws:s3:::$${bucket_name}"]

    principals {
      type = "AWS"
      identifiers = [
      "${aws_cloudfront_origin_access_identity.origin_access_identity.iam_arn}"]
    }
  }
}


data "template_file" "default" {
  template = "${data.aws_iam_policy_document.origin.json}"

  vars = {
    origin_path = "${coalesce(var.origin_path, "/")}"
    bucket_name = "${var.client_app_name}-${terraform.workspace}"
  }
}


resource "aws_s3_bucket_policy" "s3-bucket" {
  #bucket = "${var.client_app_name}-${terraform.workspace}"
  bucket = "${aws_s3_bucket.s3-cloudfront.id}"
  policy = "${data.template_file.default.rendered}"
  depends_on = [
    "aws_s3_bucket.s3-cloudfront",
  "aws_s3_bucket_public_access_block.s3-cloudfront-denied"]
}


resource "aws_cloudfront_distribution" "s3_distribution" {
  origin {
    domain_name = "${aws_s3_bucket.s3-cloudfront.bucket_regional_domain_name}"
    #domain_name = "${var.domain_name}"
    origin_id = "${local.s3_origin_id}"


    s3_origin_config {
      origin_access_identity = "${aws_cloudfront_origin_access_identity.origin_access_identity.cloudfront_access_identity_path}"
    }
  }

  enabled         = "true"
  is_ipv6_enabled = "true"
  price_class     = "${var.price_class}"
  #retain_on_delete    = "true"
  depends_on = [
    "aws_s3_bucket.s3-cloudfront",
    "aws_s3_bucket_public_access_block.s3-cloudfront-denied",
  "aws_s3_bucket_policy.s3-bucket"]

  viewer_certificate {
    cloudfront_default_certificate = "true"
  }


  tags = {
    env = "${terraform.workspace}"
  }


  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }


  default_cache_behavior {
    cached_methods         = "${var.cached_methods}"
    target_origin_id       = "${local.s3_origin_id}"
    allowed_methods        = "${var.allowed_methods}"
    viewer_protocol_policy = "redirect-to-https"

    forwarded_values {
      query_string = "false"

      cookies {
        forward = "none"
      }
    }

  }


}

