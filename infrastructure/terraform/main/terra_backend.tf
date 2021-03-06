terraform {
  backend "s3" {
    encrypt                 = true
    bucket                  = "stagingterraform-remotestate-storage-s3"
    region                  = "us-west-2"
    key                     = "terraform.tfstate"
    dynamodb_table          = "Terraform-State-Lock-DynamoDB"
    shared_credentials_file = "~/.aws/credentials"
    #profile = "${var.aws_profile}"
  }
}
