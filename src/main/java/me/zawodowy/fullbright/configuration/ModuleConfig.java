package me.zawodowy.fullbright.configuration;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "module")
public class ModuleConfig implements ConfigData {

    @Comment("Opóźnienie w wysyłaniu komend")
    public int delay = 1000;

    @Comment("Zapamiętuje pozycję dziennika po wyrzuceniu")
    public boolean rememberOnDisconnect = true;

    @Comment("TYMCZASOWO USUNIĘTE")
    public boolean continueOnRelog = false;
}