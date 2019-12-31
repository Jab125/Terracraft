package com.github.SimplyCmd.terracraft;

import static com.github.SimplyCmd.terracraft.Main.MOD_ID;

import com.github.SimplyCmd.terracraft.lists.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The main class of the mod, this is the class that looks like a mod to forge.
 */
@Mod(MOD_ID)
public class Main {
    /**
     * The modid of this mod, this has to match the modid in the mods.toml and has to be in the format defined in {@link net.minecraftforge.fml.loading.moddiscovery.ModInfo}
     */
    public static final String MOD_ID = "terracraft";
    public static Main instance;
    private static final Logger logger = LogManager.getLogger(MOD_ID);

    public static final ItemGroup TERRACRAFT = new TerracraftItemGroup();

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        logger.info("Setup method registered");
    }
    private void clientRegistries(final FMLClientSetupEvent event) {
        logger.info("clientRegistries method registered");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent //run on launch
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    ItemList.testing_dust = new Item(new Item.Properties().group(ItemGroup.MISC)).setRegistryName(location("testing_dust")),
                    ItemList.copper_bar = new Item(new Item.Properties().group(TERRACRAFT)).setRegistryName(location("copper_bar")),

                    ItemList.iron_hammer = new AxeItem(ToolMaterialList.iron_hammer_mat, -1.0f, 6.0f, new Item.Properties().group(TERRACRAFT)).setRegistryName(location("iron_hammer")),
                    ItemList.iron_shortsword = new SwordItem(ToolMaterialList.iron_shortsword_mat, 1, 6.0f, new Item.Properties().group(TERRACRAFT)).setRegistryName(location("iron_shortsword")),
                    ItemList.iron_broadsword = new SwordItem(ToolMaterialList.iron_broadsword_mat, 1,1.0f, new Item.Properties().group(TERRACRAFT)).setRegistryName(location("iron_broadsword")),

                    ItemList.angel_statue = new BlockItem(BlockList.angel_statue, new Item.Properties().group(TERRACRAFT)).setRegistryName(BlockList.angel_statue.getRegistryName()),
                    ItemList.copper_ore = new BlockItem(BlockList.copper_ore, new Item.Properties().group(TERRACRAFT)).setRegistryName(BlockList.copper_ore.getRegistryName()),
                    ItemList.silver_ore = new BlockItem(BlockList.silver_ore, new Item.Properties().group(TERRACRAFT)).setRegistryName(BlockList.silver_ore.getRegistryName())
                    );
            logger.info("Items registered");
        }

        @SubscribeEvent //run on launch
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    BlockList.angel_statue = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 30.0f).lightValue(0).sound(SoundType.STONE)).setRegistryName(location("angel_statue")), //lightValue = how much it glows, Sound = breaking sound
                    BlockList.copper_ore = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 30.0f).lightValue(0).sound(SoundType.STONE)).setRegistryName(location("copper_ore")), //lightValue = how much it glows, Sound = breaking sound
                    BlockList.silver_ore = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 30.0f).lightValue(0).sound(SoundType.STONE)).setRegistryName(location("silver_ore")) //lightValue = how much it glows, Sound = breaking sound
            );
            logger.info("Blocks registered");
        }

        private static ResourceLocation location(String name) {
            return new ResourceLocation(MOD_ID, name);
        }
    }
}