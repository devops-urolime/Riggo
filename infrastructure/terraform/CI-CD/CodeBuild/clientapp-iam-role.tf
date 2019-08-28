data "aws_s3_bucket" "bucket_arn" {
  bucket = "${var.client-app-s3-bucket}"
}

# locals {
#   private_subnet_ids = "${data.aws_subnet_ids.private.ids}"
# }
data "aws_subnet" "subnet" {
  #  depends_on = ["data.aws_subnet_ids.private"]
  count = length(var.private_subnet_ids)
  id = tolist(var.private_subnet_ids)[count.index]
}



resource "aws_iam_role" "client-app-codebuild-service-role" {
  name = "${terraform.workspace}-client-app-codebuild-servicerole"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "codebuild.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "client-app-codebuild-service-role-policy" {
  role = "${aws_iam_role.client-app-codebuild-service-role.name}"
  name_prefix = "${terraform.workspace}-clientapp-codebuild-servicepolicy"
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Resource": [
        "*"
      ],
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ]
    },
    {
      "Effect": "Allow",
      "Action": [
        "ec2:CreateNetworkInterface",
        "ec2:DescribeDhcpOptions",
        "ec2:DescribeNetworkInterfaces",
        "ec2:DeleteNetworkInterface",
        "ec2:DescribeSubnets",
        "ec2:DescribeSecurityGroups",
        "ec2:DescribeVpcs"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "ec2:CreateNetworkInterfacePermission"
      ],
      "Resource": [
        "arn:aws:ec2:us-west-2:845657178663:network-interface/*"
      ],
      "Condition": {
        "StringEquals": {
          "ec2:Subnet": [
            "${data.aws_subnet.subnet.0.arn}",
            "${data.aws_subnet.subnet.1.arn}"

          ],
          "ec2:AuthorizedService": "codebuild.amazonaws.com"
        }
      }
    },
    {
      "Effect": "Allow",
      "Action": [
        "s3:*"
      ],
      "Resource": [
        "${data.aws_s3_bucket.bucket_arn.arn}",
        "${data.aws_s3_bucket.bucket_arn.arn}/*"
      ]
    },   
    {
            "Effect": "Allow",
            "Resource": [
                "${var.codepipeline_artifact_bucket}*"
            ],
            "Action": [
                "s3:PutObject",
                "s3:GetObject",
                "s3:GetObjectVersion",
                "s3:GetBucketAcl",
                "s3:GetBucketLocation"
            ]
    },
    {
      "Effect": "Allow",
      "Resource": [
        "*"
      ],
      "Action": [
        "cloudfront:CreateInvalidation"
      ]
    }
  ]
}
POLICY
}
