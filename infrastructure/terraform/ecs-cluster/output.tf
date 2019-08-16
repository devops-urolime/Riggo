output "ecs_cluster_name" {
  value = "${aws_ecs_cluster.ecs-cluster.name}"
}
output "ecs_service_name" {
  value = "${aws_ecs_service.riggo-ecs-service.name}"
}

output "task_definition" {
  value = "${aws_ecs_task_definition.riggo-ecs-TD.revision}"
}

output "ApplicationELB_Id" {
  value = "${aws_lb.ecs-lb.arn_suffix}"
}


output "production_targetgroup_suffix" {
  value = "${aws_lb_target_group.ecs-lb-targetgroup.arn_suffix}"
}

output "testing_targetgroup_suffix" {
  value = "${aws_lb_target_group.ecs-lb-targetgroup-testlistner.arn_suffix}"
}
