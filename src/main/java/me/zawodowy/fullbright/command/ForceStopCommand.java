package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import me.zawodowy.fullbright.configuration.ModConfig;
import me.zawodowy.fullbright.utils.EssentialsValues;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ForceStopCommand implements Command<FabricClientCommandSource> {

    private final EssentialsValues essentialsValues;

    public ForceStopCommand(EssentialsValues essentialsValues) {
        this.essentialsValues = essentialsValues;
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {
        this.essentialsValues.continueForce = false;

        return 0;
    }
}
