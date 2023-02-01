package net.eagle.ancientartifacts.block;

import net.eagle.ancientartifacts.AncientArtifacts;
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
    new Block(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()), ItemGroup.BUILDING_BLOCKS);


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
