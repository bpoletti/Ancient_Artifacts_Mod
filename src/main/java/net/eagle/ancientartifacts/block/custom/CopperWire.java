package net.eagle.ancientartifacts.block.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class CopperWire extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WIRE_CONNECTION_NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty WIRE_CONNECTION_SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WIRE_CONNECTION_EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WIRE_CONNECTION_WEST = BlockStateProperties.WEST;
    public static final BooleanProperty WIRE_CONNECTION_UP = BlockStateProperties.UP;
    public static final BooleanProperty WIRE_CONNECTION_DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty IS_ROOT;
    public static final BooleanProperty IS_POWERED;
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
    protected static final VoxelShape ROD_X_SHAPE;
    protected static final VoxelShape ROD_Y_SHAPE;
    protected static final VoxelShape ROD_Z_SHAPE;
    private static final Map<Direction, VoxelShape> DIRECTION_TO_SIDE_SHAPE;

    private static final Map<BlockState, VoxelShape> SHAPES = Maps.newHashMap();
    public static final Map<Direction, BooleanProperty> DIRECTION_TO_WIRE_CONNECTION_PROPERTY = Maps.newHashMap(ImmutableMap.of(Direction.NORTH, WIRE_CONNECTION_NORTH, Direction.EAST, WIRE_CONNECTION_EAST, Direction.SOUTH, WIRE_CONNECTION_SOUTH, Direction.WEST, WIRE_CONNECTION_WEST, Direction.UP, WIRE_CONNECTION_UP, Direction.DOWN, WIRE_CONNECTION_DOWN));

    public CopperWire(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_ROOT, false).setValue(IS_POWERED, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.DOWN).setValue(WIRE_CONNECTION_DOWN, false).setValue(WIRE_CONNECTION_UP, false).setValue(WIRE_CONNECTION_NORTH, false).setValue(WIRE_CONNECTION_SOUTH,false).setValue(WIRE_CONNECTION_EAST,false).setValue(WIRE_CONNECTION_WEST,false).setValue(POWER,0));
        for (BlockState blockState : this.getStateDefinition().getPossibleStates()) {
            if (blockState.getValue(POWER) != 0) continue;
            SHAPES.put(blockState, this.getShapeForState(blockState));
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.setValue(POWER, 0).setValue(IS_POWERED, false));
    }

    private VoxelShape getShapeForState(BlockState state) {
        VoxelShape shape = getBaseShape(state);
        for (Direction direction : Direction.values()) {
            if (state.getValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction))) {
                VoxelShape tempShape = DIRECTION_TO_SIDE_SHAPE.get(direction);
                shape = Shapes.or(tempShape, shape);
            }
        }
        return shape;
    }

    public VoxelShape getBaseShape(BlockState state) {
        switch (state.getValue(FACING).getAxis()) {
            default: {
                return ROD_X_SHAPE;
            }
            case Z: {
                return ROD_Z_SHAPE;
            }
            case Y:
        }
        return ROD_Y_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean waterlogged = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return this.getPlacementState(ctx.getLevel(), this.defaultBlockState(), ctx.getClickedPos())
                .setValue(FACING, ctx.getClickedFace()).setValue(WATERLOGGED, waterlogged);
    }

    private BlockState getPlacementState(Level level, BlockState state, BlockPos pos){
        int strongestPower = 0;
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if(neighborState.getBlock() == Blocks.COPPER_BLOCK || neighborState.getBlock() == Blocks.WAXED_COPPER_BLOCK){
                BlockPos leverPos = findEtherLever(level, neighborPos);
                BlockState leverPowerState = level.getBlockState(leverPos);
                if(leverPos != pos && leverPowerState.getBlock() instanceof EtherLever){
                    state = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true).setValue(IS_ROOT, true).setValue(POWER, level.getBestNeighborSignal(neighborPos));
                } else {
                    state = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true).setValue(IS_ROOT, false);
                }
            }
            else if (neighborState.getBlock() instanceof CopperWire) {
                state = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true);
                level.setBlock(neighborPos, neighborState.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()), true), 3);
            } else {
                if(neighborState.isSolidRender()){
                    state = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true);
                }
            }
        }
        strongestPower = getStrongestRestonePower(level, pos);
        if(strongestPower == 0){
            state = state.setValue(POWER, strongestPower);
        } else{
            state = state.setValue(POWER, strongestPower - 1);
            level.levelEvent(LevelEvent.PARTICLES_ELECTRIC_SPARK, pos, state.getValue(FACING).getAxis().ordinal());
        }
        if(state.getValue(POWER) > 0){
            state = state.setValue(IS_POWERED, true);
        } else {
            state = state.setValue(IS_POWERED, false);
        }
        return state;
    }

    @Override
    public @NonNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof CopperWire) {
                if(state.getValue(IS_ROOT)){
                    BlockState updatedNeighborState = neighborState.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()), false).setValue(POWER, 0).setValue(IS_POWERED, false);
                    level.setBlock(neighborPos, updatedNeighborState, 3);
                } else{
                    BlockState updatedNeighborState = neighborState.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()), false);
                    level.setBlock(neighborPos, updatedNeighborState, 3);
                }
                this.updateNeighbors(level, pos);
            }
        }
        return state;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.is(state.getBlock()) || level.isClientSide()) {
            return;
        }
        this.updateNeighbors(level, pos);

        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), this);
        }

        this.updateOffsetNeighbors(level, pos);
    }

    private void updateOffsetNeighbors(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            this.updateNeighbors(level, pos.relative(direction));
        }
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.relative(direction);
            if (level.getBlockState(blockPos).isSolidRender()) {
                this.updateNeighbors(level, blockPos.above());
                continue;
            }
            this.updateNeighbors(level, blockPos.below());
        }
    }

    private void updateNeighbors(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), this);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean isMoving) {
        super.neighborChanged(state, level, pos, sourceBlock, wireOrientation, isMoving);

        BlockPos fromPos = null;
        for (Direction dir : Direction.values()) {
            BlockPos offsetPos = pos.relative(dir);
            if (level.getBlockState(offsetPos).getBlock() == sourceBlock) {
                fromPos = offsetPos;
                break;
            }
        }

        if (fromPos == null) return;

        int netPower = 0;
        BlockState copperPowerState = level.getBlockState(fromPos);
        BlockState fromState = level.getBlockState(fromPos);

        if(copperPowerState.getBlock() == Blocks.COPPER_BLOCK || copperPowerState.getBlock() == Blocks.WAXED_COPPER_BLOCK) {
            BlockPos leverPos = findEtherLever(level, fromPos);
            BlockState leverState = level.getBlockState(leverPos);
            if(leverState.getBlock() instanceof EtherLever){
                netPower = level.getBestNeighborSignal(fromPos);
                state = state.setValue(POWER, netPower).setValue(IS_ROOT, true);
                if(state.getValue(POWER) > 0){
                    state = state.setValue(IS_POWERED, true);
                    level.levelEvent(LevelEvent.PARTICLES_ELECTRIC_SPARK, pos, state.getValue(FACING).getAxis().ordinal());
                } else {
                    state = state.setValue(IS_POWERED, false);
                }
                level.setBlock(pos, state, 3);
                return;
            } else {
                state = state.setValue(POWER, netPower).setValue(IS_ROOT, false);
                if(state.getValue(POWER) > 0){
                    state = state.setValue(IS_POWERED, true);
                } else {
                    state = state.setValue(IS_POWERED, false);
                }
                level.setBlock(pos, state, 3);
                return;
            }
        } else if(fromState.getBlock() instanceof CopperWire){
            if(!state.getValue(IS_ROOT)){
                int strongestPower = getStrongestRestonePower(level, pos);
                for (Direction direction : Direction.values()) {
                    BlockPos neighborPos = pos.relative(direction);
                    BlockState neighborState = level.getBlockState(neighborPos);
                    if(neighborState.getBlock() instanceof CopperWire){
                        int powerRecieved = level.getSignal(pos.relative(direction), direction);
                        strongestPower = Math.max(strongestPower, powerRecieved);
                    }
                }
                if(strongestPower == 0){
                    state = state.setValue(POWER, strongestPower);
                } else{
                    state = state.setValue(POWER, strongestPower - 1);
                }
            }
        }

        if(state.getValue(POWER) > 0){
            state = state.setValue(IS_POWERED, true);
        } else {
            state = state.setValue(IS_POWERED, false);
        }

        if(state.getValue(IS_ROOT)){
            boolean hasCopperBlockNeighbor = false;
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                if (level.getBlockState(neighborPos).getBlock() == Blocks.COPPER_BLOCK || level.getBlockState(neighborPos).getBlock() == Blocks.WAXED_COPPER_BLOCK) {
                    hasCopperBlockNeighbor = true;
                    break;
                }
            }
            if (!hasCopperBlockNeighbor) {
                state = state.setValue(POWER, 0).setValue(IS_POWERED, false).setValue(IS_ROOT, false);
            }
        } else {
            boolean hasCopperWireNeighbors = false;
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                if (level.getBlockState(neighborPos).getBlock() instanceof CopperWire) {
                    hasCopperWireNeighbors = true;
                    break;
                }
            }

            if (!hasCopperWireNeighbors) {
                state = state.setValue(POWER, 0).setValue(IS_POWERED, false);
            }
        }
        level.setBlock(pos, state, 3);
    }

    private int getStrongestRestonePower(Level level, BlockPos pos){
        int strongestPower = 0;
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if(neighborState.getBlock() instanceof CopperWire){
                int powerReceived = neighborState.getValue(POWER);
                strongestPower = Math.max(strongestPower, powerReceived);
            }
        }
        return strongestPower;
    }

    private BlockPos findEtherLever(Level level, BlockPos pos){
        for (Direction direction : Direction.values()) {
            BlockPos leverPos = pos.relative(direction);
            BlockState leverState = level.getBlockState(leverPos);
            if(leverState.getBlock() instanceof EtherLever){
                return leverPos;
            }
        }
        return pos;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if(state.getValue(POWER) == 0){
            return 0;
        }
        if (state.getValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction))) {
            return state.getValue(POWER);
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getSignal(level, pos, direction);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_ROOT, WATERLOGGED, IS_POWERED, POWER, FACING, WIRE_CONNECTION_NORTH, WIRE_CONNECTION_SOUTH, WIRE_CONNECTION_EAST, WIRE_CONNECTION_WEST, WIRE_CONNECTION_UP, WIRE_CONNECTION_DOWN);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        IS_ROOT = BooleanProperty.create("is_root");
        IS_POWERED = BooleanProperty.create("is_powered");
        VoxelShape vx1 = Block.box(0,6.3,6.3,2.5,9.7,9.7);
        VoxelShape vx2 = Block.box(2.5,7,7,13.5,9,9);
        VoxelShape vx3 = Block.box(13.5,6.3,6.3,16,9.7,9.7);

        ROD_X_SHAPE = Shapes.or(vx1, vx2, vx3).optimize();

        VoxelShape vy1 = Block.box(6.3,0,6.3,9.7,2.5,9.7);
        VoxelShape vy2 = Block.box(7,2.5,7,9,13.5,9);
        VoxelShape vy3 = Block.box(6.3,13.5,6.3,9.7,16,9.7);

        ROD_Y_SHAPE = Shapes.or(vy1, vy2, vy3).optimize();

        VoxelShape vz1 = Block.box(6.3,6.3,0,9.7,9.7,2.5);
        VoxelShape vz2 = Block.box(7,7,2.5,9,9,13.5);
        VoxelShape vz3 = Block.box(6.3,6.3,13.5,9.7,9.7,16);

        ROD_Z_SHAPE = Shapes.or(vz1, vz2, vz3).optimize();

        DIRECTION_TO_SIDE_SHAPE = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, ROD_Z_SHAPE, Direction.SOUTH, ROD_Z_SHAPE, Direction.EAST, ROD_X_SHAPE, Direction.WEST, ROD_X_SHAPE, Direction.UP, ROD_Y_SHAPE, Direction.DOWN, ROD_Y_SHAPE));
    }
}