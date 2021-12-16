package io.stipop.java_sample;

import io.stipop.models.SPSticker;

public class ChatItem {
    String message;
    SPSticker spSticker;

    public ChatItem(String msg, SPSticker sticker){
        message = msg;
        spSticker = sticker;
    }
}
