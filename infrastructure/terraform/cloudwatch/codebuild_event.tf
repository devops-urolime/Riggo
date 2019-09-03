resource "aws_cloudwatch_event_rule" "codebuild-event" {
  name        = "${terraform.workspace}-codebuild-events"
  description = "This is the rule to push the CodeBuild events of projects api service & client-app in ${terraform.workspace} environment and send notify users through SNS."

#   event_pattern = <<PATTERN
# {
#     "source": [
#     "aws.codepipeline"
#   ],
#   "detail-type": [
#     "CodePipeline Stage Execution State Change"
#   ],
#   "detail": {
#     "pipeline": [
#       "${jsonencode(var.pipeline_jobs)}"

#     ],
#     "state": [
#       "FAILED",
#       "SUCCEEDED"
#     ]
#   }
  
# }
# PATTERN
event_pattern = jsonencode({
  "source": [
    "aws.codebuild"
  ],
  "detail-type": [
    "CodeBuild Build State Change"
  ],
  "detail": {
    "build-status": [
      "FAILED",
      "SUCCEEDED"
    ],
    "project-name": "${var.codebuild_projectnames}"
  }
})
  

}

resource "aws_cloudwatch_event_target" "codebuild-event-target" {
  rule      = "${aws_cloudwatch_event_rule.codebuild-event.name}"
  target_id = "Codebuild-SendToSNS"
  arn       = "${aws_cloudformation_stack.sns_event_topic.outputs["ARN"]}"
  input_transformer {
      input_paths = {"current-context":"$.detail.current-phase-context","Build-id":"$.detail.build-id","Build-project":"$.detail.project-name","Build-status":"$.detail.build-status"}
      input_template = tostring("\"This message is to notify you that codeBuild project <Build-project> in ${terraform.workspace} environment with id <Build-id> is <Build-status>.\"")
  }
}