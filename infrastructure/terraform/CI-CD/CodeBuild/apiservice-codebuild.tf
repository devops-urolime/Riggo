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
  apiservice_description = "Build project for apiservice in ${terraform.workspace} environment"
  build_image_directory = "${lookup(var.artifactory_directory, "build_output_image_dir")}"
  build_deploy_directory = "${lookup(var.artifactory_directory, "build_output_deploy_dir")}"
  placeholder_text = "${lookup(var.artifactory_directory, "placeholder_text")}"

}



resource "aws_codebuild_project" "apiservice-codebuild" {
  # name          = "${count.index == "0" ? "${terraform.workspace}-client-app-codebuild" : "${terraform.workspace}-api-service-codebuild"}"
  depends_on = ["aws_s3_bucket.codebuild_caching_s3_bucket"]
  name          = "${local.apiservice_codebuildname}"
  description   = "${local.apiservice_description}"
  build_timeout = "300"
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
    privileged_mode = true

    environment_variable {
      name  = "AWS_REGION"
      value = "${data.aws_region.current.name}"
    }

    environment_variable {
      name = "CONTAINER_NAME"
      value = "${var.container_name}"
    }

    environment_variable {
      name = "DEPLOY_ARTIFACT_NAME"
      value = "${local.build_deploy_directory}"
    }
    environment_variable {
      name = "TASKDEF_NAME"
      value = "${var.task_definition}"
    }
    environment_variable {
      name = "ECR_PLACEHOLDER"
      value = "${local.placeholder_text}"
    }


    environment_variable {
      name = "IMAGE_ARTIFACT_NAME"
      value = "${local.build_image_directory}"
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

    # subnets = "${data.aws_subnet_ids.private.ids}"
    #   "${var.private_subnet1_id}",
    #   "${var.private_subnet2_id}"
    subnets = "${var.private_subnet_ids}"
         
    

    security_group_ids = "${data.aws_security_groups.security_groups.ids}"
    #   "${var.privatesecurity_group_id}"
         
    
  }

  tags = {
    env = "${terraform.workspace}"
  }
}