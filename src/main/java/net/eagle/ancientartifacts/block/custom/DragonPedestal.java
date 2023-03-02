package net.eagle.ancientartifacts.block.custom;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.entity.DragonPedestalEntity;
import net.eagle.ancientartifacts.block.entity.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class DragonPedestal extends BlockWithEntity implements BlockEntityProvider {

    public static final BooleanProperty GILDED = BooleanProperty.of("gilded");
//    public static final BooleanProperty FOSSIL_HEAD = BooleanProperty.of("fossil_head");
//    public static final BooleanProperty HEART_SEA = BooleanProperty.of("heart_sea");
//    public static final BooleanProperty ORB_INFINIUM = BooleanProperty.of("orb_of_infinium");
    public final static DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape SHAPE_UPPER;
    protected static final VoxelShape SHAPE_LOWER;
    protected static final VoxelShape SHAPE_LOWER_G;

    static {
        //TOP
        VoxelShape su1 = Block.createCuboidShape(5.5,0,5.5,10.5,1,10.5);
        VoxelShape su2 = Block.createCuboidShape(3.5,1,3.5,12.5,4,12.5);
        VoxelShape su3 = Block.createCuboidShape(3.5,3,3.25,12.5,6,12.75);

        //BOTTOM
        VoxelShape sl1_g = Block.createCuboidShape(1, 0, 1, 15, 1, 15);
        VoxelShape sl1 = Block.createCuboidShape(2,0,2,14,1,14);
        VoxelShape sl2 = Block.createCuboidShape(3,1,3,13,3,13);
        VoxelShape sl3 = Block.createCuboidShape(5.5,3,5.5,10.5,12,10.5);

        SHAPE_UPPER = VoxelShapes.union(su1, su2, su3).simplify();
        SHAPE_LOWER = VoxelShapes.union(sl1, sl2, sl3).simplify();
        SHAPE_LOWER_G = VoxelShapes.union(sl1_g, sl2, sl3).simplify();
    }
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;


    public DragonPedestal(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(FACING, Direction.NORTH)
                .with(GILDED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? (state.get(GILDED) ? SHAPE_LOWER_G : SHAPE_LOWER) : SHAPE_UPPER;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {

        super.onBlockAdded(state, world, pos, oldState, notify);
        if(!world.isClient){
            Block down = world.getBlockState(pos.down()).getBlock();
            if (down == ModBlocks.GILDED_PLATE && !state.get(DragonPedestal.GILDED)) {
                world.removeBlock(pos, false);
                world.setBlockState(pos.down(), state.with(HALF, DoubleBlockHalf.LOWER)
                        .with(GILDED,true), 3);
                world.setBlockState(pos, state.with(HALF, DoubleBlockHalf.UPPER));
            }
        }

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {

        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            super.onPlaced(world, pos, state, placer, itemStack);
            return;
        }
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        var world = ctx.getWorld();

        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return this.getDefaultState()
                    .with(FACING, ctx.getPlayerFacing().getOpposite())
                    .with(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, GILDED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof DragonPedestalEntity) {
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.UPPER ? null : new DragonPedestalEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos topPos;
        BlockPos botPos;
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            topPos = pos;
            botPos = pos.down();
        } else {
            topPos = pos.up();
            botPos = pos;
        }
        world.removeBlock(topPos, false);
        world.removeBlock(botPos, false);
        world.updateNeighbors(topPos, Blocks.AIR);

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.DRAGON_PEDESTAL_ENTITY, DragonPedestalEntity::tick);
    }
}
