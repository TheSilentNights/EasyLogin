package com.thesilentnights.easylogin.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class TextUtil {

    public static MutableComponent serialize(FormatType type, String text) {
        return appendHead(appendStyle(new TextComponent(text), type));
    }

    public static MutableComponent serialize(FormatType type, TranslatableComponent text) {
        return appendHead(appendStyle(text, type));
    }


    private static MutableComponent appendStyle(MutableComponent component, FormatType type) {
        switch (type) {
            case SUCCESS:
                component.withStyle(ChatFormatting.GREEN);
                break;
            case FAILURE:
                component.withStyle(ChatFormatting.RED);
                break;
            case INFO:
                component.withStyle(ChatFormatting.BLUE);
                break;
        }
        return component;
    }

    private static MutableComponent appendHead(MutableComponent component) {
        return new TranslatableComponent("easylogin.message.head").withStyle(ChatFormatting.LIGHT_PURPLE).append(component);
    }


    public enum FormatType {
        SUCCESS,
        FAILURE,
        INFO
    }
}
