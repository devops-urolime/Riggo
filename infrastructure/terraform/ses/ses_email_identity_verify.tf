resource "aws_ses_email_identity" "email_verify" {
  email = "${var.ses_email_address}"
}