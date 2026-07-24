package cn.thesilentnights.easylogin.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class MessageSender {
    public static void sendMessage(CommandContext<CommandSourceStack> pContext, String message, MessageType type) throws CommandSyntaxException {
        sendMessage(pContext.getSource().getPlayerOrException(), message, type);
    }

    public static void sendMessage(CommandContextBuilder<CommandSourceStack> pContext, String message, MessageType type) throws CommandSyntaxException {
        sendMessage(pContext.getSource().getPlayerOrException(), message, type);
    }

    public static void sendMessage(Player p, String message, MessageType type){
        MutableComponent component = serizeMessage(message, type);
        p.sendMessage(component,p.getUUID());
    }

    public static void sendMessage(Player p, MutableComponent message, MessageType type){
        MutableComponent component = serizeMessage(message, type);
        p.sendMessage(component,p.getUUID());
    }

    public static void sendMessage(PlayerEvent event, String message, MessageType type){
        sendMessage(event.getPlayer(), message, type);
    }

    private static MutableComponent serizeMessage(String message, MessageType type){
        MutableComponent component = new TextComponent(message);
        return serizeMessage(component, type);
    }

    private static MutableComponent serizeMessage(MutableComponent message, MessageType type){
        MutableComponent head = new TextComponent("[EasyLogin]: ").withStyle(ChatFormatting.LIGHT_PURPLE);
        switch (type){
            case INFO:
                head.append(message.withStyle(ChatFormatting.GRAY));
                break;
            case ERROR:
                head.append(message.withStyle(ChatFormatting.RED));
                break;
            case SUCCESS:
                head.append(message.withStyle(ChatFormatting.GREEN));
                break;
        }
        return head; 
    }

    public enum MessageType{
        INFO,
        ERROR,
        SUCCESS,
    }
}
