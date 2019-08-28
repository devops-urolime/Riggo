resource "aws_s3_bucket" "codebuild_caching_s3_bucket" {
  bucket = "${terraform.workspace}-codebuild-caching-bucket"
  acl    = "private"
  tags = {
      env = "${terraform.workspace}"
  }
   versioning {
    enabled = true
  }
}
