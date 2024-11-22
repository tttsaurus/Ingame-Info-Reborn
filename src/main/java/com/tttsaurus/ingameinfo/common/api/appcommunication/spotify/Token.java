package com.tttsaurus.ingameinfo.common.api.appcommunication.spotify;

import java.time.LocalDate;

public class Token
{
    public String accessToken;
    public String refreshToken;

    public int expiresIn;
    public LocalDate start;

    public Token(String accessToken, String refreshToken, int expiresIn)
    {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.start = LocalDate.now();
    }

    public Token()
    {
        accessToken = "";
        refreshToken = "";
        expiresIn = 0;
        start = LocalDate.now();
    }
}
