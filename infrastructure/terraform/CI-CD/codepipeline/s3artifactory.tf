resource "aws_s3_bucket" "codepipeline_artifactory_bucket" {
  bucket = "${terraform.workspace}-codepipeline-artifact-bucket"
  force_destroy = true
  acl    = "private"
  tags = {
      env = "${terraform.workspace}"
  }
   versioning {
    enabled = true
  }
}



resource "aws_s3_bucket_public_access_block" "codepipeline_artifactory_bucket-denied" {

  bucket = "${aws_s3_bucket.codepipeline_artifactory_bucket.id}"

  block_public_acls = true
  block_public_policy = true
  ignore_public_acls      = true
  restrict_public_buckets = true
  depends_on = [
  "aws_s3_bucket.codepipeline_artifactory_bucket"]
}