resource "aws_iam_access_key" "smtp_user" {
  user    = "${aws_iam_user.smtp_user.name}"
}

resource "aws_iam_user" "smtp_user" {
  name = "ses-stmp-user-${var.ses_smtp_user_domain}"
  path = "/"
}

resource "aws_iam_user_policy" "ses_access" {
  name = "${terraform.workspace}.ses_access_smtp_user_role"
  user = "${aws_iam_user.smtp_user.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
      {  "Effect":"Allow",  
         "Action":"ses:SendRawEmail",  
         "Resource":"*"
      }
  ]

}
EOF
}
