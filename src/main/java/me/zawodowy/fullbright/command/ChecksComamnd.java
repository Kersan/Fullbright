package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ChecksComamnd implements Command<FabricClientCommandSource> {


    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        String content = context.getInput().replaceFirst("checks ", "");
        String[] names = content.split(" ");

        new Thread(() -> {

            for (String name : names){
                try {
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    if (con.getResponseCode() == 200){
                        context.getSource().sendFeedback(Text.of("§e§l" + name + " §ejest kontem premium :("));
                        context.getSource().getPlayer().playSound(SoundEvents.ENTITY_VILLAGER_NO, 1f, 1f);
                    }

                    else if (con.getResponseCode() == 204) {
                        context.getSource().sendFeedback(Text.of("§a§l" + name + " §anie jest kontem premium :-)"));
                        context.getSource().getPlayer().playSound(SoundEvents.ENTITY_CAT_AMBIENT, 1f, 1f);
                    }

                    else {
                        context.getSource().sendFeedback(Text.of("§cCoś poszło nie tak :/"));
                        context.getSource().getPlayer().playSound(SoundEvents.ENTITY_VILLAGER_NO, 1f, 1f);
                    }



                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();

        return 1;
    }
}
