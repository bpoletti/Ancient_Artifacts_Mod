package net.eagle.ancientartifacts.block.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DragonPedestalEntity extends BlockEntity {

    public DragonPedestalEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRAGON_PEDESTAL_ENTITY, pos, state);
    }

    public static void tick(Level world, BlockPos blockPos, BlockState blockState, DragonPedestalEntity entity) {

    }
}
