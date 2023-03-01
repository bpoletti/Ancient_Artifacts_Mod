package net.eagle.ancientartifacts.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DragonPedestalEntity extends BlockEntity {

    public DragonPedestalEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRAGON_PEDESTAL_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, DragonPedestalEntity entity) {

    }
}
