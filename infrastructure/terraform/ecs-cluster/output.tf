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

output "elb_arn" {
  value = "${aws_lb.ecs-lb.arn}"
}


output "production_targetgroup_suffix" {
  value = "${aws_lb_target_group.ecs-lb-targetgroup.arn_suffix}"
}

output "testing_targetgroup_suffix" {
  value = "${aws_lb_target_group.ecs-lb-targetgroup-testlistner.arn_suffix}"
}

output "prod_listener" {
  value = "${aws_lb_listener.front_end.arn}"
}

output "test_listener" {
  value = "${aws_lb_listener.testing-listener.arn}"
}

output "target_group_name_blue" {
  value = "${aws_lb_target_group.ecs-lb-targetgroup.name}"
}

output "target_group_name_green" {
  value = "${aws_lb_target_group.ecs-lb-targetgroup-testlistner.name}"
}

# output "container_definition" {
#   value = "${aws_ecs_task_definition.riggo-ecs-TD.container_definitions}"
# }

output "container_definition" {
  value = "${data.template_file.container-definition.rendered}"
}

output "task_definition_family" {
  value = "${aws_ecs_task_definition.riggo-ecs-TD.family}"
}

output "container_name" {
  value = "${data.template_file.container-definition.vars["container_name"]}"
}

output "elb_endpoint" {
  value = "${aws_lb.ecs-lb.dns_name}"
}

output "ECS_instance_SG" {
  value = "${aws_security_group.ecs-instance-SG.id}"
}

output "elb_hosted_zone_id" {
  value = "${aws_lb.ecs-lb.zone_id}"
}

