package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.IgiConfig;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyAccessUtils;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyUserInfo;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.TrackPlaying;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.button.IMouseClickButton;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.api.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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

    @Reactive(targetUid = "trackTitle", property = "xShiftSpeed", initiativeSync = true)
    public ReactiveObject<Float> trackTitleXShiftSpeed = new ReactiveObject<>(){};

    @Reactive(targetUid = "progressBar", property = "percentage", initiativeSync = true)
    public ReactiveObject<Float> progressBarPercentage = new ReactiveObject<>(){};

    @Reactive(targetUid = "editButton", property = "enabled", initiativeSync = true)
    public ReactiveObject<Boolean> editButtonEnabled = new ReactiveObject<>(){};

    @Reactive(targetUid = "editButton", property = "addClickListener", initiativeSync = true)
    public ReactiveObject<IMouseClickButton> editButtonAddClickListener = new ReactiveObject<>(){};

    private float durationMs = 0;
    private float estimatedProgressMs;
    private boolean isPlaying = false;

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
                if (trackPlaying.trackExists)
                {
                    trackTitleXShiftSpeed.set(8f);
                    if (!albumImageUrl.get().equals(trackPlaying.albumImage300by300))
                        albumImageUrl.set(trackPlaying.albumImage300by300);
                    if (!trackTitleText.get().equals(trackPlaying.trackName))
                        trackTitleText.set(trackPlaying.trackName);
                    float percentage = 0f;
                    if (trackPlaying.durationMs != 0)
                        percentage = ((float)trackPlaying.progressMs) / ((float)trackPlaying.durationMs);
                    progressBarPercentage.set(percentage);
                    durationMs = trackPlaying.durationMs;
                    estimatedProgressMs = trackPlaying.progressMs;
                    isPlaying = trackPlaying.isPlaying;
                }
            }
            catch (Exception ignored) { }
            return null;
        });
    }

    @Override
    public void start()
    {
        isActiveSetter.invoke(false);
        exitCallbackSetter.invoke(() ->
        {
            editButtonEnabled.set(false);
            isFocusedSetter.invoke(false);
            return false;
        });

        albumImageUrl.set("");
        progressBarPercentage.set(0f);
        editButtonEnabled.set(false);
        editButtonAddClickListener.set((IMouseClickButton)(() ->
        {
            if (Minecraft.getMinecraft().player != null)
                Minecraft.getMinecraft().player.sendChatMessage("test");
        }));

        EventCenter.spotifyOverlayEvent.addListener((flag) ->
        {
            if (flag)
            {
                if (SpotifyUserInfo.token.accessToken.isEmpty())
                {
                    EntityPlayerSP player = Minecraft.getMinecraft().player;
                    if (player != null)
                    {
                        player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " The access token is empty."));
                        player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Please run the command #spotify-oauth first."));
                    }
                    return;
                }

                isActiveSetter.invoke(true);
                trackTitleXShiftSpeed.set(20f);
                trackTitleText.set("Please wait... And make sure you play a track on Spotify");

                refreshTokenIfNeeded(() ->
                {
                    try
                    {
                        TrackPlaying trackPlaying = SpotifyAccessUtils.getCurrentlyPlaying(SpotifyUserInfo.token.accessToken);
                        if (trackPlaying.trackExists)
                        {
                            trackTitleXShiftSpeed.set(8f);
                            albumImageUrl.set(trackPlaying.albumImage300by300);
                            trackTitleText.set(trackPlaying.trackName);
                            float percentage = 0f;
                            if (trackPlaying.durationMs != 0)
                                percentage = ((float)trackPlaying.progressMs) / ((float)trackPlaying.durationMs);
                            progressBarPercentage.set(percentage);
                            durationMs = trackPlaying.durationMs;
                            estimatedProgressMs = trackPlaying.progressMs;
                            isPlaying = trackPlaying.isPlaying;
                        }
                    }
                    catch (Exception ignored) { }
                });
            }
            else
                isActiveSetter.invoke(false);
        });

        EventCenter.spotifyOverlayEditEvent.addListener(() ->
        {
            if (isActiveGetter.invoke())
            {
                editButtonEnabled.set(true);
                isFocusedSetter.invoke(true);
            }
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
                        if (IgiConfig.SPOTIFY_AUTO_DISPLAY)
                            EventCenter.spotifyOverlayEvent.trigger(true);
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

        if (progressBarPercentage.get() < 1 && isPlaying)
        {
            int timeMs = (int)(deltaTime * 1000);
            estimatedProgressMs += timeMs;
            if (durationMs != 0)
            {
                if (estimatedProgressMs >= durationMs)
                {
                    estimatedProgressMs = durationMs;
                    refreshTrackTimer = 1.5f;
                }
                float percentage = estimatedProgressMs / durationMs;
                progressBarPercentage.set(percentage);
            }
        }
    }
}
