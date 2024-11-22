package com.tttsaurus.ingameinfo.common.api.appcommunication.spotify;

import java.util.ArrayList;
import java.util.List;

public class TrackPlaying
{
    public boolean trackExists = false;
    public List<String> artists = new ArrayList<>();
    public String albumImage640by640;
    public String albumImage300by300;
    public String albumImage64by64;
    public String trackName;
    public int progressMs;
    public boolean isPlaying;
}
