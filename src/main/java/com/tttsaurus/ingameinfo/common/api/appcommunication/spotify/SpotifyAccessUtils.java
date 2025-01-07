package com.tttsaurus.ingameinfo.common.api.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.serialization.json.RawJsonUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("all")
public final class SpotifyAccessUtils
{
    private static final String CURRENTLY_PLAYING_URL = "https://api.spotify.com/v1/me/player/currently-playing";
    private static final String USER_PROFILE_URL = "https://api.spotify.com/v1/me";

    public static TrackPlaying getCurrentlyPlaying(String accessToken) throws Exception
    {
        URL url = new URL(CURRENTLY_PLAYING_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) response.append(inputLine);

                String rawJson = response.toString();
                TrackPlaying track = new TrackPlaying();
                track.trackExists = true;

                String progressMs = RawJsonUtils.extractValue(rawJson, "progress_ms");
                String isPlaying = RawJsonUtils.extractValue(rawJson, "is_playing");
                track.progressMs = Integer.parseInt(progressMs);
                track.isPlaying = Boolean.parseBoolean(isPlaying);

                String item = RawJsonUtils.extractValue(rawJson, "item");

                String trackName = RawJsonUtils.extractValue(item, "name");
                track.trackName = trackName;

                String album = RawJsonUtils.extractValue(item, "album");
                String images = RawJsonUtils.extractValue(album, "images");
                List<String> imageList = RawJsonUtils.splitArray(images);
                track.albumImage64by64 = RawJsonUtils.extractValue(imageList.get(2), "url");
                track.albumImage300by300 = RawJsonUtils.extractValue(imageList.get(1), "url");
                track.albumImage640by640 = RawJsonUtils.extractValue(imageList.get(0), "url");

                String artists = RawJsonUtils.extractValue(item, "artists");
                List<String> artistList = RawJsonUtils.splitArray(artists);
                artistList.forEach(str -> track.artists.add(RawJsonUtils.extractValue(str, "name")));

                return track;
            }
        }
        else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT)
            return new TrackPlaying();
        else
            throw new Exception("Failed to retrieve currently playing track: " + responseCode + " " + connection.getResponseMessage());
    }

    public static String getUserName(String accessToken) throws Exception
    {
        URL url = new URL(USER_PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) response.append(inputLine);

                return RawJsonUtils.extractValue(response.toString(), "display_name");
            }
        }
        else
            throw new Exception("Failed to retrieve user profile: " + responseCode + " " + connection.getResponseMessage());
    }
}
