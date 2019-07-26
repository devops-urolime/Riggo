# resource "aws_api_gateway_authorizer" "authorizer" {
#   name                   = "riggo-authorizer-Auth0"
#   rest_api_id            = "${aws_api_gateway_rest_api.riggo-api-gateway.id}"
#   authorizer_uri         = "${aws_lambda_function.authorizer.invoke_arn}"
#   authorizer_credentials = "${aws_iam_role.invocation_role.arn}"
# }