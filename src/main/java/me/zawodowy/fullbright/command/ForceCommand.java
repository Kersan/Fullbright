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

        if (MinecraftClient.getInstance().getCurrentServerEntry() == null){
            context.getSource().sendFeedback(Text.of("§cMusisz być na serwerze, być móc użyć tej komendy!"));
            return 1;
        }

        String ip = Objects.requireNonNull(MinecraftClient.getInstance().getCurrentServerEntry()).address;
        MinecraftClient mc = MinecraftClient.getInstance();

        new Thread(() -> {
            try {
                this.loopPasswords(this.passwords, mc, ip, config, context);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        return 0;
    }

    private void loopPasswords(List<String> passwords, MinecraftClient mc, String ip, ModConfig config,
                               CommandContext<FabricClientCommandSource> context) throws InterruptedException {
        for (String password : passwords) {

            if (mc.getCurrentServerEntry() == null ||
                    !ip.equalsIgnoreCase(mc.getCurrentServerEntry().address)){
                this.kickedWhileForce(config, ip, password);
                break;
            }

            if (context.getSource().getPlayer() == null){
                return;
            }

            if (!this.essentialsValues.continueForce) {
                this.essentialsValues.continueForce = true;
                return;
            }

            // TODO: Informacja o obecnym haśle dla użytkownika
            System.out.println("Sprawdzane haslo: " + password);

            context.getSource().getPlayer().sendChatMessage("/login " + password);
            context.getSource().getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, 0.1f, 0.95f);
            Thread.sleep(config.moduleConfig.delay);
        }
    }

    private void kickedWhileForce(ModConfig config, String ip, String password) {
        System.out.println("Wyrzucono z serwera podczas działania /force");

        if (config.moduleConfig.rememberOnDisconnect){
            return;
        }

        if (config.moduleConfig.continueOnRelog){
            this.essentialsValues.continueOnRestart = true;
            this.essentialsValues.lastServerAddres = ip;
        }

        this.essentialsValues.cachePasswords = this.passwords.subList(
            this.passwords.indexOf(password),
            this.passwords.size()
        );
    }
}
