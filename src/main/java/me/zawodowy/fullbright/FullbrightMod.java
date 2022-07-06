package me.zawodowy.fullbright;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.zawodowy.fullbright.command.*;
import me.zawodowy.fullbright.configuration.ModConfig;
import me.zawodowy.fullbright.utils.EssentialsValues;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Environment(EnvType.CLIENT)
public class FullbrightMod implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("fullbright");
	public static final String MOD_ID = "fullbright";

	@Override
	public void onInitializeClient() {

		AutoConfig.register(
				ModConfig.class,
				PartitioningSerializer.wrap(JanksonConfigSerializer::new)
		);

		EssentialsValues essentialsValues = new EssentialsValues();

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("forcereset")
				.executes(new ForceResetCommand(essentialsValues)));

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("force")
				.executes(new ForceCommand(essentialsValues)));

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("forcestop")
				.executes(new ForceStopCommand(essentialsValues)));

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("check")
				.then(ClientCommandManager.argument("Nick", EntityArgumentType.player())
						.executes(new CheckComamnd())));

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("checks")
				.then(ClientCommandManager.argument("Nicki", MessageArgumentType.message())
						.executes(new ChecksComamnd())));

	}
}
