

# vars {
#   source_output_dir = "${var.source_output_artifact_dir}"
#   build_output_image_dir = "${var.build_output_image_artifact_dir}"
#   build_output_deploy_dir = "${var.build_output_deploy_artifact_dir}"
#   placeholder_text = "${var.image_placeholder_text}"
# }

resource "aws_codepipeline" "codepipeline_clientapp" {
  name     = "${terraform.workspace}-codepipeline-clientapp"
  role_arn = "${aws_iam_role.codepipeline_role_clientapp.arn}"
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
        Owner  = "${var.github_organization_name}"
        Repo   = "${var.github_repository_name}"
        Branch = "${lookup(var.github_branch_name, "clientapp")}"
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
      output_artifacts = ["${local.build_deploy_directory}"]
      version          = "1"

      configuration = {
        ProjectName = "${lookup(var.codebuild_project_name, "clientapp")}"
      }
    }
  }

}