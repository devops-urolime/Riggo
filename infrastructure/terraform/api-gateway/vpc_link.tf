resource "aws_api_gateway_vpc_link" "api-gateway-ELB" {
  name        = "${terraform.workspace}-linkto-ELB"
  description = "Link from API Gateway in ${terraform.workspace} environment to ELB running internally in VPC"
  target_arns = ["${var.elb_arn}"]
}