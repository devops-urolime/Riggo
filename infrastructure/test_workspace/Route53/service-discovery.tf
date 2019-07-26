resource "aws_service_discovery_private_dns_namespace" "ecs_namespace" {
  name        = "${terraform.workspace}"
  description = "Riggo namespace"
  vpc         = "${var.vpc_id}"


}

resource "aws_service_discovery_service" "ecs_svc_discover" {
  name = "${var.cluster_name}-service"

  dns_config {
    namespace_id = "${aws_service_discovery_private_dns_namespace.ecs_namespace.id}"

    dns_records {
      ttl  = "${var.service_discovery_ttl}"
      type = "SRV"
    }

  }
  health_check_custom_config {
    failure_threshold = 1
  }

}


