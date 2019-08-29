output "codebuild_project_name_apiservice" {
  value = "${aws_codebuild_project.apiservice-codebuild.name}"
}

output "codebuild_project_name_clientapp" {
  value = "${aws_codebuild_project.clientapp-codebuild.name}"
}