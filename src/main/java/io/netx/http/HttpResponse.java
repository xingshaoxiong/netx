package io.netx.http;

public class HttpResponse {
    private int statusCode;
    private String protocol;
    private String contentType;
    private String responsline;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResponsline() {
        return responsline;
    }

    public void setResponsline(String responsline) {
        this.responsline = responsline;
    }
}
