resource "aws_s3_bucket" "codepipeline_artifactory_bucket" {
  bucket = "${terraform.workspace}-codepipeline-artifact-bucket"
  acl    = "private"
  tags = {
      env = "${terraform.workspace}"
  }
   versioning {
    enabled = true
  }
}
