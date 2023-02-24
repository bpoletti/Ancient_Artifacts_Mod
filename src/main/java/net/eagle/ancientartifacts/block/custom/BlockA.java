package net.eagle.ancientartifacts.block.custom;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockA extends Block {

    public static final BooleanProperty BLOCK_A_V2 = BooleanProperty.of("block_a_v2");

//    public static final BooleanProperty IS_V2 = BooleanProperty.of("is_v2");

//    public static final DirectionProperty FACING;

    // .with(IS_V2, false)
    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_V2;

    public BlockA(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(BLOCK_A_V2, false));
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(BLOCK_A_V2) ? SHAPE_V2 : SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState();
    }

//    @Override
//    public BlockState rotate(BlockState state, BlockRotation rotation) {
//        return state.with(FACING, rotation.rotate(state.get(FACING)));
//    }
//
//    @Override
//    public BlockState mirror(BlockState state, BlockMirror mirror) {
//        return state.rotate(mirror.getRotation(state.get(FACING)));
//    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BLOCK_A_V2);
    }

    static {
//        FACING = HorizontalFacingBlock.FACING;
        SHAPE = Block.createCuboidShape(3, 0, 3, 13, 2, 13);
        SHAPE_V2 = Block.createCuboidShape(3, 0, 3, 13, 10, 13);
    }
}
