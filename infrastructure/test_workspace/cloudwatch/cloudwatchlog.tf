resource "aws_cloudwatch_log_group" "ecs_logs" {
  name = "/ecs/riggo-ecs-${terraform.workspace}"

  tags = {
    env = "${terraform.workspace}"
  }
}
