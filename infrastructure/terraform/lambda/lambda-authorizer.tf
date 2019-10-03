# resource "aws_lambda_function" "JWT-CustomAuthorizer" {
#   #filename      = "${data.template_file.authorizer.rendered}"
#   filename      = "${path.module}/custom-authorizer.zip"
#   description   = "JWT RSA Custom Authorizer"
#   function_name = "jwtRsaCustomAuthorizer-${terraform.workspace}"
#   role          = "${var.lambda_invoke_arn}"
#   handler       = "${var.handler}"
#   timeout       = "${var.timeout}"

#   # The filebase64sha256() function is available in Terraform 0.11.12 and later
#   # For Terraform 0.11.11 and earlier, use the base64sha256() function and the file() function:
#   # source_code_hash = "${base64sha256(file("lambda_function_payload.zip"))}"
#   source_code_hash = "${filebase64sha256("${path.module}/custom-authorizer.zip")}"

#   runtime = "${var.runtime_platform}"

#   environment {
#     variables = {
#         AUDIENCE = "${var.env_audience}"
#         JWKS_URI = "${var.env_auth0_JWKS_URI}"
#         TOKEN_ISSUER = "${var.env_auth0_TOKEN_ISSUER}"
#      }
#   }

#   tags = {
#       env = "${terraform.workspace}"
#   }

#   lifecycle {
#   ignore_changes = [environment,source_code_hash,last_modified]
# }
# }


# # data "template_file" "authorizer" {

# # template = "${file("${path.module}/custom-authorizer.zip")}"
  
# # }
