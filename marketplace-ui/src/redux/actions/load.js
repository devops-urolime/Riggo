export const GET_LOAD = "GET_LOAD";
export const GET_LOAD_SUCCESS = "GET_LOAD_SUCCESS";
export const GET_LOAD_PIPE_LINE_SUMMARY = "GET_LOAD_PIPE_LINE_SUMMARY";
export const GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS = "GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS";
export const GET_LOAD_PIPE_LINE_SUMMARY_FAIL = "GET_LOAD_PIPE_LINE_SUMMARY_FAIL";
export const APP_API_CALL_FAIL = "APP_API_CALL_FAIL";


export function findLoadById(id) {
  return {
    type: GET_LOAD,
    id
  };
}

export function loadPipeLineSummary() {
  return {
    type: GET_LOAD_PIPE_LINE_SUMMARY
  };
}
