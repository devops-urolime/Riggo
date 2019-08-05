locals {
  #s3_origin_id = "s3-${var.client_app_name}"
  s3_origin_id = "${aws_s3_bucket.s3-cloudfront.bucket}"

}


resource "aws_cloudfront_origin_access_identity" "origin_access_identity" {
  #comment = "${var.client_app_name}.${terraform.workspace}.s3.us-west-2.amazonaws.com"
  comment = "${aws_s3_bucket.s3-cloudfront.bucket}.s3.us-west-2.amazonaws.com"
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
    #bucket_name = "${var.client_app_name}-${terraform.workspace}"
    bucket_name = "${aws_s3_bucket.s3-cloudfront.bucket}"
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

  aliases = ["${var.cname_alias}"]

  enabled         = "true"
  is_ipv6_enabled = "true"
  default_root_object = "${var.cloudfront_root_object}"
  price_class     = "${var.price_class}"
  #retain_on_delete    = "true"
  depends_on = [
    "aws_s3_bucket.s3-cloudfront",
    "aws_s3_bucket_public_access_block.s3-cloudfront-denied",
  "aws_s3_bucket_policy.s3-bucket"]

  viewer_certificate {
    #cloudfront_default_certificate = "true"
    acm_certificate_arn = "${var.cloudfront_acm_arn}"
    minimum_protocol_version = "${var.cloudfront_ssl_protocol_ver}"
    ssl_support_method = "sni-only"
  }





dynamic "custom_error_response" {
  # iterator = custom_error_response
  for_each = var.error_code
  content {
    #  type      =  custom_error_response.value.type
    # error_caching_min_ttl = custom_error_response.error_caching_min_ttl
    # error_code = custom_error_response.error_code
    # response_code = custom_error_response.response_code
    # response_page_path = custom_error_response.response_page_path
    error_caching_min_ttl = "${var.error_caching_min_ttl["${custom_error_response.value}"]}"
    error_code            = custom_error_response.value
    response_code         = "${var.response_code["${custom_error_response.value}"]}"
    response_page_path    = "${var.response_page_path["${custom_error_response.value}"]}"
  }
}



# custom_error_response = [

# {
#   error_caching_min_ttl = "${var.error_caching_min_ttl["${var.ERROR_CODE}"]}"
#   error_code = "${var.error_code["${var.ERROR_CODE}"]}"
#   response_code = "${var.response_code["${var.ERROR_CODE}"]}"
#   response_page_path = "${var.response_page_path["${var.ERROR_CODE}"]}"


# }

# {
#   error_caching_min_ttl = "${var.error_caching_min_ttl["${var.ERROR_CODE}"]}"
#   error_code = "${var.error_code["${var.ERROR_CODE}"]}"
#   response_code = "${var.response_code["${var.ERROR_CODE}"]}"
#   response_page_path = "${var.response_page_path["${var.ERROR_CODE}"]}"


# }


#]

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

#  lifecycle {
#    ignore_changes = [aliases,custom_error_response,custom_error_response,viewer_certificate]
#   }

}

