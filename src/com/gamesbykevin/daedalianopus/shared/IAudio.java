package com.gamesbykevin.daedalianopus.shared;

import com.gamesbykevin.daedalianopus.resources.GameAudio;

public interface IAudio 
{
    public void setAudioKey(final GameAudio.Keys audioKey);
    
    public GameAudio.Keys getAudioKey();
}