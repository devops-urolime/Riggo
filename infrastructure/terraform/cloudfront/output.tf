output "s3-bucket" {
  value = "${aws_s3_bucket.s3-cloudfront.bucket_domain_name}"
}

output "cloudfront_domain_name" {
  value = "${aws_cloudfront_distribution.s3_distribution.domain_name}"
}



