import { BASE_END_POINT, LOAD_END_POINT } from "./config";

export const findLoadByIdApi = async (idLoad) => {
    const END_POINT = BASE_END_POINT + LOAD_END_POINT + '/' + idLoad;
    const token = localStorage.getItem('accessToken');
    const ACCESS_TOKEN_JWT = token || '';
    const requestData = {
        method: 'get',
        headers: new Headers({
            'Authorization': 'Bearer ' + ACCESS_TOKEN_JWT,
            'Accept': 'application/json'
        })
    };
    try {
        const response = await fetch(
            END_POINT,
            requestData
        );
        const json = await response.json();
        return (json) ? json : {};
    } catch (e) {
        return e;
    }
};
