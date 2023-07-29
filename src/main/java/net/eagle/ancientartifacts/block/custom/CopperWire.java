package net.eagle.ancientartifacts.block.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CopperWire extends Block implements Waterloggable{

    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    public static final BooleanProperty WIRE_CONNECTION_NORTH = Properties.NORTH;
    public static final BooleanProperty WIRE_CONNECTION_SOUTH = Properties.SOUTH;
    public static final BooleanProperty WIRE_CONNECTION_EAST = Properties.EAST;
    public static final BooleanProperty WIRE_CONNECTION_WEST = Properties.WEST;
    public static final BooleanProperty WIRE_CONNECTION_UP = Properties.UP;
    public static final BooleanProperty WIRE_CONNECTION_DOWN = Properties.DOWN;
    public static final BooleanProperty IS_ROOT;
    public static final BooleanProperty IS_POWERED;
    public static final IntProperty POWER = IntProperty.of("power", 0, 15);
    // protected static final VoxelShape ROD_CENTER_SHAPE;
    protected static final VoxelShape ROD_X_SHAPE;
    protected static final VoxelShape ROD_Y_SHAPE;
    protected static final VoxelShape ROD_Z_SHAPE;
    private static final Map<Direction, VoxelShape> DIRECTION_TO_SIDE_SHAPE;

    private static final Map<BlockState, VoxelShape> SHAPES = Maps.newHashMap();
    public static final Map<Direction, BooleanProperty> DIRECTION_TO_WIRE_CONNECTION_PROPERTY = Maps.newHashMap(ImmutableMap.of(Direction.NORTH, WIRE_CONNECTION_NORTH, Direction.EAST, WIRE_CONNECTION_EAST, Direction.SOUTH, WIRE_CONNECTION_SOUTH, Direction.WEST, WIRE_CONNECTION_WEST, Direction.UP, WIRE_CONNECTION_UP, Direction.DOWN, WIRE_CONNECTION_DOWN));


    public CopperWire(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(IS_ROOT, false).with(IS_POWERED, false).with(WATERLOGGED, false).with(FACING, Direction.DOWN).with(WIRE_CONNECTION_DOWN, false).with(WIRE_CONNECTION_UP, false).with(WIRE_CONNECTION_NORTH, false).with(WIRE_CONNECTION_SOUTH,false).with(WIRE_CONNECTION_EAST,false).with(WIRE_CONNECTION_WEST,false).with(POWER,0));
        for (BlockState blockState : this.getStateManager().getStates()) {
            if (blockState.get(POWER) != 0) continue;
            SHAPES.put(blockState, this.getShapeForState(blockState));
        }
    }
    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.with(POWER, 0).with(IS_POWERED, false));
    }

    private VoxelShape getShapeForState(BlockState state) {
        VoxelShape shape = getBaseShape(state);
        for (Direction direction : Direction.values()) {
            if (state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction))) {
                VoxelShape tempShape = DIRECTION_TO_SIDE_SHAPE.get(direction);
                shape = VoxelShapes.union(tempShape, shape);
            }
        }
        return shape;
    }
    public VoxelShape getBaseShape(BlockState state) {
        switch (state.get(FACING).getAxis()) {
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean waterlogged = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
        return this.getPlacementState(ctx.getWorld(), this.getDefaultState(), ctx.getBlockPos())
                .with(FACING, ctx.getSide()).with(WATERLOGGED, waterlogged);
    }
    private BlockState getPlacementState(World world, BlockState state, BlockPos pos){
        int strongestPower = 0;
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if(neighborState.getBlock() == Blocks.COPPER_BLOCK || neighborState.getBlock() == Blocks.WAXED_COPPER_BLOCK){
                BlockPos leverPos = findEtherLever(world, neighborPos);
                BlockState leverPowerState = world.getBlockState(leverPos);
                if(leverPos != pos && leverPowerState.getBlock() instanceof EtherLever){
                    state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true).with(IS_ROOT, true).with(POWER, world.getReceivedRedstonePower(neighborPos));
                } else {
                    state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true).with(IS_ROOT, false);
                }
            }
            else if (neighborState.getBlock() instanceof CopperWire) {
                state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true); // Set the property based on the direction
                world.setBlockState(neighborPos, neighborState.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()), true), 3);
            } else {
                if(neighborState.isSolidBlock(world, neighborPos)){
                    state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), true);
                }
            }
        }
        strongestPower = getStrongestRestonePower(world, pos);
        if(strongestPower == 0){
            state = state.with(POWER, strongestPower);
        } else{
            world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, state.get(FACING).getAxis().ordinal());
            state = state.with(POWER, strongestPower - 1);
        }
        if(state.get(POWER) > 0){
            state = state.with(IS_POWERED, true);

        } else {
            state = state.with(IS_POWERED, false);
        }
        return state;
    }
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);

        // Update the neighboring CopperWire blocks when a block is broken
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof CopperWire) {
                if(state.get(IS_ROOT)){
                    BlockState updatedNeighborState = neighborState.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()), false).with(POWER, 0).with(IS_POWERED, false);
                    world.setBlockState(neighborPos, updatedNeighborState, 3);
                } else{
                    BlockState updatedNeighborState = neighborState.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction.getOpposite()), false);
                    world.setBlockState(neighborPos, updatedNeighborState, 3);
                }
                this.updateNeighbors(world, pos);
            }
        }
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock()) || world.isClient) {
            return;
        }
        this.updateNeighbors(world, pos);

        // Call update for CopperWire and non-CopperWire neighbors
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAlways(pos.offset(direction), this);
        }

        this.updateOffsetNeighbors(world, pos);
    }
    private void updateOffsetNeighbors(World world, BlockPos pos) {
        // Update all offset neighbors
        for (Direction direction : Direction.values()) {
            this.updateNeighbors(world, pos.offset(direction));
        }
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.offset(direction);
            if (world.getBlockState(blockPos).isSolidBlock(world, blockPos)) {
                this.updateNeighbors(world, blockPos.up());
                continue;
            }
            this.updateNeighbors(world, blockPos.down());

        }
    }
    private void updateNeighbors(World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAlways(pos.offset(direction), this);
        }
    }
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);

        int netPower = 0;
        BlockState copperPowerState = world.getBlockState(fromPos);
        BlockState fromState = world.getBlockState(fromPos);
        if(copperPowerState.getBlock() == Blocks.COPPER_BLOCK || copperPowerState.getBlock() == Blocks.WAXED_COPPER_BLOCK) {
            BlockPos leverPos = findEtherLever(world, fromPos);
            BlockState leverState = world.getBlockState(leverPos);
            if(leverState.getBlock() instanceof EtherLever){
                netPower = world.getReceivedRedstonePower(fromPos);
                state = state.with(POWER, netPower).with(IS_ROOT, true);
                if(state.get(POWER) > 0){
                    state = state.with(IS_POWERED, true);
                    world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, state.get(FACING).getAxis().ordinal());
                } else {
                    state = state.with(IS_POWERED, false);
                }
                world.setBlockState(pos, state, 3);
                return;
            } else {
                state = state.with(POWER, netPower).with(IS_ROOT, false);
                if(state.get(POWER) > 0){
                    state = state.with(IS_POWERED, true);
                } else {
                    state = state.with(IS_POWERED, false);
                }
                world.setBlockState(pos, state, 3);
                return;
            }
        } else if(fromState.getBlock() instanceof CopperWire){
            if(!state.get(IS_ROOT)){
                int strongestPower = getStrongestRestonePower(world, pos);
                for (Direction direction : Direction.values()) {
                    BlockPos neighborPos = pos.offset(direction);
                    BlockState neighborState = world.getBlockState(neighborPos);
                    if(neighborState.getBlock() instanceof  CopperWire){
                        int powerRecieved = world.getEmittedRedstonePower(pos.offset(direction), direction);
                        strongestPower = Math.max(strongestPower, powerRecieved);
                    }
                }
                if(strongestPower == 0){
                    state = state.with(POWER, strongestPower);
                } else{
                    state = state.with(POWER, strongestPower - 1);
                }
            }
        }
        if(state.get(POWER) > 0){
            state = state.with(IS_POWERED, true);
        } else {
            state = state.with(IS_POWERED, false);
        }
        if(state.get(IS_ROOT)){
            boolean hasCopperBlockNeighbor = false;
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                if (world.getBlockState(neighborPos).getBlock() == Blocks.COPPER_BLOCK || world.getBlockState(neighborPos).getBlock() == Blocks.WAXED_COPPER_BLOCK) {
                    hasCopperBlockNeighbor = true;
                    break;
                }
            }
            if (!hasCopperBlockNeighbor) {
                state = state.with(POWER, 0).with(IS_POWERED, false).with(IS_ROOT, false);
            }
        }else {
            boolean hasCopperWireNeighbors = false;
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                if (world.getBlockState(neighborPos).getBlock() instanceof CopperWire) {
                    hasCopperWireNeighbors = true;
                    break;
                }
            }

            // If no CopperWire neighbors are found, reset the power to 0
            if (!hasCopperWireNeighbors) {
                state = state.with(POWER, 0).with(IS_POWERED, false);
            }
        }
        world.setBlockState(pos, state, 3);
    }
    private int getStrongestRestonePower(World world, BlockPos pos){
        int strongestPower = 0;
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if(neighborState.getBlock() instanceof CopperWire){
                int powerReceived = neighborState.get(POWER);
                strongestPower = Math.max(strongestPower, powerReceived);
            }
        }
        return strongestPower;
    }
    private BlockPos findEtherLever(World world, BlockPos pos){
        for (Direction direction : Direction.values()) {
            BlockPos leverPos = pos.offset(direction);
            BlockState leverState = world.getBlockState(leverPos);
            if(leverState.getBlock() instanceof EtherLever){
                return leverPos;
            }
        }
        return pos;
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED).booleanValue()) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
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
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.isOf(newState.getBlock())) {
            return;
        }
        super.onStateReplaced(state, world, pos, newState, moved);
        if (world.isClient) {
            return;
        }

        // Update neighboring blocks when a CopperWire block is replaced
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAlways(pos.offset(direction), this);
        }

        this.updateNeighbors(world, pos);
        this.updateOffsetNeighbors(world, pos);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if(state.get(POWER) == 0){
            return 0;
        }
        if (state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction))) {
            return state.get(POWER);
        }
        return 0;
    }
    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getWeakRedstonePower(world, pos, direction);
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_ROOT, WATERLOGGED, IS_POWERED, POWER, FACING, WIRE_CONNECTION_NORTH, WIRE_CONNECTION_SOUTH, WIRE_CONNECTION_EAST, WIRE_CONNECTION_WEST, WIRE_CONNECTION_UP, WIRE_CONNECTION_DOWN);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        FACING = Properties.FACING;
        IS_ROOT = BooleanProperty.of("is_root");
        IS_POWERED = BooleanProperty.of("is_powered");
        VoxelShape vx1 = Block.createCuboidShape(0,6.3,6.3,2.5,9.7,9.7);
        VoxelShape vx2 = Block.createCuboidShape(2.5,7,7,13.5,9,9);
        VoxelShape vx3 = Block.createCuboidShape(13.5,6.3,6.3,16,9.7,9.7);

        ROD_X_SHAPE = VoxelShapes.union(vx1, vx2, vx3).simplify();

        VoxelShape vy1 = Block.createCuboidShape(6.3,0,6.3,9.7,2.5,9.7);
        VoxelShape vy2 = Block.createCuboidShape(7,2.5,7,9,13.5,9);
        VoxelShape vy3 = Block.createCuboidShape(6.3,13.5,6.3,9.7,16,9.7);

        ROD_Y_SHAPE = VoxelShapes.union(vy1, vy2, vy3).simplify();

        VoxelShape vz1 = Block.createCuboidShape(6.3,6.3,0,9.7,9.7,2.5);
        VoxelShape vz2 = Block.createCuboidShape(7,7,2.5,9,9,13.5);
        VoxelShape vz3 = Block.createCuboidShape(6.3,6.3,13.5,9.7,9.7,16);

        ROD_Z_SHAPE = VoxelShapes.union(vz1, vz2, vz3).simplify();

        //ROD_CENTER_SHAPE = Block.createCuboidShape(7,7,7,9,9,9);

        DIRECTION_TO_SIDE_SHAPE = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, ROD_Z_SHAPE, Direction.SOUTH, ROD_Z_SHAPE, Direction.EAST, ROD_X_SHAPE, Direction.WEST, ROD_X_SHAPE, Direction.UP, ROD_Y_SHAPE, Direction.DOWN, ROD_Y_SHAPE));

    }

}