package net.eagle.ancientartifacts.block;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {

    public static final Block NENDER_BRICK = registerBlock("nender_brick", Block::new,
            BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().sound(SoundType.NETHER_BRICKS));

    public static final Block CHACHAPOYAN_IDOL = registerBlock("chachapoyan_idol", ChachapoyanIdol::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).lightLevel(state -> 8).strength(3f).requiresCorrectToolForDrops().noOcclusion());

    public static final Block GILDED_PLATE = registerBlock("gilded_plate", GildedPlate::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).strength(1f).requiresCorrectToolForDrops());

    public static final Block DRAGON_PEDESTAL = registerBlock("dragon_pedestal", DragonPedestal::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK).strength(2f).requiresCorrectToolForDrops());

    public static final Block ETHER_LEVER = registerBlock("ether_lever", EtherLever::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.LEVER).strength(1f).requiresCorrectToolForDrops());

    public static final Block TOTEM_OF_ORDER = registerBlock("totem_of_order", TotemOrder::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1f).requiresCorrectToolForDrops());

    public static final Block TOTEM_OF_CHAOS = registerBlock("totem_of_chaos", TotemChaos::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1f).requiresCorrectToolForDrops());

    public static final Block COPPER_WIRE = registerBlock("copper_wire", CopperWire::new,
            BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(CopperWire.IS_POWERED) ? 8 : 0));

    // Overloaded 3-parameter helper method to securely pass the properties into the block factory
    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Identifier id = Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, name);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);

        Block toRegister = factory.apply(properties.setId(key));
        registerBlockItem(name, toRegister);
        return Registry.register(BuiltInRegistries.BLOCK, id, toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Identifier id = Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, name);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

        Registry.register(BuiltInRegistries.ITEM, id,
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemKey)));
    }

    public static void registerModBlocks() {
        AncientArtifacts.LOGGER.info("Registering ModBlocks for " + AncientArtifacts.MOD_ID);
    }
}