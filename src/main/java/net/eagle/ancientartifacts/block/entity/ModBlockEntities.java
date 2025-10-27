package net.eagle.ancientartifacts.block.entity;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<DragonPedestalEntity> DRAGON_PEDESTAL_ENTITY;

    public static void registerBlockEntities(){
        DRAGON_PEDESTAL_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(AncientArtifacts.MOD_ID, "dragon_pedestal"),
                FabricBlockEntityTypeBuilder.create(DragonPedestalEntity::new,
                        ModBlocks.DRAGON_PEDESTAL).build(null));

    }
}

