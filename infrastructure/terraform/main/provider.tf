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

provider "github" {
  token        = "${data.external.github_access_token.result.github_access_token}"
  organization = "rig-go"
}

data "external" "github_access_token" {
  program = [
    "aws",
    "s3",
    "cp",
    "s3://datastore-riggo/data.json",
  "-"]
}