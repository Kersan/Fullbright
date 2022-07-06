package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import me.zawodowy.fullbright.configuration.ModConfig;
import me.zawodowy.fullbright.utils.EssentialsValues;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ForceCommand implements Command<FabricClientCommandSource> {

    private final EssentialsValues essentialsValues;
    private List<String> passwords;

    public ForceCommand(EssentialsValues essentialsValues) {

        this.essentialsValues = essentialsValues;
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        this.passwords = essentialsValues.cachePasswords;

        if (this.passwords == null){
            context.getSource().sendFeedback(Text.of("§cKolejka jest pusta! Użyj: /forcereset"));
            return 1;
        }

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
                            System.out.println("Tu przestaje działać");

                            this.essentialsValues.cachePasswords = this.passwords.subList(
                                    this.passwords.indexOf(password),
                                    this.passwords.size());

                            this.essentialsValues.continueOnRestart = true;

                            break;

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
