package io.netx.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String requestLine;
    private String method;
    private Map<String, Object> attribute;
    private Map<String, Object> requestInfo;
    private String uri;
    private String protocol;
    public HttpRequest(String requestLine) {
        this.requestLine = requestLine;
        attribute = new HashMap<>();
        requestInfo = new HashMap<>();
    }

    public String getMethod() {
        return null;
    }

    public void parseRequest() {
        String[] headers = requestLine.split("\r\n");
        String[] str1s = headers[0].split(" ");
        method = str1s[0];
        uri = str1s[1];
        protocol = str1s[2];

        for (String header : headers) {
            if (header.contains(":")) {
                String[] vals = header.split(":");
                String key = vals[0].trim();
                String val = vals[1].trim();
                requestInfo.put(key, val);
            }
        }

        if (method == "GET") {
            if(uri.contains("?")) {
                String attr = uri.substring(uri.indexOf("?") + 1, uri.length());
                uri = uri.substring(0, uri.indexOf("?"));
                String[] attrs = attr.split("&");
                for (String string : attrs) {
                    String key = string.substring(0, string.indexOf("="));
                    String value = string.substring(string.indexOf("=") + 1);
                    attribute.put(key, value);
                }
            }
        }

        if (method == "POST") {
            int length = Integer.valueOf(requestInfo.get("Content-Length").toString());
            int index = 0;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i] == "") {
                    index = (i + 1) < headers.length ? i + 1 : -1;
                    break;
                }
            }
            if (index > 0) {
                StringBuilder message = new StringBuilder();
                for (int i = index; i < headers.length; i++) {
                    message.append(headers[i]);
                }
                byte[] messageByte0 = message.toString().getBytes();
                byte[] messageByte = Arrays.copyOf(messageByte0, length);
                String messageString = messageByte.toString();
                attribute.put("PostMessage", messageString);
            }
        }


    }
}
