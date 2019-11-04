resource "aws_api_gateway_domain_name" "custom" {
  regional_certificate_arn = "${var.custom_name_cert}"
  domain_name     = "api.${var.hosted_zone_name}"
  security_policy = "TLS_1_2"

  endpoint_configuration {
    types = ["REGIONAL"]
  }
}

resource "aws_api_gateway_base_path_mapping" "base_path" {
  api_id      = "${data.aws_api_gateway_rest_api.rest_api.id}"
  stage_name  = "${aws_api_gateway_stage.stage.stage_name}"
  domain_name = "${aws_api_gateway_domain_name.custom.domain_name}"
  base_path = "${var.basepath_apigateway}"
}