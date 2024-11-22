package com.tttsaurus.ingameinfo.common.api.appcommunication.spotify;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.tttsaurus.ingameinfo.common.api.json.RawJsonUtils;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("all")
public final class SpotifyOAuthUtils
{
    private static final int CODE_LISTENING_SERVER_PORT = 8888;
    private static HttpServer codeListeningServer = null;

    public static void startCodeListeningServer() throws IOException
    {
        if (codeListeningServer == null)
        {
            codeListeningServer = HttpServer.create(new InetSocketAddress(CODE_LISTENING_SERVER_PORT), 0);
            codeListeningServer.createContext("/", new OAuthCodeHandler());
            codeListeningServer.setExecutor(null);
            codeListeningServer.start();
        }
    }

    static class OAuthCodeHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();

            if (query != null && query.contains("code="))
            {
                String authorizationCode = query.split("code=")[1];
                if (authorizationCode.contains("&"))
                    authorizationCode = authorizationCode.split("&")[0];

                try
                {
                    SpotifyUserInfo.token = getToken(authorizationCode);

                    String response = "<h1>[In-Game Info Reborn]</h1><br><h2>Authorization Successful</h2><p>You can close this window</p>";
                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) { os.write(responseBytes); }

                    exchange.getHttpContext().getServer().stop(0);
                    codeListeningServer = null;
                }
                catch (Exception e)
                {
                    String response = "<h1>[In-Game Info Reborn]</h1><br><h2>Exception</h2><p>" + e.getMessage() + "</p><p>Please try again</p>";
                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) { os.write(responseBytes); }

                    exchange.getHttpContext().getServer().stop(0);
                    codeListeningServer = null;
                }
            }
            else
            {
                String response = "<h1>[In-Game Info Reborn]</h1><br><h2>No Authorization Code Found</h2><p>Please try again</p>";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(responseBytes); }

                exchange.getHttpContext().getServer().stop(0);
                codeListeningServer = null;
            }
        }
    }

    public static final String CLIENT_ID = "";
    public static final String CLIENT_SECRET = "";
    private static final String REDIRECT_URI = "http://localhost:8888";  // Make sure this matches the redirect URI registered in your Spotify app
    private static final String AUTHORIZATION_URL = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    public static String generateAuthURL()
    {
        String scope = "user-read-private user-read-email user-read-playback-state user-read-currently-playing";  // Define the scope of access you want
        try
        {
            return AUTHORIZATION_URL + "?client_id=" + CLIENT_ID
                    + "&response_type=code"
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8.toString())
                    + "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException ignored) { }
        return "";
    }

    public static Token getToken(String authorizationCode) throws IOException
    {
        URL url = new URL(TOKEN_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "";
        try
        {
            params = "code=" + authorizationCode
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8.toString())
                    + "&grant_type=authorization_code"
                    + "&client_id=" + CLIENT_ID
                    + "&client_secret=" + CLIENT_SECRET;
        }
        catch (UnsupportedEncodingException ignored) { }

        try (OutputStream os = connection.getOutputStream())
        {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        BufferedReader reader;
        if (responseCode == 200)
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            responseBuilder.append(line);
        reader.close();

        String responseStr = responseBuilder.toString();
        if (responseCode != 200)
            throw new IOException("Error fetching tokens: " + responseStr);

        String accessToken = RawJsonUtils.extractValue(responseStr, "access_token");
        String refreshToken = RawJsonUtils.extractValue(responseStr, "refresh_token");
        String expiresIn = RawJsonUtils.extractValue(responseStr, "expires_in");

        return new Token(accessToken, refreshToken, Integer.parseInt(expiresIn));
    }

    public static void refreshAccessToken(Token token) throws IOException
    {
        URL url = new URL(TOKEN_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "";
        try
        {
            params = "grant_type=refresh_token"
                    + "&refresh_token=" + URLEncoder.encode(token.refreshToken, StandardCharsets.UTF_8.toString())
                    + "&client_id=" + CLIENT_ID
                    + "&client_secret=" + CLIENT_SECRET;
        }
        catch (UnsupportedEncodingException ignored) { }

        try (OutputStream os = connection.getOutputStream())
        {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        BufferedReader reader;
        if (responseCode == 200)
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            responseBuilder.append(line);
        reader.close();

        String responseStr = responseBuilder.toString();
        if (responseCode != 200)
            throw new IOException("Error refreshing access token: " + responseStr);

        String accessToken = RawJsonUtils.extractValue(responseStr, "access_token");
        String expiresIn = RawJsonUtils.extractValue(responseStr, "expires_in");

        token.accessToken = accessToken;
        token.expiresIn = Integer.parseInt(expiresIn);
        token.start = LocalDate.now();
    }
}
