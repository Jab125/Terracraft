package com.github.n_slash_a.terracraftia;

import com.google.common.base.Preconditions;
import com.github.n_slash_a.terracraftia.block.MiniModelBlock;
import com.github.n_slash_a.terracraftia.config.ConfigHelper;
import com.github.n_slash_a.terracraftia.config.ConfigHolder;
import com.github.n_slash_a.terracraftia.init.ModBlocks;
import com.github.n_slash_a.terracraftia.init.ModItemGroups;
import com.github.n_slash_a.terracraftia.tileentity.MiniModelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * Subscribe to events from the MOD EventBus that should be handled on both PHYSICAL sides in this class
 *
 * @author N_slash_A
 */
@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger(Main.MODID + " Mod Event Subscriber");

    /**
     * This method will be called by Forge when it is time for the mod to register its Blocks.
     * This method will always be called before the Item registry method.
     */
    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {
        // Register all your blocks inside this registerAll call
        event.getRegistry().registerAll(
                // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Iron Ore
                setup(new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)), "example_ore"),
                // This block has the IRON material, meaning it needs at least a stone pickaxe to break it. It is very similar to the Iron Block
                setup(new Block(Block.Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL)), "example_block"),
                // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Furnace
                setup(new MiniModelBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13)), "mini_model")
        );
        LOGGER.debug("Registered Blocks");
    }

    /**
     * This method will be called by Forge when it is time for the mod to register its Items.
     * This method will always be called after the Block registry method.
     */
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                // This is a very simple Item. It has no special properties except for being on our creative tab.
                setup(new Item(new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP)), "acorn")
        );

        // We need to go over the entire registry so that we include any potential Registry Overrides
        for (final Block block : ForgeRegistries.BLOCKS.getValues()) {

            final ResourceLocation blockRegistryName = block.getRegistryName();
            // An extra safe-guard against badly registered blocks
            Preconditions.checkNotNull(blockRegistryName, "Registry Name of Block \"" + block + "\" is null! This is not allowed!");

            // Check that the blocks is from our mod, if not, continue to the next block
            if (!blockRegistryName.getNamespace().equals(Main.MODID)) {
                continue;
            }

            // If you have blocks that don't have a corresponding BlockItem, uncomment this code and create
            // an Interface - or even better an Annotation - called NoAutomaticBlockItem with no methods
            // and implement it on your blocks that shouldn't have BlockItems autmatically made for them
//			if (block instanceof NoAutomaticBlockItem) {
//				continue;
//			}

            // Make the properties, and make it so that the item will be on our ItemGroup (CreativeTab)
            final Item.Properties properties = new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP);
            // Create the new BlockItem with the block and it's properties
            final BlockItem blockItem = new BlockItem(block, properties);
            // Setup the new BlockItem with the block's registry name and register it
            registry.register(setup(blockItem, blockRegistryName));
        }
        LOGGER.debug("Registered Items");
    }

    /**
     * This method will be called by Forge when it is time for the mod to register its TileEntityType.
     * This method will always be called after the Block and Item registry methods.
     */
    @SubscribeEvent
    public static void onRegisterTileEntityTypes(@Nonnull final RegistryEvent.Register<TileEntityType<?>> event) {
        // Register your TileEntityTypes here if you have them
        event.getRegistry().registerAll(
                // We don't have a datafixer for our TileEntity, so we pass null into build
                setup(TileEntityType.Builder.create(MiniModelTileEntity::new, ModBlocks.MINI_MODEL).build(null), "mini_model")
        );
        LOGGER.debug("Registered TileEntityTypes");
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            ConfigHelper.bakeClient(config);
            LOGGER.debug("Baked client config");
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            ConfigHelper.bakeServer(config);
            LOGGER.debug("Baked server config");
        }
    }

    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) {
        Preconditions.checkNotNull(name, "Name to assign to entry cannot be null!");
        return setup(entry, new ResourceLocation(Main.MODID, name));
    }

    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName) {
        Preconditions.checkNotNull(entry, "Entry cannot be null!");
        Preconditions.checkNotNull(registryName, "Registry name to assign to entry cannot be null!");
        entry.setRegistryName(registryName);
        return entry;
    }

}