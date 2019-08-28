data "external" "github_access_token" {
  program = [
    "aws",
    "s3",
    "cp",
    "s3://datastore-riggo/data.json",
  "-"]
}

locals {
  source_directory = "${lookup(var.artifactory_directory, "source_output_dir")}"
  build_image_directory = "${lookup(var.artifactory_directory, "build_output_image_dir")}"

  build_deploy_directory = "${lookup(var.artifactory_directory, "build_output_deploy_dir")}"

  placeholder_text = "${lookup(var.artifactory_directory, "placeholder_text")}"
}

# vars {
#   source_output_dir = "${var.source_output_artifact_dir}"
#   build_output_image_dir = "${var.build_output_image_artifact_dir}"
#   build_output_deploy_dir = "${var.build_output_deploy_artifact_dir}"
#   placeholder_text = "${var.image_placeholder_text}"
# }

resource "aws_codepipeline" "codepipeline_apiservice" {
  name     = "${terraform.workspace}-codepipeline-apiservice"
  role_arn = "${aws_iam_role.codepipeline_role_apiservice.arn}"
  lifecycle {
    ignore_changes = ["stage[0].action[0].configuration"]
  }

  artifact_store {
    location = "${aws_s3_bucket.codepipeline_artifactory_bucket.bucket}"
    type     = "S3"


  }

  stage {
    name = "Source"

    action {
      name             = "Source"
      category         = "Source"
      owner            = "ThirdParty"
      provider         = "GitHub"
      version          = "1"
      output_artifacts = ["${local.source_directory}"]

      configuration = {
        Owner  = "rig-go"
        Repo   = "${var.github_repository_name}"
        Branch = "${terraform.workspace}-${lookup(var.github_branch_name, "apiservices")}"
        PollForSourceChanges = "false"
        OAuthToken = "${data.external.github_access_token.result.github_access_token}"
        
      }
    }
  }

  stage {
    name = "Build"

    action {
      name             = "Build"
      category         = "Build"
      owner            = "AWS"
      provider         = "CodeBuild"
      input_artifacts  = ["${local.source_directory}"]
      output_artifacts = ["${local.build_image_directory}","${local.build_deploy_directory}"]
      version          = "1"

      configuration = {
        ProjectName = "${lookup(var.codebuild_project_name, "apiservice")}"
      }
    }
  }

  stage {
    name = "Deploy"

    action {
      name            = "Deploy"
      category        = "Deploy"
      owner           = "AWS"
      provider        = "CodeDeployToECS"
      input_artifacts = ["${local.build_image_directory}","${local.build_deploy_directory}"]
      version         = "1"

      configuration = {
        ApplicationName = "${var.codedeploy_application_name}"
        DeploymentGroupName = "${var.codedeploy_application_deploymentgroupname}"
        Image1ArtifactName = "${local.build_image_directory}"
        TaskDefinitionTemplateArtifact = "${local.build_deploy_directory}"
        Image1ContainerName = "${substr(local.placeholder_text, 1, 11)}"
        TaskDefinitionTemplatePath = "taskdef.json"
        AppSpecTemplateArtifact = "${local.build_deploy_directory}"
        AppSpecTemplatePath = "appspec.yaml"
        
      }
    }
  }
}