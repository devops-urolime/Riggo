# resource "aws_route53_record" "apigateway_custom_aliasrecord" {
#   name    = "${var.api_custom_domain_name}"
#   type    = "A"
#   zone_id = "${data.aws_route53_zone.hosted_zone.zone_id}"

#   alias {
#     evaluate_target_health = false
#     name                   = "${var.api_regional_domain_name}"
#     zone_id                = "${var.api_regional_zone_id}"
#   }
# }