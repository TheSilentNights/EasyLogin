package cn.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.fml.earlydisplay.theme.ThemeIds;

public abstract class AdminCommands extends PermissionRequired implements ICommands{
        private final LiteralArgumentBuilder<CommandSourceStack> mainNode = Commands.literal("easylogin").requires(
                stack -> {
                        return this.requireAdminPermission(stack) && this.requireLoginAuth(stack);
                }
        );

        @Override
        public void register(CommandDispatcher<CommandSourceStack> source) {
                LiteralArgumentBuilder<CommandSourceStack> command = this.registerAsAdmin(this.mainNode);
                source.register(command);
        }

        protected abstract LiteralArgumentBuilder<CommandSourceStack> registerAsAdmin(LiteralArgumentBuilder<CommandSourceStack> mainNode);
}
