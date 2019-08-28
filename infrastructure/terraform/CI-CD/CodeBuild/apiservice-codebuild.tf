# data "aws_region" "current" {}

# data "aws_subnet_ids" "private" {
#   vpc_id = "${var.vpc_id}"

#   filter {
#     name   = "tag:Name"
#     values = ["${terraform.workspace} Private*"]       # insert values here
#   }

# }
# data "aws_security_groups" "security_groups" {
#   filter {
#     name   = "group-name"
#     values = ["terraform*"]
#   }

#   filter {
#     name   = "vpc-id"
#     values = ["${var.vpc_id}"]
#   }
# }

locals  {
  
  apiservice_codebuildname = "${terraform.workspace}-api-service-codebuild"
  apiservice_description = "Build project for apiservice"

}



resource "aws_codebuild_project" "apiservice-codebuild" {
  # name          = "${count.index == "0" ? "${terraform.workspace}-client-app-codebuild" : "${terraform.workspace}-api-service-codebuild"}"
  depends_on = ["aws_s3_bucket.codebuild_caching_s3_bucket"]
  name          = "${local.apiservice_codebuildname}"
  description   = "${local.apiservice_description}"
  build_timeout = "5"
  service_role  = "${aws_iam_role.api-service-codebuild-service-role.arn}"

  artifacts {
    type = "CODEPIPELINE"
  }
  cache {
    type     = "S3"
    location = "${aws_s3_bucket.codebuild_caching_s3_bucket.bucket}"
  }
  environment {
    compute_type                = "${var.compute_type}"
    image                       = "${var.codebuild_image}"
    type                        = "LINUX_CONTAINER"
    image_pull_credentials_type = "CODEBUILD"
     

    environment_variable {
      name  = "AWS_REGION"
      value = "${data.aws_region.current.name}"
    }
  
    dynamic "environment_variable" {
      for_each = var.environment_variables["apiservices"]
      content {
        name  = environment_variable.value.name
        value = environment_variable.value.value
      }
    }
    
  }
  
  


  logs_config {
    cloudwatch_logs {
      group_name = "/ecs/codebuild/${terraform.workspace}-api-service-codebuild"
    }
   }

  source {
    type = "CODEPIPELINE"
    buildspec  = "${lookup(var.buildspec_path, "apiservice")}"
  }

  vpc_config {
    vpc_id = "${var.vpc_id}"

    subnets = "${data.aws_subnet_ids.private.ids}"
    #   "${var.private_subnet1_id}",
    #   "${var.private_subnet2_id}"
         
    

    security_group_ids = "${data.aws_security_groups.security_groups.ids}"
    #   "${var.privatesecurity_group_id}"
         
    
  }

  tags = {
    env = "${terraform.workspace}"
  }
}