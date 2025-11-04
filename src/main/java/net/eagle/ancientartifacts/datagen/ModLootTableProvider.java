package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.CHACHAPOYAN_IDOL);
        addDrop(ModBlocks.COPPER_WIRE);
        addDrop(ModBlocks.DRAGON_PEDESTAL);
        addDrop(ModBlocks.ETHER_LEVER);
        addDrop(ModBlocks.GILDED_PLATE);
        addDrop(ModBlocks.NENDER_BRICK);
        addDrop(ModBlocks.TOTEM_OF_ORDER);
        addDrop(ModBlocks.TOTEM_OF_CHAOS);
    }
}
