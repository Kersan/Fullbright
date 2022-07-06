package me.zawodowy.fullbright.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.zawodowy.fullbright.utils.EssentialsValues;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ForceResetCommand implements Command<FabricClientCommandSource> {

    private final EssentialsValues essentialsValues;

    public ForceResetCommand(EssentialsValues essentialsValues) {
        this.essentialsValues = essentialsValues;
        this.essentialsValues.cachePasswords = getPasswords();
    }


    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {

        this.essentialsValues.cachePasswords = getPasswords();

        System.out.println(this.essentialsValues.cachePasswords.size());

        return 1;
    }


    private List<String> getPasswords(){
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/fullbright/hasla.txt");

            if (inputStream == null) {
                throw new NullPointerException("nie udało się załadować pliku hasła.txt");
            }

            BufferedReader readIn = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return readIn.lines().collect(Collectors.toList());

        }catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
