package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static ObjectNode jsonResponse(Object response, boolean success) {
        ObjectNode result = Json.newObject();
        result.put("success", success);
        if (response instanceof String) {
            result.put("message", (String) response);
        } else {
            result.putPOJO("data", response);
        }
        return result;
    }
    public static boolean validateEmail(String email) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(email);
        return matcher.find();
    }
}