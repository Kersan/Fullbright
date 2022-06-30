package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import me.zawodowy.fullbright.configuration.ModConfig;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckComamnd implements Command<FabricClientCommandSource> {


    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        String[] name = context.getInput().split(" ");

        new Thread(() -> {

            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name[1]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                if (con.getResponseCode() == 200){
                    context.getSource().sendFeedback(Text.of("§e§l" + name[1] + " §ejest kontem premium :("));
                    context.getSource().getPlayer().playSound(SoundEvents.ENTITY_VILLAGER_NO, 1f, 1f);
                }

                else if (con.getResponseCode() == 204) {
                    context.getSource().sendFeedback(Text.of("§a§l" + name[1] + " §anie jest kontem premium :-)"));
                    context.getSource().getPlayer().playSound(SoundEvents.ENTITY_CAT_AMBIENT, 1f, 1f);
                }

                else {
                    context.getSource().sendFeedback(Text.of("§cCoś poszło nie tak :/"));
                    context.getSource().getPlayer().playSound(SoundEvents.ENTITY_VILLAGER_NO, 1f, 1f);
                }



            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        return 1;
    }
}
