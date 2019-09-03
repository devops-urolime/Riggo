resource "aws_cloudwatch_event_rule" "codebuild-unitest-event" {
  name        = "${terraform.workspace}-codebuild-unitest-events"
  description = "This rule is to notify subscribers about the status of unit testing in ${terraform.workspace} environment for both api service and client app."

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
    "CodeBuild Build Phase Change"
  ],
  "detail": {
    "completed-phase": [
      "PRE_BUILD"
    ],
    "project-name": "${var.codebuild_projectnames}"
  }
})
  

}

resource "aws_cloudwatch_event_target" "codebuild-unitest-event-target" {
  rule      = "${aws_cloudwatch_event_rule.codebuild-unitest-event.name}"
  target_id = "CodebuildUnittest-SendToSNS"
  arn       = "${aws_cloudformation_stack.sns_event_topic.outputs["ARN"]}"
  input_transformer {
      input_paths = {"project-name":"$.detail.project-name","build-id":"$.detail.build-id","Phase-name":"$.detail.completed-phase","phase-status":"$.detail.completed-phase-status"}
      input_template = tostring("\"This message is to notify you that Testing phase in the codebuild <project-name> with id <build-id> in ${terraform.workspace} environment got <phase-status>.\"")
  }
}