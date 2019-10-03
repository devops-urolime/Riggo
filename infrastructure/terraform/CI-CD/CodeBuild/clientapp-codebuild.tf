data "aws_region" "current" {}

# data "aws_subnet_ids" "private" {
#   vpc_id = "${var.vpc_id}"

#   filter {
#     name   = "tag:Name"
#     values = ["${terraform.workspace} Private*"]       # insert values here
#   }

# }
# data "aws_security_groups" "security_groups" {
#     filter {
#     name   = "tag:Name"
#     values = ["*-${terraform.workspace}-ECSinstance"]
#   }

#   filter {
#     name   = "vpc-id"
#     values = ["${var.vpc_id}"]
#   }
# }

locals  {
  
  clientapp_codebuildname = "${terraform.workspace}-client-app-codebuild"
  clientapp_description = "Build project for client-app in ${terraform.workspace} environment"
}




resource "aws_codebuild_project" "clientapp-codebuild" {
  # name          = "${count.index == "0" ? "${terraform.workspace}-client-app-codebuild" : "${terraform.workspace}-api-service-codebuild"}"
  name          = "${local.clientapp_codebuildname}"
  description   = "${local.clientapp_description}"
  build_timeout = "300"
  service_role  = "${aws_iam_role.client-app-codebuild-service-role.arn}"

  artifacts {
    type = "CODEPIPELINE"
  }


  environment {
    compute_type                = "${var.compute_type}"
    image                       = "${var.codebuild_image}"
    type                        = "LINUX_CONTAINER"
    image_pull_credentials_type = "CODEBUILD"
    privileged_mode = true
     
    environment_variable {
      name  = "AWS_S3_BUCKET_DEPLOY"
      value = "${var.client-app-s3-bucket}"
    }

    environment_variable {
      name  = "AWS_S3_REGION_DEPLOY"
      value = "${data.aws_region.current.name}"
    }
  
    environment_variable {
      name = "AWS_CLOUDFRONT_DISTRIBUTION_ID"
      value = "${var.cloudfront_distribution_id}"
    }
    
    environment_variable {
      name = "REACT_APP_BASE_END_POINT"
      value = "${var.apigateway_invoke_url}"
    }
    #  dynamic "environment_variable" {
    #   for_each = var.environment_variables["clientapp"]
    #   content {
    #     name  = environment_variable.value.name
    #     value = environment_variable.value.value
    #   }
    # }
    
  }

  logs_config {
    cloudwatch_logs {
      group_name = "${var.clientapp-loggroup}"
    }
   }

  source {
    type = "CODEPIPELINE"
    buildspec = "${lookup(var.buildspec_path, "clientapp")}"
  }

  vpc_config {
    vpc_id = "${var.vpc_id}"

    # subnets = "${data.aws_subnet_ids.private.ids}"
    #   "${var.private_subnet1_id}",
    #   "${var.private_subnet2_id}"
      subnets = "${var.private_subnet_ids}"
         
    

    #security_group_ids = "${data.aws_security_groups.security_groups.ids}"
    security_group_ids = "${var.ECS_securitygroup_ids}"
    #   "${var.privatesecurity_group_id}"
         
    
  }

  tags = {
    env = "${terraform.workspace}"
  }
}