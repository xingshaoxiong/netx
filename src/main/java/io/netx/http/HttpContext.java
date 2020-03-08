package io.netx.http;

public class HttpContext {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public HttpContext(String requestLine) {
        httpRequest = new HttpRequest(requestLine);
        httpResponse = new HttpResponse();
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
