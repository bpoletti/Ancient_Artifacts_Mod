package net.eagle.ancientartifacts.block;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.custom.*;
import net.eagle.ancientartifacts.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    
    public static final Block NENDER_BRICK = registerBlock("nender_brick", 
    new Block(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);

    public static final Block CHACHAPOYAN_IDOL = registerBlock("chachapoyan_idol",
    new ChachapoyanIdol(FabricBlockSettings.of(Material.METAL).luminance(8).strength(4f).
            requiresTool().nonOpaque()), ModItemGroup.ARTIFACTS);

    public static final Block GILDED_PLATE = registerBlock("gilded_plate",
            new GildedPlate(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);

    public static final Block DRAGON_PEDESTAL = registerBlock("dragon_pedestal",
            new DragonPedestal(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);

    public static final Block ETHER_LEVER = registerBlock("ether_lever",
            new EtherLever(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);
    public static final Block TOTEM_OF_ORDER = registerBlock("totem_of_order",
            new TotemOrder(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);
    public static final Block TOTEM_OF_CHAOS = registerBlock("totem_of_chaos",
            new TotemChaos(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);

    public static final Block COPPER_WIRE = registerBlock("copper_wire",
            new CopperWire(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool()), ModItemGroup.ARTIFACTS);

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(AncientArtifacts.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {

        return Registry.register(Registry.ITEM, new Identifier(AncientArtifacts.MOD_ID, name),
        new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        AncientArtifacts.LOGGER.debug("Registering ModBlocks for " + AncientArtifacts.MOD_ID);
    }

}
