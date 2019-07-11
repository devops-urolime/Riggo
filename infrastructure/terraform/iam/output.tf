output "cloudwatch_APIGateway_Global_logs" {
  value = "${aws_iam_role.cloudwatch-APIGateway.arn}"
}

output "lambda_invoke" {
  value = "${aws_iam_role.lambda-invoke-role.arn}"
}
