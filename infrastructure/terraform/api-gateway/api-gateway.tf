# resource "aws_api_gateway_rest_api" "riggo-api-gateway" {
#   name = "Riggo Platform qa"
#   #description = "API to access riggo"
#   body = "${data.template_file.riggo_api_swagger.rendered}"
#   endpoint_configuration {
#     types = [
#     "REGIONAL"]
#   }
  
#   lifecycle {
#    ignore_changes = [description]
#   }
#  }

# data "template_file" "riggo_api_swagger" {
#   template = "${file("${path.module}/swagger.yaml")}"

#   vars = {

#     name = "Riggo Platform ${terraform.workspace}"
#     basePath = "${terraform.workspace}"
#     ELB_ENDPOINT = "${var.elb_endpoint}"
#     authorizerUri = "${var.authorize_uri}"
#     authorizerArn = "${var.authorizerArn}"
#     region = "${data.aws_region.current.name}"

#   }

# }

resource "aws_api_gateway_deployment" "riggo-api-gateway-deployment" {
  rest_api_id = "${data.aws_api_gateway_rest_api.rest_api.id}"
  stage_name  = "${terraform.workspace}"

  variables = {
    "elb_url" = "${var.elb_endpoint}"
  }
}

data "aws_api_gateway_rest_api" "rest_api" {
  name = "${var.rest_api_name}"
}

# data "aws_region" "current" {}


# resource "aws_api_gateway_stage" "stage" {
#   stage_name    = "${terraform.workspace}-eds-test"
#   rest_api_id   = "${aws_api_gateway_rest_api.riggo-api-gateway.id}"
#   deployment_id = "${aws_api_gateway_deployment.riggo-api-gateway-deployment.id}"
# }
# resource "aws_api_gateway_resource" "resource" {
#   path_part   = "resource"
#   parent_id   = "${aws_api_gateway_rest_api.riggo-api-gateway.root_resource_id}"
#   rest_api_id = "${aws_api_gateway_rest_api.riggo-api-gateway.id}"
# }


# resource "aws_api_gateway_integration" "mockintegration" {
#   rest_api_id          = "${aws_api_gateway_rest_api.riggo-api-gateway.id}"
#   resource_id          = "${aws_api_gateway_resource.resource.id}"
#   http_method          = "${aws_api_gateway_method..http_method}"
#   type                 = "MOCK"
#   cache_key_parameters = ["method.request.path.param"]
#   cache_namespace      = "foobar"
#   timeout_milliseconds = 29000

#   request_parameters = {
#     "integration.request.header.X-Authorization" = "'static'"
#   }

#   # Transforms the incoming XML request to JSON
#   request_templates = {
#     "application/xml" = <<EOF
# {
#    "body" : $input.json('$')
# }
# EOF
#   }
# }

# resource "aws_api_gateway_account" "Global-log" {
#   cloudwatch_role_arn = "${var.cloudwatchlogs-globalarn}"

#   lifecycle {
#   prevent_destroy = true
#   }
# }