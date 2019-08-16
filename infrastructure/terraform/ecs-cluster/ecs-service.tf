resource "aws_ecs_service" "riggo-ecs-service" {
  name                               = "Riggo-ECS-${terraform.workspace}-SVC"
  cluster                            = "${aws_ecs_cluster.ecs-cluster.id}"
  task_definition                    = "${aws_ecs_task_definition.riggo-ecs-TD.arn}"
  desired_count                      = "${var.desired_ecs_instance-size}"
  deployment_minimum_healthy_percent = "${var.deployment_minimum_healh_percent}"
  deployment_maximum_percent         = "${var.deployment_maximum_healh_percent}"
  health_check_grace_period_seconds  = "${var.health_check_grace_period_seconds}"

  lifecycle {
   ignore_changes = [task_definition,load_balancer]
  }

  depends_on = [
    "aws_lb.ecs-lb",
    "aws_lb_listener.front_end"
  ]
  #"aws_autoscaling_attachment.ecs-targetgroup-attachment"
  #iam_role   = "${aws_iam_role.ecs-service-role.arn}"

  ordered_placement_strategy {
    type  = "spread"
    field = "attribute:ecs.availability-zone"
  }

   deployment_controller {
   type = "CODE_DEPLOY"
 }

  ordered_placement_strategy {
    type  = "spread"
    field = "instanceId"
  }
  service_registries {

    registry_arn   = "${var.service_discovery_arn}"
    container_name = "Riggo-resource-svr-${terraform.workspace}"
    container_port = "${var.container_port}"

  }

  load_balancer {
    target_group_arn = "${aws_lb_target_group.ecs-lb-targetgroup.arn}"
    container_name   = "Riggo-resource-svr-${terraform.workspace}"
    container_port   = "${var.container_port}"
  }
}