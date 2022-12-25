package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import static me.zawodowy.fullbright.utils.CheckLogic.checkMojangNickname;


public class ChecksCommand implements Command<FabricClientCommandSource> {

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        String content = context.getInput().replaceFirst("checks ", "");
        String[] names = content.split(" ");

        new Thread(() -> {
            for (String name : names){
                try {checkMojangNickname(name, context);}
                catch (Exception e) {throw new RuntimeException(e);}
            }
        }).start();
        return 1;
    }
}
