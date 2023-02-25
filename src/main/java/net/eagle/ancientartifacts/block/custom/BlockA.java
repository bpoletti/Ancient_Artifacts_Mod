package net.eagle.ancientartifacts.block.custom;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockA extends Block {

    public static final BooleanProperty BLOCK_A_V2 = BooleanProperty.of("block_a_v2");
    public static final BooleanProperty BLOCK_A_V3 = BooleanProperty.of("block_a_v3");
    public static final BooleanProperty BLOCK_A_V4 = BooleanProperty.of("block_a_v4");
    public static final DirectionProperty FACING;
    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_V2;
    private static final VoxelShape SHAPE_V3;
    private static final VoxelShape SHAPE_V4;

    public BlockA(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(BLOCK_A_V2, false)
                .with(BLOCK_A_V3,false)
                .with(BLOCK_A_V4, false));
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(BlockA.BLOCK_A_V2) ? (state.get(BlockA.BLOCK_A_V3) ? state.get(BlockA.BLOCK_A_V4) ? SHAPE_V4 : SHAPE_V3 : SHAPE_V2) : SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getStackInHand(hand).getItem() == Items.STICK){
            if (state.isOf(ModBlocks.BLOCK_A) && !(Boolean) state.get(BlockA.BLOCK_A_V4) && state.get(BlockA.BLOCK_A_V3)) {
                if (world.isClient) {
                    return ActionResult.SUCCESS;
                } else {
                    BlockState blockState2 = state.with(BlockA.BLOCK_A_V4, true);
                    world.setBlockState(pos, blockState2);
                    return ActionResult.CONSUME;
                }
            } else {
                return ActionResult.PASS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
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
        builder.add(FACING, BLOCK_A_V2, BLOCK_A_V3, BLOCK_A_V4);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        SHAPE = Block.createCuboidShape(3, 0, 3, 13, 2, 13);
        SHAPE_V2 = Block.createCuboidShape(3, 0, 3, 13, 10, 13);
        SHAPE_V3 = Block.createCuboidShape(2, 0, 1, 14, 10, 15);
        SHAPE_V4 = Block.createCuboidShape(2, 0, 1, 14, 11, 15);
    }
}
