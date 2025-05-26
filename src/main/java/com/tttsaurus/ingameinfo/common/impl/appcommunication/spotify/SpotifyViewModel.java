package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.core.file.FileUtils;
import com.tttsaurus.ingameinfo.config.IgiConfig;
import com.tttsaurus.ingameinfo.common.core.appcommunication.spotify.SpotifyAccessUtils;
import com.tttsaurus.ingameinfo.common.core.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.core.appcommunication.spotify.SpotifyUserInfo;
import com.tttsaurus.ingameinfo.common.core.appcommunication.spotify.TrackPlaying;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.button.IMouseClickButton;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.Reactive;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.ReactiveObject;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import java.io.RandomAccessFile;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class SpotifyViewModel extends ViewModel<SpotifyView>
{
    // albumImageGroup
    @Reactive(targetUid = "albumImageGroup", property = "padding", initiativeSync = true)
    public ReactiveObject<Padding> albumImageGroupPadding = new ReactiveObject<>(){};

    // albumImage
    @Reactive(targetUid = "albumImage", property = "url", initiativeSync = true)
    public ReactiveObject<String> albumImageUrl = new ReactiveObject<>(){};

    @Reactive(targetUid = "albumImage", property = "width", initiativeSync = true)
    public ReactiveObject<Float> albumImageWidth = new ReactiveObject<>(){};

    @Reactive(targetUid = "albumImage", property = "height", initiativeSync = true)
    public ReactiveObject<Float> albumImageHeight = new ReactiveObject<>(){};

    @Reactive(targetUid = "albumImage", property = "padding", initiativeSync = true)
    public ReactiveObject<Padding> albumImagePadding = new ReactiveObject<>(){};

    // trackTitleGroup
    @Reactive(targetUid = "trackTitleGroup", property = "enabled", initiativeSync = true)
    public ReactiveObject<Boolean> trackTitleGroupEnabled = new ReactiveObject<>(){};

    // trackTitle
    @Reactive(targetUid = "trackTitle", property = "text", initiativeSync = true)
    public ReactiveObject<String> trackTitleText = new ReactiveObject<>(){};

    @Reactive(targetUid = "trackTitle", property = "xShiftSpeed", initiativeSync = true)
    public ReactiveObject<Float> trackTitleXShiftSpeed = new ReactiveObject<>(){};

    // progressBarGroup
    @Reactive(targetUid = "progressBarGroup", property = "padding", initiativeSync = true)
    public ReactiveObject<Padding> progressBarGroupPadding = new ReactiveObject<>(){};

    // progressBar
    @Reactive(targetUid = "progressBar", property = "percentage", initiativeSync = true)
    public ReactiveObject<Float> progressBarPercentage = new ReactiveObject<>(){};

    @Reactive(targetUid = "progressBar", property = "width", initiativeSync = true)
    public ReactiveObject<Float> progressBarWidth = new ReactiveObject<>(){};

    // anotherTrackTitleGroup
    @Reactive(targetUid = "anotherTrackTitleGroup", property = "enabled", initiativeSync = true)
    public ReactiveObject<Boolean> anotherTrackTitleGroupEnabled = new ReactiveObject<>(){};

    @Reactive(targetUid = "anotherTrackTitle", property = "text", initiativeSync = true)
    public ReactiveObject<String> anotherTrackTitleText = new ReactiveObject<>(){};

    @Reactive(targetUid = "anotherTrackTitle", property = "xShiftSpeed", initiativeSync = true)
    public ReactiveObject<Float> anotherTrackTitleXShiftSpeed = new ReactiveObject<>(){};

    @Reactive(targetUid = "anotherTrackAuthor", property = "text", initiativeSync = true)
    public ReactiveObject<String> anotherTrackAuthorText = new ReactiveObject<>(){};

    @Reactive(targetUid = "anotherTrackTimer", property = "text", initiativeSync = true)
    public ReactiveObject<String> anotherTrackTimerText = new ReactiveObject<>(){};

    // editButton
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

                    anotherTrackTitleXShiftSpeed.set(8f);
                    if (!anotherTrackTitleText.get().equals(trackPlaying.trackName))
                        anotherTrackTitleText.set(trackPlaying.trackName);
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < trackPlaying.artists.size(); i++)
                    {
                        builder.append(trackPlaying.artists.get(i));
                        if (i != trackPlaying.artists.size() - 1) builder.append(", ");
                    }
                    String authors = builder.toString();
                    if (!anotherTrackAuthorText.get().equals(authors))
                        anotherTrackAuthorText.set(authors);
                    anotherTrackTimerText.set(calcTimeText(percentage));
                }
            }
            catch (Exception ignored) { }
            return null;
        });
    }
    private String calcTimeText(float percentage)
    {
        int totalSeconds = (int)durationMs / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String time = String.format("%d:%02d", minutes, seconds);
        totalSeconds = (int)(percentage * totalSeconds);
        minutes = totalSeconds / 60;
        seconds = totalSeconds % 60;
        time = String.format("%d:%02d", minutes, seconds) + " / " + time;
        return time;
    }
    private void switchLayout()
    {
        if (IgiConfig.SPOTIFY_EXTENDED_LAYOUT)
        {
            trackTitleGroupEnabled.set(false);
            albumImageGroupPadding.set(new Padding(5f, 5f, 0f, 0f));
            albumImageWidth.set(60f);
            albumImageHeight.set(60f);
            albumImagePadding.set(new Padding(0f, 0f, 5f, 5f));
            progressBarGroupPadding.set(new Padding(0f, 5f, 0f, 0f));
            progressBarWidth.set(115f);
            anotherTrackTitleGroupEnabled.set(true);
        }
        else
        {
            trackTitleGroupEnabled.set(true);
            albumImageGroupPadding.set(new Padding(10f, 5f, 0f, 0f));
            albumImageWidth.set(40f);
            albumImageHeight.set(40f);
            albumImagePadding.set(new Padding(0f, 0f, 10f, 10f));
            progressBarGroupPadding.set(new Padding(0f, 10f, 0f, 0f));
            progressBarWidth.set(50f);
            anotherTrackTitleGroupEnabled.set(false);
        }
    }

    @Override
    public void start()
    {
        setActive(false);
        setExitCallback(() ->
        {
            editButtonEnabled.set(false);
            setFocused(false);
            return false;
        });

        albumImageUrl.set("");
        progressBarPercentage.set(0f);

        switchLayout();

        editButtonAddClickListener.set((IMouseClickButton)(() ->
        {
            if (IgiConfig.SPOTIFY_EXTENDED_LAYOUT)
            {
                IgiConfig.useSpotifyExtendedLayout(false);
                switchLayout();
            }
            else
            {
                IgiConfig.useSpotifyExtendedLayout(true);
                switchLayout();
            }
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

                setActive(true);
                trackTitleXShiftSpeed.set(20f);
                trackTitleText.set("Please wait... And make sure you play a track on Spotify");

                anotherTrackTitleXShiftSpeed.set(20f);
                anotherTrackTitleText.set("Please wait... And make sure you play a track on Spotify");
                anotherTrackAuthorText.set("");
                anotherTrackTimerText.set("");

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

                            anotherTrackTitleXShiftSpeed.set(8f);
                            anotherTrackTitleText.set(trackPlaying.trackName);
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < trackPlaying.artists.size(); i++)
                            {
                                builder.append(trackPlaying.artists.get(i));
                                if (i != trackPlaying.artists.size() - 1) builder.append(", ");
                            }
                            anotherTrackAuthorText.set(builder.toString());
                            anotherTrackTimerText.set(calcTimeText(percentage));
                        }
                    }
                    catch (Exception ignored) { }
                });
            }
            else
                setActive(false);
        });

        EventCenter.spotifyOverlayEditEvent.addListener(() ->
        {
            if (getActive())
            {
                editButtonEnabled.set(true);
                setFocused(true);
            }
        });

        // read refresh token and refresh
        try
        {
            RandomAccessFile file = new RandomAccessFile(FileUtils.getFile("spotify_refresh_token.txt", "cache"), "rw");
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
                String timeText = calcTimeText(percentage);
                if (!anotherTrackTimerText.get().equals(timeText))
                    anotherTrackTimerText.set(timeText);
            }
        }
    }
}
