resource "aws_api_gateway_rest_api" "riggo-api-gateway" {
  name = "Riggo Platform ${terraform.workspace}"
  #description = "API to access riggo"
  body = "${data.template_file.riggo_api_swagger.rendered}"
  endpoint_configuration {
    types = [
    "REGIONAL"]
  }
  
  lifecycle {
   ignore_changes = [ body,description ]
  }
 }

data "template_file" "riggo_api_swagger" {
  template = "${file("${path.module}/swagger-saas.yaml")}"

  vars = {

    name = "Riggo Platform ${terraform.workspace}"
    authorizerUri = "${var.authorize_uri}"
    authorizerArn = "${var.authorizerArn}"

  }

}

resource "aws_api_gateway_deployment" "riggo-api-gateway-deployment" {
  rest_api_id = "${aws_api_gateway_rest_api.riggo-api-gateway.id}"
  stage_name  = "${terraform.workspace}"
}


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

resource "aws_api_gateway_account" "Global-log" {
  cloudwatch_role_arn = "${var.cloudwatchlogs-globalarn}"

  lifecycle {
  prevent_destroy = true
  }
}