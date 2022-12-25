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
    private final String passwordPath;

    public ForceResetCommand(EssentialsValues essentialsValues) {
        this.essentialsValues = essentialsValues;
        this.essentialsValues.cachePasswords = getPasswords();
        this.passwordPath = "assets/fullbright/hasla.txt";
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {
        this.essentialsValues.cachePasswords = getPasswords();
        return 0;
    }

    private List<String> getPasswords(){
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.passwordPath);

            if (inputStream == null) {
                throw new NullPointerException("Nie udało się załadować pliku hasła.txt");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader readIn = new BufferedReader(inputStreamReader);

            return readIn.lines().collect(Collectors.toList());

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
