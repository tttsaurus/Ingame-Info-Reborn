package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyAccessUtils;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyUserInfo;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.TrackPlaying;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class SpotifyViewModel extends ViewModel<SpotifyView>
{
    @Reactive(targetUid = "albumImage", property = "url", initiativeSync = true)
    public ReactiveObject<String> albumImageUrl = new ReactiveObject<>(){};

    @Reactive(targetUid = "trackTitle", property = "text", initiativeSync = true)
    public ReactiveObject<String> trackTitleText = new ReactiveObject<>(){};

    @Override
    public void start()
    {
        activeSetter.invoke(false);
        EventCenter.spotifyOverlayEvent.addListener((flag) ->
        {
            if (flag)
            {
                activeSetter.invoke(true);
                trackTitleText.set("Please wait...");
                CompletableFuture.supplyAsync(() ->
                {
                    try
                    {
                        TrackPlaying trackPlaying = SpotifyAccessUtils.getCurrentlyPlaying(SpotifyUserInfo.token.accessToken);
                        albumImageUrl.set(trackPlaying.albumImage300by300);
                        trackTitleText.set(trackPlaying.trackName);
                    }
                    catch (Exception ignored) { }
                    return null;
                });
            }
            else
                activeSetter.invoke(false);
        });

        // read refresh token and refresh
        File directory = new File("config/ingameinfo/cache");
        if (!directory.exists()) directory.mkdirs();
        try
        {
            RandomAccessFile file = new RandomAccessFile("config/ingameinfo/cache/spotify_refresh_token.txt", "rw");
            StringBuilder builder = new StringBuilder();
            String line = file.readLine();
            while (line != null)
            {
                builder.append(line);
                line = file.readLine();
            }

            String refreshToken = builder.toString();
            if (!refreshToken.isEmpty())
            {
                try
                {
                    SpotifyUserInfo.token.refreshToken = refreshToken;
                    SpotifyOAuthUtils.refreshAccessToken(SpotifyUserInfo.token);
                }
                catch (IOException e)
                {
                    file.setLength(0);
                }
            }

            file.close();
        }
        catch (Exception ignored) { }
    }

    private float refreshTokenTimer = 0;
    @Override
    public void onFixedUpdate(double deltaTime)
    {
        refreshTokenTimer += (float)deltaTime;
        if (refreshTokenTimer > 1f)
        {
            refreshTokenTimer -= 1f;
            long timeSpan = Duration.between(SpotifyUserInfo.token.start, LocalDateTime.now()).getSeconds();
            if (timeSpan >= (SpotifyUserInfo.token.expiresIn - 5))
            {
                CompletableFuture.supplyAsync(() ->
                {
                    try
                    {
                        SpotifyOAuthUtils.refreshAccessToken(SpotifyUserInfo.token);
                    }
                    catch (Exception ignored) { }
                    return null;
                });
            }
        }
    }
}
