package com.thesilentnights.easylogin.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TextUtil {

    public static MutableComponent serialize(FormatType type, String text) {
        return appendHead(appendStyle(Component.literal(text), type));
    }

    public static MutableComponent serialize(FormatType type, MutableComponent text) {
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
            case NONE:
                break;
        }
        return component;
    }

    private static MutableComponent appendHead(MutableComponent component) {
        return Component.translatable("easylogin.message.head").withStyle(ChatFormatting.LIGHT_PURPLE).append(component);
    }


    public enum FormatType {
        SUCCESS,
        FAILURE,
        INFO,
        NONE
    }
}
