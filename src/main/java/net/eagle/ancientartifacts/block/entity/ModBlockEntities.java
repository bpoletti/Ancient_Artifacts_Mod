package net.eagle.ancientartifacts.block.entity;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static BlockEntityType<DragonPedestalEntity> DRAGON_PEDESTAL_ENTITY;

    public static void registerBlockEntities(){
        DRAGON_PEDESTAL_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, "dragon_pedestal"),
                FabricBlockEntityTypeBuilder.create(DragonPedestalEntity::new, ModBlocks.DRAGON_PEDESTAL).build()
        );
    }
}