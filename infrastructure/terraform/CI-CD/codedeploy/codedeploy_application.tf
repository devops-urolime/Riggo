resource "aws_codedeploy_app" "deployment_application" {
  compute_platform = "ECS"
  name             = "${terraform.workspace}-apiservice-ECSdeployment"
}