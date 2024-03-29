# data "aws_s3_bucket" "bucket_arn" {
#   bucket = "${var.client-app-s3-bucket}"
# }


# data "aws_subnet" "subnet" {
#   count = "${length(data.aws_subnet_ids.private.ids)}"
#   id = "${tolist(data.aws_subnet_ids.private.ids)[count.index]}"
# }



resource "aws_iam_role" "api-service-codebuild-service-role" {
  name = "${terraform.workspace}-api-service-codebuild-servicerole"

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

resource "aws_iam_role_policy" "api-service-codebuild-service-role-policy" {
  role = "${aws_iam_role.api-service-codebuild-service-role.name}"
  name_prefix = "${terraform.workspace}-apiservice-codebuild-servicepolicy"
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
                "ecr:*",
                "ecs:*"
            ],
            "Resource": "*"
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
        "s3:*"
      ],
      "Resource": [
        "${aws_s3_bucket.codebuild_caching_s3_bucket.arn}",
        "${aws_s3_bucket.codebuild_caching_s3_bucket.arn}/*"
      ]
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
                "ssm:GetParameters"
            ],
            "Resource": "arn:aws:ssm:us-west-2:845657178663:parameter/CodeBuild/*"
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
    }
  ]
}
POLICY
}
