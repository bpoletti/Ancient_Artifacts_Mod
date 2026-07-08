package net.eagle.ancientartifacts.block;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.custom.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

    public static final Block NENDER_BRICK = registerBlock("nender_brick", Block::new,
            AbstractBlock.Settings.copy(Blocks.NETHER_BRICKS).strength(2f).requiresTool());

    public static final Block CHACHAPOYAN_IDOL = registerBlock("chachapoyan_idol", ChachapoyanIdol::new,
            AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).luminance(state -> 8).strength(3f).requiresTool().nonOpaque());

    public static final Block GILDED_PLATE = registerBlock("gilded_plate", GildedPlate::new,
            AbstractBlock.Settings.copy(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).strength(1f).requiresTool());

    public static final Block DRAGON_PEDESTAL = registerBlock("dragon_pedestal", DragonPedestal::new,
            AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK).strength(2f).requiresTool());

    public static final Block ETHER_LEVER = registerBlock("ether_lever", EtherLever::new,
            AbstractBlock.Settings.copy(Blocks.LEVER).strength(1f).requiresTool());

    public static final Block TOTEM_OF_ORDER = registerBlock("totem_of_order", TotemOrder::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1f).requiresTool());

    public static final Block TOTEM_OF_CHAOS = registerBlock("totem_of_chaos", TotemChaos::new,
            AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(1f).requiresTool());

    public static final Block COPPER_WIRE = registerBlock("copper_wire", CopperWire::new,
            AbstractBlock.Settings.create().strength(1f).requiresTool()
                    .luminance(state -> state.get(CopperWire.IS_POWERED) ? 8 : 0));


    // The Updated Helper Method
    private static <T extends Block> T registerBlock(String name, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(AncientArtifacts.MOD_ID, name));

        T block = factory.apply(settings.registryKey(blockKey));

        registerBlockItem(name, block);

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static Item registerBlockItem(String name, Block block) {
        // Generate the Item RegistryKey for the BlockItem
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AncientArtifacts.MOD_ID, name));

        return Registry.register(Registries.ITEM, itemKey,
                new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey()));
    }

    public static void registerModBlocks() {
        AncientArtifacts.LOGGER.debug("Registering ModBlocks for {}", AncientArtifacts.MOD_ID);
    }
}