package net.eagle.ancientartifacts.block;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    
    public static final Block NENDER_BRICK = registerBlock("nender_brick", 
    new Block(FabricBlockSettings.copyOf(Blocks.NETHER_BRICKS).strength(4f).requiresTool()));

    public static final Block CHACHAPOYAN_IDOL = registerBlock("chachapoyan_idol",
    new ChachapoyanIdol(FabricBlockSettings.copyOf(Blocks.GOLD_BLOCK).luminance(8).strength(4f).
            requiresTool().nonOpaque()));

    public static final Block GILDED_PLATE = registerBlock("gilded_plate",
            new GildedPlate(FabricBlockSettings.copyOf(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).strength(4f).requiresTool()));

    public static final Block DRAGON_PEDESTAL = registerBlock("dragon_pedestal",
            new DragonPedestal(FabricBlockSettings.copyOf(Blocks.PURPUR_BLOCK).strength(4f).requiresTool()));

    public static final Block ETHER_LEVER = registerBlock("ether_lever",
            new EtherLever(FabricBlockSettings.copyOf(Blocks.LEVER).strength(4f).requiresTool()));
    public static final Block TOTEM_OF_ORDER = registerBlock("totem_of_order",
            new TotemOrder(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(4f).requiresTool()));
    public static final Block TOTEM_OF_CHAOS = registerBlock("totem_of_chaos",
            new TotemChaos(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(4f).requiresTool()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(AncientArtifacts.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        Item item = Registry.register(Registries.ITEM, new Identifier(AncientArtifacts.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));

        return item;
    }

    public static void registerModBlocks() {
        AncientArtifacts.LOGGER.debug("Registering ModBlocks for " + AncientArtifacts.MOD_ID);
    }

}
