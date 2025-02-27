package io.github.simplycmd.terracraft.blocks;

import io.github.simplycmd.terracraft.entities.coin_portal.CoinPortalEntity;
import io.github.simplycmd.terracraft.registry.BlockRegistry;
import io.github.simplycmd.terracraft.registry.EntityRegistry;
import io.github.simplycmd.terracraft.registry.ItemRegistry;
import io.github.simplycmd.terracraft.registry.SoundRegistry;
import lombok.Getter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PotBlock extends Block implements Waterloggable {
    private static final Random RANDOM = new Random();
    
    @Getter
    private final int variants;

    @Getter
    private final double coin_portal_chance;

    @Getter
    private final Item torch_type;

    @Getter
    private final float base_money_modifier;

    public static final IntProperty VARIANT = IntProperty.of("variant", 0, 3);

    public PotBlock(int variants, double coin_portal_chance, Item torch_type, float base_money_modifier) {
        super(FabricBlockSettings.of((new Material.Builder(MapColor.TERRACOTTA_BROWN)).build()).breakInstantly().noCollision().sounds(new BlockSoundGroup(1.0F, 1.0F, SoundRegistry.BLOCK_POT_SMASH_EVENT, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL)));
        setDefaultState(getStateManager().getDefaultState().with(VARIANT, 0));
        this.variants = variants;
        this.coin_portal_chance = coin_portal_chance;
        this.torch_type = torch_type;
        this.base_money_modifier = base_money_modifier;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(VARIANT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(VARIANT, (int) Math.round((Math.random() * (getVariants() - 1))));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.625, 0.75);
    }

    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);

        // Check if coin portal (ends process)
        if (RANDOM.nextFloat() < getCoin_portal_chance()) spawnCoinPortal(world, pos);
        else {
            // TODO: Give gold key next to dungeon
            // TODO: Drop a potion (ends process)
            // TODO: In multiplayer drop wormhole potion (ends process)

            // Random number from 1-8 (7 outcomes)
            switch(RANDOM.nextInt(7) + 1) {
                // 1: Drop hearts
                case 1: {
                    dropHearts(world, pos);
                    break;
                }
                // 2: Drop torches
                case 2: {
                    dropTorches(world, pos);
                    break;
                }
                // 3: TODO: Drop ammo
                case 3: ;
                // 4: TODO: Drop healing potions
                case 4: ;
                // 5: TODO: Drop explosives
                case 5: ;
                // 6: TODO: Drop rope
                case 6: ;
                // 7/8: Drop money
                case 7: {
                    dropCoins(world, pos);
                    break;
                }
                case 8: {
                    dropCoins(world, pos);
                    break;
                }
            }
        }
    }

    private void spawnCoinPortal(World world, BlockPos potPos) {
        CoinPortalEntity portal = new CoinPortalEntity(EntityRegistry.COIN_PORTAL, world);
        portal.updatePosition(potPos.getX(), potPos.getY() + 2, potPos.getZ());
        world.spawnEntity(portal);
    }

    private void dropHearts(World world, BlockPos pos) {
        if (world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10000, true).getHealth() < 20) {
            // Spawn first heart
            ItemEntity heart = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ItemRegistry.heart.getItem().getDefaultStack());
            world.spawnEntity(heart);

            // Spawn second heart
            if (RANDOM.nextFloat() < 0.5) {
                ItemEntity heart2 = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ItemRegistry.heart.getItem().getDefaultStack());
                world.spawnEntity(heart2);
            }
        }
    }

    private void dropTorches(World world, BlockPos pos) {
        // TODO: drop glowsticks instead when in water

        // Determines an amount from 4-12
        int amount = RANDOM.nextInt(8) + 4;

        // Tundra drops half the amount of torches
        if (equals(BlockRegistry.tundra_pot.getBlock()))
            for (int i = 0; i < (amount / 2); i++) {
                ItemEntity torch = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getTorch_type().getDefaultStack());
                world.spawnEntity(torch);
            }
        else
            for (int i = 0; i < amount; i++) {
                ItemEntity torch = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getTorch_type().getDefaultStack());
                world.spawnEntity(torch);
            }
    }

    private void dropCoins(World world, BlockPos pos) {
        // Spawn silver coins from 0-3
        for (int i = 0; i < RANDOM.nextInt(3); i++) {
            ItemEntity coin = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ItemRegistry.silver_coin.getItem().getDefaultStack());
            world.spawnEntity(coin);
        }

        // Spawn copper coins from 0-64
        ItemStack copper_coins = ItemRegistry.copper_coin.getItem().getDefaultStack();
        copper_coins.setCount(RANDOM.nextInt(64));
        ItemEntity coin = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), copper_coins);
        world.spawnEntity(coin);
    }
}
