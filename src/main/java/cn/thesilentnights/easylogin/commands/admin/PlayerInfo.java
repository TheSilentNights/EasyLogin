package cn.thesilentnights.easylogin.commands.admin;

import cn.thesilentnights.easylogin.commands.AdminCommands;
import cn.thesilentnights.easylogin.services.commands.PlayerInfoService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;

public class PlayerInfo extends AdminCommands {
        @Override
        protected LiteralArgumentBuilder<CommandSourceStack> registerAsAdmin(
                LiteralArgumentBuilder<CommandSourceStack> mainNode
        ) {
                var playerInfo = Commands.literal("playerInfo");

                var playerArgInfo = Commands.argument("player", GameProfileArgument.gameProfile());

                playerArgInfo.executes(
                        (var context) -> Dependencies.getDependency(PlayerInfoService.class).handle(context)
                                ? 1
                                : 0
                );

                return mainNode.then(playerInfo.then(playerArgInfo));
        }
}
