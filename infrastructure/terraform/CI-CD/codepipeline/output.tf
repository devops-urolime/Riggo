output "codepipeline_s3_bucket" {
  value = "${aws_s3_bucket.codepipeline_artifactory_bucket.arn}"
}

