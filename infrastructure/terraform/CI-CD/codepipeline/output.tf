output "codepipeline_s3_bucket" {
  value = "${aws_s3_bucket.codepipeline_artifactory_bucket.arn}"
}

output "codepipeline_apiservice_name" {
  value = "${aws_codepipeline.codepipeline_apiservice.name}"
}

output "codepipeline_clientapp_name" {
  value = "${aws_codepipeline.codepipeline_clientapp.name}"
}
