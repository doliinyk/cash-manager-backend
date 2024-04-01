package com.cashmanagerbackend.utils;

import com.cashmanagerbackend.entities.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class Util {
    private static final String REDIRECT_URL = "redirectUrl";

    public static Map<String, Object> createObjectVariables(String redirectUrl, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<>();
        boolean isFrontendRequest = false;

        if (redirectUrl == null) {
            redirectUrl = request.getRequestURL()
                    .toString()
                    .replace("register", "activate")
                    .replace("user","auth/activate");
        } else {
            isFrontendRequest = true;
        }

        variables.put(REDIRECT_URL, redirectUrl);
        variables.put("isFrontendRequest", isFrontendRequest);

        return variables;
    }
    public static void putUserMailVariables(User user, Map<String, Object> variables) {
        variables.put("login", user.getLogin());
        variables.put(REDIRECT_URL,
                variables.get(REDIRECT_URL) + "?userId=" + user.getId() + "&activationToken=" + user.getActivationRefreshUUID());
    }
}
