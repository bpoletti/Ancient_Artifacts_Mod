package net.eagle.ancientartifacts.block.entity;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<DragonPedestalEntity> DRAGON_PEDESTAL_ENTITY;

    public static void registerBlockEntities(){
        DRAGON_PEDESTAL_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(AncientArtifacts.MOD_ID, "dragon_pedestal"),
                FabricBlockEntityTypeBuilder.create(DragonPedestalEntity::new,
                        ModBlocks.DRAGON_PEDESTAL).build(null));

    }
}

