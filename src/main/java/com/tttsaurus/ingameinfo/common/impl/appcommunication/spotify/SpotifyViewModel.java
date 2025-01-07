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

    private void refreshTokenIfNeeded()
    {
        long timeSpan = Duration.between(SpotifyUserInfo.token.start, LocalDateTime.now()).getSeconds();
        if (timeSpan >= (SpotifyUserInfo.token.expiresIn - 10))
        {
            CompletableFuture.supplyAsync(() ->
            {
                try
                {
                    SpotifyOAuthUtils.refreshAccessToken(SpotifyUserInfo.token);
                }
                catch (Exception e)
                {
                    SpotifyUserInfo.token.accessToken = "";
                }
                return null;
            });
        }
    }
    private void refreshTokenIfNeeded(Runnable asyncThen)
    {
        long timeSpan = Duration.between(SpotifyUserInfo.token.start, LocalDateTime.now()).getSeconds();
        if (timeSpan >= (SpotifyUserInfo.token.expiresIn - 10))
        {
            CompletableFuture.supplyAsync(() ->
            {
                try
                {
                    SpotifyOAuthUtils.refreshAccessToken(SpotifyUserInfo.token);
                }
                catch (Exception e)
                {
                    SpotifyUserInfo.token.accessToken = "";
                }
                return null;
            }).thenRun(asyncThen);
        }
        else
            CompletableFuture.supplyAsync(() ->
            {
                asyncThen.run();
                return null;
            });
    }
    private void refreshTrackInfo()
    {
        CompletableFuture.supplyAsync(() ->
        {
            try
            {
                TrackPlaying trackPlaying = SpotifyAccessUtils.getCurrentlyPlaying(SpotifyUserInfo.token.accessToken);
                if (!albumImageUrl.get().equals(trackPlaying.albumImage300by300))
                    albumImageUrl.set(trackPlaying.albumImage300by300);
                if (!trackTitleText.get().equals(trackPlaying.trackName))
                    trackTitleText.set(trackPlaying.trackName);
            }
            catch (Exception ignored) { }
            return null;
        });
    }

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

                refreshTokenIfNeeded(() ->
                {
                    try
                    {
                        TrackPlaying trackPlaying = SpotifyAccessUtils.getCurrentlyPlaying(SpotifyUserInfo.token.accessToken);
                        albumImageUrl.set(trackPlaying.albumImage300by300);
                        trackTitleText.set(trackPlaying.trackName);
                    }
                    catch (Exception ignored) { }
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
            file.close();

            String refreshToken = builder.toString();
            if (!refreshToken.isEmpty())
            {
                SpotifyUserInfo.token.refreshToken = refreshToken;
                CompletableFuture.supplyAsync(() ->
                {
                    boolean success = false;
                    try
                    {
                        SpotifyOAuthUtils.refreshAccessToken(SpotifyUserInfo.token);
                        success = true;
                    }
                    catch (Exception e)
                    {
                        SpotifyUserInfo.token.accessToken = "";
                    }

                    if (success)
                    {
                        try
                        {
                            SpotifyUserInfo.userName = SpotifyAccessUtils.getUserName(SpotifyUserInfo.token.accessToken);
                        }
                        catch (Exception ignored) { }
                    }
                    return null;
                });
            }
        }
        catch (Exception ignored) { }
    }

    private float refreshTokenTimer = 0;
    private float refreshTrackTimer = 0;
    @Override
    public void onFixedUpdate(double deltaTime)
    {
        refreshTokenTimer += (float)deltaTime;
        if (refreshTokenTimer > 5f)
        {
            refreshTokenTimer -= 5f;
            refreshTokenIfNeeded();
        }

        refreshTrackTimer += (float)deltaTime;
        if (refreshTrackTimer > 3f)
        {
            refreshTrackTimer -= 3f;
            refreshTrackInfo();
        }
    }
}
