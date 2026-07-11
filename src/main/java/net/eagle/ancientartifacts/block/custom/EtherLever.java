package net.eagle.ancientartifacts.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;

public class EtherLever extends LeverBlock {

    public static final BooleanProperty POWERED = LeverBlock.POWERED;
    protected static final VoxelShape NORTH_WALL_SHAPE;
    protected static final VoxelShape SOUTH_WALL_SHAPE;
    protected static final VoxelShape WEST_WALL_SHAPE;
    protected static final VoxelShape EAST_WALL_SHAPE;
    protected static final VoxelShape FLOOR_Z_AXIS_SHAPE;
    protected static final VoxelShape FLOOR_X_AXIS_SHAPE;
    protected static final VoxelShape CEILING_Z_AXIS_SHAPE;
    protected static final VoxelShape CEILING_X_AXIS_SHAPE;

    static {
        NORTH_WALL_SHAPE = Block.box(6.0, 5.0, 4.0, 10.0, 11.0, 16.0);
        SOUTH_WALL_SHAPE = Block.box(6.0, 5.0, 0.0, 10.0, 11.0, 10.0);
        WEST_WALL_SHAPE = Block.box(4.0, 5.0, 6.0, 16.0, 11.0, 10.0);
        EAST_WALL_SHAPE = Block.box(0.0, 5.0, 6.0, 12.0, 11.0, 10.0);
        FLOOR_Z_AXIS_SHAPE = Block.box(6.0, 0.0, 5.0, 10.0, 12.0, 11.0);
        FLOOR_X_AXIS_SHAPE = Block.box(5.0, 0.0, 6.0, 11.0, 12.0, 10.0);
        CEILING_Z_AXIS_SHAPE = Block.box(6.0, 4.0, 5.0, 10.0, 16.0, 11.0);
        CEILING_X_AXIS_SHAPE = Block.box(5.0, 4.0, 6.0, 11.0, 16.0, 10.0);
    }

    public EtherLever(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(POWERED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACE)) {
            case FLOOR -> {
                if (Objects.requireNonNull(state.getValue(FACING).getAxis()) == Direction.Axis.X) {
                    return FLOOR_X_AXIS_SHAPE;
                }
                return FLOOR_Z_AXIS_SHAPE;
            }
            case WALL -> {
                switch (state.getValue(FACING)) {
                    case EAST -> {
                        return EAST_WALL_SHAPE;
                    }
                    case WEST -> {
                        return WEST_WALL_SHAPE;
                    }
                    case SOUTH -> {
                        return SOUTH_WALL_SHAPE;
                    }
                }
                return NORTH_WALL_SHAPE;
            }
        }
        if (Objects.requireNonNull(state.getValue(FACING).getAxis()) == Direction.Axis.X) {
            return CEILING_X_AXIS_SHAPE;
        }
        return CEILING_Z_AXIS_SHAPE;
    }

    // onUse was split into useItemOn and useWithoutItem in modern MojMap
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide()) {
            BlockState blockState = state.cycle(POWERED);

            if (blockState.getValue(POWERED)) {
                EtherLever.spawnParticles(blockState, level, pos, 1.0f);
            }
            return InteractionResult.SUCCESS;
        }

        BlockState blockState = this.togglePower(state, level, pos);
        float f = blockState.getValue(POWERED) ? 0.6f : 0.5f;
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, f);
        level.gameEvent(player, blockState.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        return InteractionResult.CONSUME;
    }

    private void setRootRod(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos copperPos = pos.relative(direction);
            BlockState copperState = level.getBlockState(copperPos);
            if(copperState.getBlock() == Blocks.COPPER_BLOCK || copperState.getBlock() == Blocks.WAXED_COPPER_BLOCK){
                BlockPos[] adjacentPositions = {
                        copperPos.north(),
                        copperPos.south(),
                        copperPos.east(),
                        copperPos.west(),
                        copperPos.above(),
                        copperPos.below()
                };
                for(BlockPos rodPos : adjacentPositions) {
                    BlockState rodState = level.getBlockState(rodPos);
                    if(rodState.getBlock() instanceof CopperWire){
                        level.setBlock(rodPos, rodState.setValue(CopperWire.IS_ROOT, true).setValue(CopperWire.POWER, level.getBestNeighborSignal(pos)).setValue(CopperWire.IS_POWERED ,level.getBlockState(pos).getValue(POWERED)), 3);
                    }
                }
            }
        }
    }

    private void setNonRootRod(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos copperPos = pos.relative(direction);
            BlockState copperState = level.getBlockState(copperPos);
            if(copperState.getBlock() == Blocks.COPPER_BLOCK || copperState.getBlock() == Blocks.WAXED_COPPER_BLOCK){
                BlockPos[] adjacentPositions = {
                        copperPos.north(),
                        copperPos.south(),
                        copperPos.east(),
                        copperPos.west(),
                        copperPos.above(),
                        copperPos.below()
                };
                for(BlockPos rodPos : adjacentPositions) {
                    BlockState rodState = level.getBlockState(rodPos);
                    if(rodState.getBlock() instanceof CopperWire){
                        level.setBlock(rodPos, rodState.setValue(CopperWire.IS_ROOT, false).setValue(CopperWire.POWER, 0).setValue(CopperWire.IS_POWERED, false), 3);
                    }
                }
            }
        }
    }

    private void setRootRodOff(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos copperPos = pos.relative(direction);
            BlockState copperState = level.getBlockState(copperPos);
            if(copperState.getBlock() == Blocks.COPPER_BLOCK || copperState.getBlock() == Blocks.WAXED_COPPER_BLOCK){
                BlockPos[] adjacentPositions = {
                        copperPos.north(),
                        copperPos.south(),
                        copperPos.east(),
                        copperPos.west(),
                        copperPos.above(),
                        copperPos.below()
                };
                for(BlockPos rodPos : adjacentPositions) {
                    BlockState rodState = level.getBlockState(rodPos);
                    if(rodState.getBlock() instanceof CopperWire){
                        level.setBlock(rodPos, rodState.setValue(CopperWire.POWER, 0).setValue(CopperWire.IS_POWERED, false), 3);
                    }
                }
            }
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        setRootRod(level, pos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        setNonRootRod(level, pos);
        return state;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return super.getSignal(state, level, pos, direction);
    }

    private static void spawnParticles(BlockState state, LevelAccessor level, BlockPos pos, float alpha) {
        Direction direction = state.getValue(FACING).getOpposite();

        // LeverBlock.getDirection -> LeverBlock.getConnectedDirection
        Direction direction2 = LeverBlock.getConnectedDirection(state).getOpposite();

        // getOffsetX() -> getStepX()
        double d = (double)pos.getX() + 0.5 + 0.1 * (double)direction.getStepX() + 0.2 * (double)direction2.getStepX();
        double e = (double)pos.getY() + 0.5 + 0.1 * (double)direction.getStepY() + 0.2 * (double)direction2.getStepY();
        double f = (double)pos.getZ() + 0.5 + 0.1 * (double)direction.getStepZ() + 0.2 * (double)direction2.getStepZ();

        int blueColor = 0x0000FF;

        level.addParticle(new DustParticleOptions(blueColor, alpha), d, e, f, 0.0, 0.0, 0.0);
    }

    private void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(LeverBlock.getConnectedDirection(state).getOpposite()), this);
    }

    public BlockState togglePower(BlockState state, Level level, BlockPos pos) {
        state = state.cycle(POWERED);
        if(!state.getValue(POWERED)){
            setRootRodOff(level, pos);
        }
        level.setBlock(pos, state, 3); // 3 = Block.NOTIFY_ALL
        this.updateNeighbors(state, level, pos);
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, POWERED);
    }
}