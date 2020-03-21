package com.example.provasensori;

import android.content.Context;
import android.media.AudioManager;

public class Utils {
    public static boolean isCallActive(Context context){
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode()==AudioManager.MODE_IN_CALL;
    }
}
