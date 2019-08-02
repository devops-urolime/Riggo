output "ecs_cluster_name" {
  value = "${aws_ecs_cluster.ecs-cluster.name}"
}
output "ecs_service_name" {
  value = "${aws_ecs_service.riggo-ecs-service.name}"
}

output "task_definition" {
  value = "${aws_ecs_task_definition.riggo-ecs-TD.revision}"
}


