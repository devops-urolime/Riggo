provider "aws" {
  # alias   = "local"
  region                  = "us-west-2"
  shared_credentials_file = "~/.aws/credentials"
  # profile  = "${var.aws_profile}"
}


# provider "aws" {
#   alias  = "local"
#   region = "${var.s3_region}"
#   shared_credentials_file = "/home/muhasin/.aws/credentials"
#   profile                 = "${var.aws_profile}"
# }
