# provider "aws" {
#   alias  = "local"
#   region = "${var.s3_region}"
#   shared_credentials_file = "/home/muhasin/.aws/credentials"
#   profile = "${var.aws_profile}"
# }
resource "aws_s3_bucket" "s3-cloudfront" {
  # provider = "aws.local"
  bucket = "${var.client_app_name}-${terraform.workspace}"
  acl    = "private"
  #region = "${var.s3_region}"
  # region = "aws.local"

  versioning {
    enabled = true
  }

  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "${var.sse_algorithm}"
      }
    }
  }

  tags = {
    Name = "${var.client_app_name}-${terraform.workspace}"
    env  = "${terraform.workspace}"
  }

}

resource "aws_s3_bucket_public_access_block" "s3-cloudfront-denied" {

  bucket = "${aws_s3_bucket.s3-cloudfront.id}"

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true

}

