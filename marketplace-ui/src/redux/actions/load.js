export const GET_LOAD = "GET_LOAD";
export const GET_LOAD_SUCCESS = "GET_LOAD_SUCCESS";

export function findLoadById(id) {
  return {
    type: GET_LOAD,
    id
  };
}
