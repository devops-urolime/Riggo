data "aws_route53_zone" "hosted_zone" {
  name         = "${var.hosted_zone_name}"
}

resource "aws_route53_record" "elb" {
  zone_id = "${data.aws_route53_zone.hosted_zone.zone_id}"
  name    = "elb.${data.aws_route53_zone.hosted_zone.name}"
  type    = "A"

  alias {
    name                   = "dualstack.${var.elb_dns_name}"
    zone_id                = "${var.elb_dns_zone_id}"
    evaluate_target_health = false
  }
}
