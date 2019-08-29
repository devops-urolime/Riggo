resource "github_repository_webhook" "github_webhook" {
  repository = "${var.github_repository_name}"
#   name = "${terraform.workspace}-github-webhook"
  configuration {
    url          = "${aws_codepipeline_webhook.codepipeline_Webhook.url}"
    content_type = "json"
    insecure_ssl = false
    secret       = "${local.webhook_secret}"
  }


  events = ["push"]
}

resource "aws_codepipeline_webhook" "codepipeline_Webhook" {
  name            = "${terraform.workspace}-codepipeline-webhook"
  authentication  = "GITHUB_HMAC"
  target_action   = "Source"
  target_pipeline = "${aws_codepipeline.codepipeline_apiservice.name}"

  authentication_configuration {
    secret_token = "${local.webhook_secret}"
  }

  filter {
    json_path    = "$.ref"
    match_equals = "refs/heads/{Branch}"
  }
}

locals {
  webhook_secret = "super-secret"
}