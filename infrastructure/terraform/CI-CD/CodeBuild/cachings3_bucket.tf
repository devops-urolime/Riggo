resource "aws_s3_bucket" "codebuild_caching_s3_bucket" {
  bucket = "${terraform.workspace}-codebuild-caching-bucket"
  force_destroy = true
  acl    = "private"
  tags = {
      env = "${terraform.workspace}"
  }
   versioning {
    enabled = true
  }
}

resource "aws_s3_bucket_public_access_block" "codebuild_caching_s3_bucket" {

  bucket = "${aws_s3_bucket_public_access_block.codebuild_caching_s3_bucket.id}"

  block_public_acls = true
  block_public_policy = true
  ignore_public_acls      = true
  restrict_public_buckets = true
  depends_on = [
  "aws_s3_bucket_public_access_block.codebuild_caching_s3_bucket"]
}
