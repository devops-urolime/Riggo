resource "aws_ecs_cluster" "ecs-cluster" {
  name = "Riggo-ECS-${terraform.workspace}"
}



