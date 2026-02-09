package com.thesilentnights.easylogin.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class TextUtil {

    /**
     * Create MutableComponent for message sending
     *
     * @param chatFormatting color
     * @param key            message key used for translation
     * @param args           message args
     * @return MutableComponent
     */
    public static MutableComponent createText(ChatFormatting chatFormatting, String key, Object... args) {
        TranslatableComponent translatableComponent = new TranslatableComponent(key, args);
        return translatableComponent.withStyle(chatFormatting);
    }

    public static MutableComponent createBold(ChatFormatting color, String key, Object... args) {
        TranslatableComponent translatableComponent = new TranslatableComponent(key, args);
        return translatableComponent.withStyle(ChatFormatting.BOLD).withStyle(color);
    }

    public static MutableComponent createText(String key, Object... args) {
        return new TranslatableComponent(key, args);
    }
}
