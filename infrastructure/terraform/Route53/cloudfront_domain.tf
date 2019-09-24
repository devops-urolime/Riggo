data "aws_route53_zone" "hosted_zone" {
  name         = "${var.hosted_zone_name}"
}

resource "aws_route53_record" "cloudfront" {
  zone_id = "${data.aws_route53_zone.hosted_zone.zone_id}"
  name    = "${var.cloudfront_alias_name}"
  type    = "A"

  alias {
    name                   = "${var.cloudfront_domain_name}"
    zone_id                = "${var.cloudfront_zone_id}"
    evaluate_target_health = false
  }
}
