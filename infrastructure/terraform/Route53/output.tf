output "service_discovery_arn" {
  value = "${aws_service_discovery_service.ecs_svc_discover.arn}"
}

# output "elb_custom_endpoint" {
#   value = "${aws_route53_record.elb.name}"
# }
