package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;


public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.NENDER_BRICK);
        blockStateModelGenerator.registerParentedItemModel(ModBlocks.CHACHAPOYAN_IDOL, Identifier.of(AncientArtifacts.MOD_ID, "block/chachapoyan_idol"));
        blockStateModelGenerator.registerParentedItemModel(ModBlocks.COPPER_WIRE, Identifier.of(AncientArtifacts.MOD_ID, "block/copper_wire"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ANKH_PENDANT, Models.GENERATED);
        itemModelGenerator.register(ModBlocks.TOTEM_OF_ORDER.asItem(), Models.GENERATED);
        itemModelGenerator.register(ModBlocks.TOTEM_OF_CHAOS.asItem(), Models.GENERATED);
        itemModelGenerator.register(ModBlocks.ETHER_LEVER.asItem(), Models.GENERATED);
        itemModelGenerator.register(ModBlocks.GILDED_PLATE.asItem(), Models.GENERATED);
        itemModelGenerator.register(ModBlocks.DRAGON_PEDESTAL.asItem(), Models.GENERATED);
        itemModelGenerator.register(ModItems.BLACK_ICE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ELDER_GUARDIAN_SCALES, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_ROD, Models.GENERATED);
        itemModelGenerator.register(ModItems.EVOKER_KEY, Models.GENERATED);
        itemModelGenerator.register(ModItems.FIREFLY_ORB, Models.GENERATED);
        itemModelGenerator.register(ModItems.MYCELIUM_DUST, Models.GENERATED);
        itemModelGenerator.register(ModItems.NETHER_GRASS, Models.GENERATED);
        itemModelGenerator.register(ModItems.OCHRE_FIREFLY_BUD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ORB_INFINIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.PEARLESCENT_FIREFLY_BUD, Models.GENERATED);
        itemModelGenerator.register(ModItems.RED_ICE, Models.GENERATED);
        itemModelGenerator.register(ModItems.VERDANT_FIREFLY_BUD, Models.GENERATED);
        itemModelGenerator.register(ModItems.WARDEN_HEART, Models.GENERATED);
        itemModelGenerator.output.accept(
                ModItems.END_STAFF,
                ItemModels.basic(ModelIds.getItemModelId(ModItems.END_STAFF))
        );itemModelGenerator.output.accept(
                ModItems.DRAGON_FOSSIL,
                ItemModels.basic(ModelIds.getItemModelId(ModItems.DRAGON_FOSSIL))
        );
    }
}
