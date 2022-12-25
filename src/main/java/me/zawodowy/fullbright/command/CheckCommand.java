package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import static me.zawodowy.fullbright.utils.CheckLogic.checkMojangNickname;

public class CheckCommand implements Command<FabricClientCommandSource> {

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        String[] name = context.getInput().split(" ");

        new Thread(() -> {
            try {checkMojangNickname(name[0], context);}
            catch (Exception e) {throw new RuntimeException(e);}
        }).start();

        return 1;
    }
}
