package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.DRAGON_PEDESTAL)
                .add(ModBlocks.ETHER_LEVER)
                .add(ModBlocks.GILDED_PLATE)
                .add(ModBlocks.TOTEM_OF_CHAOS)
                .add(ModBlocks.TOTEM_OF_ORDER)
                .add(ModBlocks.NENDER_BRICK)
                .add(ModBlocks.CHACHAPOYAN_IDOL)
                .add(ModBlocks.COPPER_WIRE);

        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.NENDER_BRICK)
                .add(ModBlocks.ETHER_LEVER)
                .add(ModBlocks.COPPER_WIRE)
                .add(ModBlocks.GILDED_PLATE)
                .add(ModBlocks.TOTEM_OF_ORDER)
                .add(ModBlocks.TOTEM_OF_CHAOS);

        valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.CHACHAPOYAN_IDOL)
                .add(ModBlocks.DRAGON_PEDESTAL);
    }
}
