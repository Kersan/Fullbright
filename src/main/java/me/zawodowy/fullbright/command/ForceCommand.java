package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import me.zawodowy.fullbright.configuration.ModConfig;
import me.zawodowy.fullbright.utils.EssentialsValues;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ForceCommand implements Command<FabricClientCommandSource> {

    private final EssentialsValues essentialsValues;
    private List<String> passwords = new ArrayList<>();

    public ForceCommand(EssentialsValues essentialsValues) {

        this.essentialsValues = essentialsValues;

        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/fullbright/hasla.txt");

            if (inputStream == null) {
                throw new NullPointerException("nie udało się załadować pliku hasła.txt");
            }

            BufferedReader readIn = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            this.passwords = readIn.lines().collect(Collectors.toList());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        try{
            if (MinecraftClient.getInstance().getCurrentServerEntry() == null){
                context.getSource().sendFeedback(Text.of("§cMusisz być na serwerze, być móc użyć tej komendy!"));
                return 0;
            }

            String ip = Objects.requireNonNull(MinecraftClient.getInstance().getCurrentServerEntry()).address;
            MinecraftClient mc = MinecraftClient.getInstance();


            new Thread(() -> {
                for (String password : this.passwords) {

                    try {

                        if (mc.getCurrentServerEntry() == null ||
                                !ip.equalsIgnoreCase(mc.getCurrentServerEntry().address)){

                            if (config.moduleConfig.stopOnDisconnect){
                                return;
                            }

                            else {
                                while (mc.getCurrentServerEntry() == null){
                                    Thread.sleep(1000);
                                }
                            }
                        }

                        if (context.getSource().getPlayer() == null){
                            return;
                        }


                        if (this.essentialsValues.continueForce){
                            System.out.println("Sprawdzane haslo: " + password);

                            context.getSource().getPlayer().sendChatMessage("/login " + password);
                            context.getSource().getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, 0.1f, 0.95f);
                            Thread.sleep(config.moduleConfig.delay);
                        }

                        else {
                            this.essentialsValues.continueForce = true;

                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        } catch (Exception e){

            e.printStackTrace();
        }

        return 1;
    }
}
