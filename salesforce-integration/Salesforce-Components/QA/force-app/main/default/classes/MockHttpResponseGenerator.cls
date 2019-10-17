@isTest
global class MockHttpResponseGenerator implements HttpCalloutMock {
    // Implement this interface method
    /*global HTTPResponse respond(HTTPRequest req) {
         
        HttpResponse res = new HttpResponse();
        res.setHeader('Content-Type', 'application/json');
        res.setBody('{"access_token":"test"}');
        res.setStatusCode(200);
        return res;
    }*/
    protected Integer code;
		protected String status;
		protected String bodyAsString;
		protected Blob bodyAsBlob;
		protected Map<String, String> responseHeaders;
		
		public MockHttpResponseGenerator(Integer code, String status, String body,
                                         Map<String, String> responseHeaders) {
			this.code = code;
			this.status = status;
			this.bodyAsString = body;
			this.bodyAsBlob = null;
			this.responseHeaders = responseHeaders;
		}

		public MockHttpResponseGenerator(Integer code, String status, Blob body,
                                         Map<String, String> responseHeaders) {
			this.code = code;
			this.status = status;
			this.bodyAsBlob = body;
			this.bodyAsString = null;
			this.responseHeaders = responseHeaders;
		}

	    public HTTPResponse respond(HTTPRequest req) {
	        HttpResponse resp = new HttpResponse();
			resp.setStatusCode(code);
			resp.setStatus(status);
            resp.setHeader('Content-Type', 'application/json');
			if (bodyAsBlob != null) {
				resp.setBodyAsBlob(bodyAsBlob);
			} else {
				resp.setBody(bodyAsString);
			}

			if (responseHeaders != null) {
			     for (String key : responseHeaders.keySet()) {
				resp.setHeader(key, responseHeaders.get(key));
			     }
			}
			return resp;
	    }
    
}