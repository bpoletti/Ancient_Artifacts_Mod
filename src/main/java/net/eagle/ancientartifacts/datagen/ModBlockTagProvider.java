package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.CHACHAPOYAN_IDOL)
                .add(ModBlocks.COPPER_WIRE)
                .add(ModBlocks.DRAGON_PEDESTAL)
                .add(ModBlocks.ETHER_LEVER)
                .add(ModBlocks.GILDED_PLATE)
                .add(ModBlocks.NENDER_BRICK)
                .add(ModBlocks.TOTEM_OF_CHAOS)
                .add(ModBlocks.TOTEM_OF_ORDER);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.NENDER_BRICK)
                .add(ModBlocks.ETHER_LEVER)
                .add(ModBlocks.COPPER_WIRE)
                .add(ModBlocks.GILDED_PLATE)
                .add(ModBlocks.TOTEM_OF_ORDER)
                .add(ModBlocks.TOTEM_OF_CHAOS);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.CHACHAPOYAN_IDOL)
                .add(ModBlocks.DRAGON_PEDESTAL);
    }
}
