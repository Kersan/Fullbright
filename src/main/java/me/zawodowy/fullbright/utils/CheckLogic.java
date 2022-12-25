package me.zawodowy.fullbright.utils;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckLogic {

    public static void checkMojangNickname(
            String name, CommandContext<FabricClientCommandSource> context) throws IOException {

        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        switch (con.getResponseCode()){
            case 200:
                CheckLogic.checkCommandFeedback(
                        context, "§e§l" + name + " §ejest kontem premium :(", false);
                break;

            case 204:
                CheckLogic.checkCommandFeedback(
                        context, "§a§l" + name + " §anie jest kontem premium :-)", true);
                break;

            default:
                CheckLogic.checkCommandFeedback(context, "§cCoś poszło nie tak :/", false);
                break;
        }
    }

    public static void checkCommandFeedback(
            CommandContext<FabricClientCommandSource> context, String message, boolean isPositive) {

        SoundEvent sound = !isPositive ? SoundEvents.ENTITY_CAT_AMBIENT : SoundEvents.ENTITY_VILLAGER_NO;

        context.getSource().sendFeedback(Text.of(message));
        context.getSource().getPlayer().playSound(sound, 1f, 1f);
    }
}
