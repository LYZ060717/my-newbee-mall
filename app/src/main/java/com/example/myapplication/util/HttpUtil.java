package com.example.myapplication.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 网络请求工具类（基于 HttpURLConnection）
 */
public class HttpUtil {

    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 15000;

    /**
     * GET 请求
     */
    public static String get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * GET 请求（携带 Token）
     */
    public static String get(String url, String token) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("token", token);
            }

            return readResponse(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * POST 请求
     */
    public static String post(String url, String jsonStr) throws IOException {
        return post(url, jsonStr, null);
    }

    /**
     * POST 请求（携带 Token）
     */
    public static String post(String url, String jsonStr, String token) throws IOException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("token", token);
            }

            os = connection.getOutputStream();
            os.write(jsonStr.getBytes(StandardCharsets.UTF_8));
            os.flush();

            return readResponse(connection);
        } finally {
            if (os != null) {
                os.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 读取响应内容（自动区分成功/失败流）
     */
    private static String readResponse(HttpURLConnection connection) throws IOException {
        int code = connection.getResponseCode();
        InputStream is;

        if (code >= 200 && code < 300) {
            is = connection.getInputStream();
        } else {
            is = connection.getErrorStream();
        }

        if (is == null) {
            throw new IOException("响应为空，HTTP code: " + code);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
