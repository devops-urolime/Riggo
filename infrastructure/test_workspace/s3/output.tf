output "domain_name" {
  value = "${aws_s3_bucket.s3-cloudfront.bucket_regional_domain_name}"
}

output "domain_id" {
  value = "${aws_s3_bucket.s3-cloudfront.id}"
}
