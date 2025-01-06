package com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify;

import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyAccessUtils;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.TrackPlaying;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyUserInfo;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.UrlImageRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class SpotifyCommandHandler
{
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event)
    {
        String message = event.getMessage().getUnformattedText();
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return;
        String prefix = "<" + player.getName() + "> ";
        if (!message.startsWith(prefix)) return;
        message = message.substring(prefix.length());

        if (message.equals("#spotify-oauth"))
        {
            event.setCanceled(true);
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + message));
            String authUrl = SpotifyOAuthUtils.generateAuthURL();
            player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Visit the auth link below to authorize:"));
            player.sendMessage(new TextComponentString(""));
            TextComponentString linkText = new TextComponentString("Auth Link Here");
            linkText.setStyle((new Style()).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, authUrl)).setUnderlined(true).setColor(TextFormatting.BLUE));
            player.sendMessage(linkText);
            player.sendMessage(new TextComponentString(""));
            try { SpotifyOAuthUtils.startCodeListeningServer(); } catch (Exception ignored) { }
        }
        else if (message.startsWith("#spotify-oauth-code"))
        {
            event.setCanceled(true);
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + message));
            String[] args = message.split(" ");
            if (args.length == 2)
            {
                String authorizationCode = args[1];
                try
                {
                    SpotifyUserInfo.token = SpotifyOAuthUtils.getToken(authorizationCode);
                    player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Successfully got the tokens!"));
                    player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Access Token:"));
                    player.sendMessage(new TextComponentString(TextFormatting.BLUE + SpotifyUserInfo.token.accessToken));
                    player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Refresh Token:"));
                    player.sendMessage(new TextComponentString(TextFormatting.BLUE + SpotifyUserInfo.token.refreshToken));
                }
                catch (Exception e)
                {
                    player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Exception: " + e.getMessage()));
                }
            }
        }
        else if (message.startsWith("#spotify-ui-display"))
        {
            event.setCanceled(true);
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + message));
            String[] args = message.split(" ");
            if (args.length == 2)
            {
                String param = args[1];
                if (param.equals("true") || param.equals("false"))
                {
                    boolean flag = param.equals("true");
                    if (flag)
                    {
                        if (SpotifyUserInfo.token.accessToken.isEmpty())
                        {
                            player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " The access token is empty."));
                            return;
                        }
                        try
                        {
                            TrackPlaying trackPlaying = SpotifyAccessUtils.getCurrentlyPlaying(SpotifyUserInfo.token.accessToken);
                            player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " " + trackPlaying.trackName));
                            trackPlaying.artists.forEach(str -> player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " " + str)));
                            UrlImageRenderer.SHARED.updateURL(trackPlaying.albumImage640by640);
                        }
                        catch (Exception e)
                        {
                            player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Exception: " + e.getMessage()));
                            return;
                        }
                        // display
                    }
                }
            }
        }
        else if (message.equals("#spotify-refresh-token"))
        {
            event.setCanceled(true);
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + message));
            if (SpotifyUserInfo.token.refreshToken.isEmpty())
            {
                player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " The refresh token is empty."));
                return;
            }
            try
            {
                SpotifyOAuthUtils.refreshAccessToken(SpotifyUserInfo.token);
                player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Successfully refreshed the access token!"));
                player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Access Token:"));
                player.sendMessage(new TextComponentString(TextFormatting.BLUE + SpotifyUserInfo.token.accessToken));
            }
            catch (Exception e)
            {
                player.sendMessage(new TextComponentString(TextFormatting.AQUA + "[SpotifyBot]" + TextFormatting.RESET + " Exception: " + e.getMessage()));
            }
        }
    }
}
