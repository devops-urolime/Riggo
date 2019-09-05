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
