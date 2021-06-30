package io.github.simplycmd.terracraft.blocks.util;

import com.mojang.datafixers.DSL;
import io.github.simplycmd.terracraft.registry.BlockRegistry;
import lombok.Getter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public enum PotBlocks {
    FOREST(4, 1/500.0, Items.TORCH, 1.0F),
    TUNDRA(3, 1/461.0, BlockRegistry.get("ice_torch").asItem(), 1.25F),
    JUNGLE(3, 1/400.0, BlockRegistry.get("jungle_torch").asItem(), 1.75F),
    DUNGEON(3, 1/384.0, Items.TORCH, 1.9F),
    UNDERWORLD(3, 1/365.0, Items.TORCH, 2.1F),
    CORRUPT(3, 1/365.0, BlockRegistry.get("corrupt_torch").asItem(), 1.6F),
    CRIMSON(3, 1/365.0, BlockRegistry.get("crimson_torch").asItem(), 1.6F),
    HALLOWED(3, 1/365.0, BlockRegistry.get("hallowed_torch").asItem(), 1.6F),
    SPIDER(3, 1/272.0, Items.TORCH, 3.5F),
    PYRAMID(3, 1/125.0, Items.TORCH, 10.0F),
    LIHZAHRD(3, 1/250.0, Items.TORCH, 4.0F), // TODO: Portal values are 1/250 for hm and 1/500 for pre-hm AND modifier is 4.0F hm and 1.0F pre-hm
    MARBLE(3, 1/375.0, Items.TORCH, 2.0F),
    DESERT(3, 1/461.0, BlockRegistry.get("desert_torch").asItem(), 1.25F);

    @Getter
    private final int variants;

    @Getter
    private final double coin_portal_chance;

    @Getter
    private final Item torch_type;

    @Getter
    private final float base_money_modifier;

    PotBlocks(int variants, double coin_portal_chance, Item torch_type, float base_money_modifier) {
        this.variants = variants;
        this.coin_portal_chance = coin_portal_chance;
        this.torch_type = torch_type;
        this.base_money_modifier = base_money_modifier;
    }
}