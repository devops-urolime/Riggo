output "s3-bucket" {
  value = "${aws_s3_bucket.s3-cloudfront.id}"
}

output "cloudfront_domain_name" {
  value = "${aws_cloudfront_distribution.s3_distribution.domain_name}"
}

output "cloudfront_distribution_id" {
  value = "${aws_cloudfront_distribution.s3_distribution.id}"
}

output "s3_bucket_region" {
  value = "${aws_s3_bucket.s3-cloudfront.region}"
}



