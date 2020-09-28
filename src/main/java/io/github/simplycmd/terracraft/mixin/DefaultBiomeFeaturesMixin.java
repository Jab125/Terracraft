package io.github.simplycmd.terracraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import io.github.simplycmd.terracraft.Terracraft;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
  @Inject(method = "addDefaultOres(Lnet/minecraft/world/biome/GenerationSettings$Builder;)V", at = @At("TAIL"))
  private static void addDefaultOres(GenerationSettings.Builder builder, CallbackInfo ci) {
    for (int i = 0; i < Terracraft.ores.size(); i++) {
      builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, Terracraft.ores.get(i));
    }
  }
}