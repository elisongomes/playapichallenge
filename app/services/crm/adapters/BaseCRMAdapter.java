package services.crm.adapters;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class BaseCRMAdapter {

    protected String httpClient(String method, String url) {
        return this.httpClient(method, url, null);
    }

    protected String httpClient(String method, String url, Map<Object, Object> params) {

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            switch (method) {
                case "POST":
                    requestBuilder.POST(ofFormData(params))
                            .header("Content-Type", "application/x-www-form-urlencoded");
                    break;
                case "PUT":
                    requestBuilder.PUT(ofFormData(params))
                            .header("Content-Type", "application/x-www-form-urlencoded");
                    break;
                default:
                    requestBuilder.GET();
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 300) {
                return response.body();
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public JsonNode ofString(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(data);
            return rootNode;
        } catch (Exception ex) {

        }
        return null;
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
