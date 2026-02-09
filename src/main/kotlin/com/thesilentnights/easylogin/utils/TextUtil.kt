package com.thesilentnights.easylogin.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TranslatableComponent

object TextUtil {
    /***
     * create MutableComponent for message sending
     * @param chatFormatting  color
     * @param key  message key used for translation
     * @param args message args
     * @return MutableComponent
     */
    fun createText(chatFormatting: ChatFormatting, key: String, vararg args: Any?): MutableComponent {
        val translatableComponent = TranslatableComponent(key, *args)
        return translatableComponent.withStyle(chatFormatting)
    }

    fun createBold(color: ChatFormatting, key: String, vararg args: Any?): MutableComponent {
        val translatableComponent = TranslatableComponent(key, *args)
        return translatableComponent.withStyle(ChatFormatting.BOLD).withStyle(color)
    }

    fun createText(key: String, vararg args: Any?): MutableComponent {
        return TranslatableComponent(key, *args)
    }
}
