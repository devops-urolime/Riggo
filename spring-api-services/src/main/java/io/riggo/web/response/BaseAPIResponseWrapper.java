package io.riggo.web.response;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseAPIResponseWrapper<T> implements Serializable {


    public BaseAPIResponse wrapForResponse(List<T> results){
        BaseAPIResponse<T> baseAPIResponse = new BaseAPIResponse<>();
        baseAPIResponse.setData(results);
        return baseAPIResponse;
    }


    public BaseAPIResponse wrapForResponse(T result){
        BaseAPIResponse<T> baseAPIResponse = new BaseAPIResponse<>();
        baseAPIResponse.addData(result);
        return baseAPIResponse;
    }


    /**
     * Name is longer to differentiate signature between methods that take Optional<T>
     */
    public BaseAPIResponse wrapForResponseListOfObjects(Optional<List<T>> results){
        BaseAPIResponse<T> baseAPIResponse = new BaseAPIResponse<>();
        if(results.isPresent())
            baseAPIResponse.setData(results.get());
        return baseAPIResponse;
    }


    public BaseAPIResponse wrapForResponse(Optional<T> result){
        BaseAPIResponse<T> baseAPIResponse = new BaseAPIResponse<>();
        if(result.isPresent())
            baseAPIResponse.addData(result.get());
        return baseAPIResponse;
    }
}
