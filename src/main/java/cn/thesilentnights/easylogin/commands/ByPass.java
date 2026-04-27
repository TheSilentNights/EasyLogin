package cn.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import cn.thesilentnights.easylogin.service.ByPassService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;

public class ByPass implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var bypass = Commands.literal("bypass");
        var playerArg = Commands.argument("player", GameProfileArgument.gameProfile());

        bypass.then(playerArg.executes(
                (CommandContext<CommandSourceStack> context) -> {
                    var player = GameProfileArgument.getGameProfiles(context, "player");
                    for (var p : player) {
                        ByPassService.addBypass(p.getId());
                    }
                    return 1;
                }));
    }

}
