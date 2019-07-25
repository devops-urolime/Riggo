output "service_discovery_arn" {
  value = "${aws_service_discovery_service.ecs_svc_discover.arn}"
}
