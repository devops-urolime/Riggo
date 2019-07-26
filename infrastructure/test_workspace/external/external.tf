data "aws_s3_bucket_object" "credentials" {
  bucket = "data-riggo"
  key    = "data.json"
}


# variable "credentials" {
#   value = "${data.aws_s3_bucket_object.credentials.body}"
# }


data "external" "get-data" {
  program = [
    "echo",
  "${data.aws_s3_bucket_object.credentials.body}"]
}

output "username" {
  value = [
  "${values(data.external.get-data.result.username)}"]
}

output "password" {
  value = [
  "${values(data.external.get-data.result.password)}"]
}