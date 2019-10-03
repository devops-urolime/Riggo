# data "aws_iam_policy_document" "relationship-lambda" {

#     statement {
#     actions = [
#     "sts:AssumeRole"]

#     principals {
#       type = "Service"
#       identifiers = [
#       "apigateway.amazonaws.com",
#       "lambda.amazonaws.com"]
#     }
#   }
  
# }


# resource "aws_iam_role" "lambda-invoke-role" {
#   name               = "Riggo-Auth0-Integration-${terraform.workspace}"
#   path               = "/"
#   assume_role_policy = "${data.aws_iam_policy_document.relationship-lambda.json}"
# }

# resource "aws_iam_role_policy_attachment" "relationship-attachment" {
#   role       = "${aws_iam_role.lambda-invoke-role.name}"
#   policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaRole"
# }