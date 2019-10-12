package com.github.SimplyCmd.terracraft.config;

import com.github.SimplyCmd.terracraft.Main;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * For configuration settings that change the behaviour of code on the LOGICAL SERVER.
 * This can be moved to an inner class of ExampleModConfig, but is separate because of personal preference and to keep the code organised
 *
 * @author N_slash_A
 */
final class ServerConfig {

    final ForgeConfigSpec.BooleanValue serverBoolean;
    final ForgeConfigSpec.ConfigValue<List<String>> serverStringList;
    final ForgeConfigSpec.ConfigValue<DyeColor> serverEnumDyeColor;

    ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        serverBoolean = builder
                .comment("An example boolean in the server config")
                .translation(Main.MODID + ".config.serverBoolean")
                .define("serverBoolean", true);
        serverStringList = builder
                .comment("An example list of Strings in the server config")
                .translation(Main.MODID + ".config.serverStringList")
                .define("serverStringList", new ArrayList<>());
        serverEnumDyeColor = builder
                .comment("An example enum DyeColor in the server config")
                .translation(Main.MODID + ".config.serverEnumDyeColor")
                .defineEnum("serverEnumDyeColor", DyeColor.WHITE);
        builder.pop();
    }

}