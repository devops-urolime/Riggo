resource "aws_iam_role" "ecs-instance-role" {
  name               = "${terraform.workspace}-ECS-Instance-role"
  path               = "/"
  assume_role_policy = "${data.aws_iam_policy_document.ecs-instance-policy.json}"
}

data "aws_iam_policy_document" "ecs-instance-policy" {
  statement {
    actions = [
    "sts:AssumeRole"]

    principals {
      type = "Service"
      identifiers = [
      "ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role_policy_attachment" "ecs-instance-role-attachment" {
  role       = "${aws_iam_role.ecs-instance-role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

resource "aws_iam_role_policy_attachment" "ecs-instance-role-cloudwatch-attachment" {
  role       = "${aws_iam_role.ecs-instance-role.name}"
  policy_arn = "${aws_iam_policy.ECS-policy.arn}"
}

resource "aws_iam_policy" "ECS-policy" {
  name        = "${terraform.workspace}-ECS-CloudwatchLog-Full"
  description = "policy to create, update ,write and read logs"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogGroup",
                "logs:CreateLogStream",
                "logs:PutLogEvents",
                "logs:DescribeLogStreams"
            ],
            "Resource": [
                "arn:aws:logs:*:*:*"
            ]
        }
    ]
}
EOF
}


resource "aws_iam_instance_profile" "ecs-instance-profile" {
  name = "${terraform.workspace}-ECS-Instance-profile"
  path = "/"
  role = "${aws_iam_role.ecs-instance-role.id}"
  # provisioner "local-exec" {
  # command = "sleep 60"
  # }
}

resource "aws_iam_role" "ecs-service-role" {
  name = "${terraform.workspace}-ECS-Service-role"
  path = "/"
  assume_role_policy = "${data.aws_iam_policy_document.ecs-service-policy.json}"
}

resource "aws_iam_role_policy_attachment" "ecs-service-role-attachment" {
  role = "${aws_iam_role.ecs-service-role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceRole"
}

data "aws_iam_policy_document" "ecs-service-policy" {
  statement {
    actions = [
    "sts:AssumeRole"]

    principals {
      type = "Service"
      identifiers = [
      "ecs.amazonaws.com"]
    }
  }
}


