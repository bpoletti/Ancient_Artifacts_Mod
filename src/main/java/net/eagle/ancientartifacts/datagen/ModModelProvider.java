package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerators) {
        blockStateModelGenerators.createTrivialCube(ModBlocks.NENDER_BRICK);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.ANKH_PENDANT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.TOTEM_OF_ORDER.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.TOTEM_OF_CHAOS.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.ETHER_LEVER.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.GILDED_PLATE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.DRAGON_PEDESTAL.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.BLACK_ICE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ELDER_GUARDIAN_SCALES, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDER_ROD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.EVOKER_KEY, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FIREFLY_ORB, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.MYCELIUM_DUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.NETHER_GRASS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.OCHRE_FIREFLY_BUD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ORB_INFINIUM, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.PEARLESCENT_FIREFLY_BUD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.RED_ICE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.VERDANT_FIREFLY_BUD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WARDEN_HEART, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.itemModelOutput.accept(
                ModItems.END_STAFF,
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ModItems.END_STAFF))
        );
        itemModelGenerators.itemModelOutput.accept(
                ModItems.DRAGON_FOSSIL,
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ModItems.DRAGON_FOSSIL))
        );

        itemModelGenerators.itemModelOutput.accept(
                ModBlocks.CHACHAPOYAN_IDOL.asItem(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ModBlocks.CHACHAPOYAN_IDOL))
        );
        itemModelGenerators.itemModelOutput.accept(
                ModBlocks.COPPER_WIRE.asItem(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ModBlocks.COPPER_WIRE))
        );
    }
}