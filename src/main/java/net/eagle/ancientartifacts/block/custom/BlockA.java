package net.eagle.ancientartifacts.block.custom;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockA extends Block {

    public static final BooleanProperty BLOCK_A_V2 = BooleanProperty.of("block_a_v2");
    public static final BooleanProperty BLOCK_A_V3 = BooleanProperty.of("block_a_v3");
    public static final DirectionProperty FACING;
    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_V2;
    private static final VoxelShape SHAPE_V3;

    public BlockA(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(BLOCK_A_V2, false).with(BLOCK_A_V3,false));
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(BlockA.BLOCK_A_V2) ? (state.get(BlockA.BLOCK_A_V3) ? SHAPE_V3 : SHAPE_V2) : SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, BLOCK_A_V2, BLOCK_A_V3);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        SHAPE = Block.createCuboidShape(3, 0, 3, 13, 2, 13);
        SHAPE_V2 = Block.createCuboidShape(3, 0, 3, 13, 10, 13);
        SHAPE_V3 = Block.createCuboidShape(2, 0, 1, 14, 10, 15);
    }
}
