



resource "github_repository_webhook" "github_webhook" {
  count = "${length(local.application_names)}"
  repository = "${var.github_repository_name}"
#   name = "${terraform.workspace}-github-webhook"
  configuration {
    url          = "${aws_codepipeline_webhook.codepipeline_Webhook.*.url[count.index]}"
    content_type = "json"
    insecure_ssl = false
    secret       = "${local.webhook_secret}"
  }


  events = ["push"]
}

resource "aws_codepipeline_webhook" "codepipeline_Webhook" {
  count = "${length(local.application_names)}"
  name            = "${terraform.workspace}-codepipeline-webhook-${count.index}"
  authentication  = "GITHUB_HMAC"
  target_action   = "Source"
  target_pipeline = "${element(local.pipeline_names, count.index)}"

  authentication_configuration {
    secret_token = "${local.webhook_secret}"
  }

  filter {
    json_path    = "$.ref"
    match_equals = "refs/heads/{Branch}"
  }
}

locals {
  webhook_secret = "${data.external.github_access_token.result.github_access_token}"
  application_names = ["apiserver","clientapp"]
  pipeline_names = ["${aws_codepipeline.codepipeline_apiservice.name}","${aws_codepipeline.codepipeline_clientapp.name}"]
}

