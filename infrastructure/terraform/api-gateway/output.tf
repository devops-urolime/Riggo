output "invoke_url" {
  value = "${aws_api_gateway_stage.stage.invoke_url}"
}

# output "regional_domain_name" {
#   value = "${aws_api_gateway_domain_name.custom.regional_domain_name}"
# }

# output "regional_zone_id" {
#   value = "${aws_api_gateway_domain_name.custom.regional_zone_id}"
# }

# output "custom_domain_name" {
#   value = "${aws_api_gateway_domain_name.custom.domain_name}"
# }
