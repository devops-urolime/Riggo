output "codedeploy_application_name" {
  value = "${aws_codedeploy_app.deployment_application.name}"
}

output "codedeploy_application_deploymentgroupname" {
  value = "${aws_codedeploy_deployment_group.deployment_group.deployment_group_name}"
}
