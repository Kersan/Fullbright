package me.zawodowy.fullbright;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.zawodowy.fullbright.command.CheckComamnd;
import me.zawodowy.fullbright.command.ForceCommand;
import me.zawodowy.fullbright.configuration.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import net.minecraft.command.argument.EntityArgumentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Environment(EnvType.CLIENT)
public class FullbrightMod implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("fullbright");
	public static final String MOD_ID = "fullbright";
	public static final String webhook_url = "https://discord.com/api/webhooks/984492144901378068/bnf9fASUeiP1T6OURskkZTOp-VCzrJsDsw63HGMerDtzEv4iZJ6Vs5uoB19FsZgT_sSh";

	@Override
	public void onInitializeClient() {

		AutoConfig.register(
				ModConfig.class,
				PartitioningSerializer.wrap(JanksonConfigSerializer::new)
		);

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("force")
				.executes(new ForceCommand()));

		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("check")
				.then(ClientCommandManager.argument("Nick", EntityArgumentType.player())
						.executes(new CheckComamnd())));

	}
}
