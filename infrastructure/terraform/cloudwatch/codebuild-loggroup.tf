resource "aws_cloudwatch_log_group" "apiservice-codebuild" {
  name = "/ecs/codebuild/${terraform.workspace}-api-service-codebuild"

  tags = {
    env = "${terraform.workspace}"
  }
}


resource "aws_cloudwatch_log_group" "clientapp-codebuild" {
  name = "/ecs/codebuild/${terraform.workspace}-client-app-codebuild"

  tags = {
    env = "${terraform.workspace}"
  }
}




