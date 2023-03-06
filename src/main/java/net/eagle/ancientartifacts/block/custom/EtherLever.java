package net.eagle.ancientartifacts.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


@SuppressWarnings("deprecation")
public class EtherLever extends WallMountedBlock {

    public static final BooleanProperty POWERED = Properties.POWERED;
    protected static final VoxelShape NORTH_WALL_SHAPE;
    protected static final VoxelShape SOUTH_WALL_SHAPE;
    protected static final VoxelShape WEST_WALL_SHAPE;
    protected static final VoxelShape EAST_WALL_SHAPE;
    protected static final VoxelShape FLOOR_Z_AXIS_SHAPE;
    protected static final VoxelShape FLOOR_X_AXIS_SHAPE;
    protected static final VoxelShape CEILING_Z_AXIS_SHAPE;
    protected static final VoxelShape CEILING_X_AXIS_SHAPE;

    static {
        NORTH_WALL_SHAPE = Block.createCuboidShape(6.0, 5.0, 4.0, 10.0, 11.0, 16.0);
        SOUTH_WALL_SHAPE = Block.createCuboidShape(6.0, 5.0, 0.0, 10.0, 11.0, 10.0);
        WEST_WALL_SHAPE = Block.createCuboidShape(4.0, 5.0, 6.0, 16.0, 11.0, 10.0);
        EAST_WALL_SHAPE = Block.createCuboidShape(0.0, 5.0, 6.0, 12.0, 11.0, 10.0);
        FLOOR_Z_AXIS_SHAPE = Block.createCuboidShape(6.0, 0.0, 5.0, 10.0, 12.0, 11.0);
        FLOOR_X_AXIS_SHAPE = Block.createCuboidShape(5.0, 0.0, 6.0, 11.0, 12.0, 10.0);
        CEILING_Z_AXIS_SHAPE = Block.createCuboidShape(6.0, 4.0, 5.0, 10.0, 16.0, 11.0);
        CEILING_X_AXIS_SHAPE = Block.createCuboidShape(5.0, 4.0, 6.0, 11.0, 16.0, 10.0);
    }
    
    public EtherLever(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FACE, WallMountLocation.WALL)
                .with(POWERED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACE)) {
            case FLOOR -> {
                if (Objects.requireNonNull(state.get(FACING).getAxis()) == Direction.Axis.X) {
                    return FLOOR_X_AXIS_SHAPE;
                }
                return FLOOR_Z_AXIS_SHAPE;
            }
            case WALL -> {
                switch (state.get(FACING)) {
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
        if (Objects.requireNonNull(state.get(FACING).getAxis()) == Direction.Axis.X) {
            return CEILING_X_AXIS_SHAPE;
        }
        return CEILING_Z_AXIS_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            BlockState blockState = state.cycle(POWERED);

            if (blockState.get(POWERED)) {
                EtherLever.spawnParticles(blockState, world, pos, 1.0f);
            }
            return ActionResult.SUCCESS;
        }
        BlockState blockState = this.togglePower(state, world, pos);
        float f = blockState.get(POWERED) ? 0.6f : 0.5f;
        world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, f);
        world.emitGameEvent(player, blockState.get(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.isOf(newState.getBlock())) {
            return;
        }
        if (state.get(POWERED)) {
            this.updateNeighbors(state, world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private static void spawnParticles(BlockState state, WorldAccess world, BlockPos pos, float alpha) {
        Direction direction = state.get(FACING).getOpposite();
        Direction direction2 = LeverBlock.getDirection(state).getOpposite();
        double d = (double)pos.getX() + 0.5 + 0.1 * (double)direction.getOffsetX() + 0.2 * (double)direction2.getOffsetX();
        double e = (double)pos.getY() + 0.5 + 0.1 * (double)direction.getOffsetY() + 0.2 * (double)direction2.getOffsetY();
        double f = (double)pos.getZ() + 0.5 + 0.1 * (double)direction.getOffsetZ() + 0.2 * (double)direction2.getOffsetZ();
        world.addParticle(new DustParticleEffect(DustParticleEffect.RED, alpha), d, e, f, 0.0, 0.0, 0.0);
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(LeverBlock.getDirection(state).getOpposite()), this);
    }

    public BlockState togglePower(BlockState state, World world, BlockPos pos) {
        state = state.cycle(POWERED);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        this.updateNeighbors(state, world, pos);
        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, POWERED);
    }
}
