output "ses_smtp_user" {
  value = "${aws_iam_access_key.smtp_user.id}"
}


output "ses_smtp_user_password" {
  value = "${aws_iam_access_key.smtp_user.ses_smtp_password}"
}
