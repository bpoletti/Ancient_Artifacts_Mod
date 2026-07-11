package net.eagle.ancientartifacts.block.custom;

import com.mojang.serialization.MapCodec;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.entity.DragonPedestalEntity;
import net.eagle.ancientartifacts.block.entity.ModBlockEntities;
import net.eagle.ancientartifacts.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DragonPedestal extends BaseEntityBlock implements EntityBlock {

    public static final BooleanProperty GILDED = BooleanProperty.create("gilded");
    public static final BooleanProperty FOSSIL_HEAD = BooleanProperty.create("fossil_head");
    public static final BooleanProperty HEART_SEA = BooleanProperty.create("heart_sea");
    public static final BooleanProperty ORB_INFINIUM = BooleanProperty.create("orb_of_infinium");
    public static final BooleanProperty END_READY = BooleanProperty.create("end_ready");

    // Explicitly declaring WATERLOGGED to ensure no inheritance issues
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING_HOPPER;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    protected static final VoxelShape SHAPE_UPPER;
    protected static final VoxelShape SHAPE_UPPER_F;
    protected static final VoxelShape SHAPE_LOWER;
    protected static final VoxelShape SHAPE_LOWER_G;

    static {
        //TOP
        VoxelShape su1 = Block.box(5.5, 0, 5.5, 10.5, 1, 10.5);
        VoxelShape su2 = Block.box(3.5, 1, 3.5, 12.5, 4, 12.5);
        VoxelShape su3_f = Block.box(1, 3, 3.25, 15.5, 11.7, 12.75);
        VoxelShape su3 = Block.box(3.5, 3, 3.25, 12.5, 6, 12.75);

        //BOTTOM
        VoxelShape sl1_g = Block.box(1, 0, 1, 15, 1, 15);
        VoxelShape sl1 = Block.box(2, 0, 2, 14, 1, 14);
        VoxelShape sl2 = Block.box(3, 1, 3, 13, 3, 13);
        VoxelShape sl3 = Block.box(5.5, 3, 5.5, 10.5, 12, 10.5);

        SHAPE_UPPER = Shapes.or(su1, su2, su3).optimize();
        SHAPE_UPPER_F = Shapes.or(su1, su2, su3_f).optimize();
        SHAPE_LOWER = Shapes.or(sl1, sl2, sl3).optimize();
        SHAPE_LOWER_G = Shapes.or(sl1_g, sl2, sl3).optimize();
    }


    public DragonPedestal(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FACING, Direction.NORTH)
                .setValue(GILDED, false)
                .setValue(FOSSIL_HEAD, false)
                .setValue(HEART_SEA, false)
                .setValue(ORB_INFINIUM, false)
                .setValue(END_READY, false)
                .setValue(WATERLOGGED, false));
    }

    public static final MapCodec<DragonPedestal> CODEC = simpleCodec(DragonPedestal::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return state.getValue(FOSSIL_HEAD) ? SHAPE_UPPER_F : SHAPE_UPPER;
        } else {
            return state.getValue(GILDED) ? SHAPE_LOWER_G : SHAPE_LOWER;
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide()) {
            Block down = level.getBlockState(pos.below()).getBlock();
            if (down == ModBlocks.GILDED_PLATE && !state.getValue(GILDED)) {
                BlockState lower = state.setValue(HALF, DoubleBlockHalf.LOWER).setValue(GILDED, true);
                BlockState upper = lower.setValue(HALF, DoubleBlockHalf.UPPER);

                level.removeBlock(pos, false); // removing current (lower) placeholder
                level.setBlock(pos.below(), lower, 3);
                level.setBlock(pos, upper, 3);
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            super.setPlacedBy(level, pos, state, placer, itemStack);
            return;
        }
        // Mirror all props into the top half at place time
        BlockState top = state.setValue(HALF, DoubleBlockHalf.UPPER);
        level.setBlock(pos.above(), top, 3);
        super.setPlacedBy(level, pos, state, placer, itemStack);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        var level = ctx.getLevel();

        if (blockPos.getY() < level.getMaxY() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
            FluidState fluidState = level.getFluidState(blockPos);
            boolean isWaterlogged = fluidState.getType() == Fluids.WATER;
            return this.defaultBlockState()
                    .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER)
                    .setValue(WATERLOGGED, isWaterlogged);
        } else {
            return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF,
                FACING,
                GILDED,
                FOSSIL_HEAD,
                HEART_SEA,
                ORB_INFINIUM,
                END_READY,
                WATERLOGGED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                          BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        final String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        BlockPos botPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        BlockPos topPos = botPos.above();

        BlockState botState = level.getBlockState(botPos);

        if (!level.getBlockState(topPos).is(this)) {
            return InteractionResult.PASS;
        }

        switch (id) {
            case "ancientartifacts:end_staff" -> {
                if (!botState.getValue(END_READY) && botState.getValue(ORB_INFINIUM)) {
                    BlockState newBot = botState.setValue(END_READY, true);

                    level.setBlock(botPos, newBot, 3);
                    level.setBlock(topPos, newBot.setValue(HALF, DoubleBlockHalf.UPPER), 3);

                    level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 0.2f, 0.9f);
                    level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP,   SoundSource.NEUTRAL, 0.2f, 1.0f);
                    if (!player.isCreative()) {
                        player.sendOverlayMessage(Component.literal("End Gateway is now Unlocked!"));
                    }
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            case "ancientartifacts:orb_infinium" -> {
                if (!botState.getValue(ORB_INFINIUM) && botState.getValue(HEART_SEA)) {
                    BlockState newBot = botState.setValue(ORB_INFINIUM, true);

                    level.setBlock(botPos, newBot, 3);
                    level.setBlock(topPos, newBot.setValue(HALF, DoubleBlockHalf.UPPER), 3);

                    level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.AMBIENT, 1.0f, 0.6f);
                    if (!player.isCreative()) stack.shrink(1);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            case "minecraft:heart_of_the_sea" -> {
                if (!botState.getValue(HEART_SEA) && botState.getValue(FOSSIL_HEAD)) {
                    BlockState newBot = botState.setValue(HEART_SEA, true);

                    level.setBlock(botPos, newBot, 3);
                    level.setBlock(topPos, newBot.setValue(HALF, DoubleBlockHalf.UPPER), 3);

                    level.playSound(null, pos, SoundEvents.CONDUIT_ACTIVATE, SoundSource.BLOCKS, 1.0f, 0.4f);
                    if (!player.isCreative()) stack.shrink(1);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            case "ancientartifacts:dragon_fossil" -> {
                if (!botState.getValue(FOSSIL_HEAD)
                        && botState.getValue(GILDED)
                        && !botState.getValue(HEART_SEA)
                        && hasNearbyRitual(level, botPos)) {

                    BlockState newBot = botState.setValue(FOSSIL_HEAD, true);

                    level.setBlock(botPos, newBot, 3);
                    level.setBlock(topPos, newBot.setValue(HALF, DoubleBlockHalf.UPPER), 3);

                    level.playSound(null, botPos, SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS, 0.8f, 0.3f);
                    if (!player.isCreative()) stack.shrink(1);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            default -> {
                return InteractionResult.PASS;
            }
        }
    }

    private static boolean hasNearbyRitual(Level level, BlockPos basePos) {
        final int r = 2;
        BlockPos min = basePos.offset(-r, -1, -r);
        BlockPos max = basePos.offset( r,  1,  r);

        boolean hasDirt  = false;
        boolean hasBrick = false;
        boolean hasLever = false;

        for (BlockPos p : BlockPos.betweenClosed(min, max)) {
            BlockState s = level.getBlockState(p);

            if (!hasDirt && (s.is(Blocks.DIRT))) {
                hasDirt = true;
            }
            if (!hasBrick && s.is(ModBlocks.NENDER_BRICK)) {
                hasBrick = true;
            }
            if (!hasLever && s.is(ModBlocks.ETHER_LEVER)) {
                hasLever = true;
            }

            if (hasDirt && hasBrick && hasLever) return true;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? null : new DragonPedestalEntity(pos, state);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos topPos;
        BlockPos botPos;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            topPos = pos;
            botPos = pos.below();
        } else {
            topPos = pos.above();
            botPos = pos;
        }
        level.removeBlock(topPos, false);
        level.removeBlock(botPos, false);
        level.updateNeighborsAt(topPos, Blocks.AIR);

        super.playerWillDestroy(level, pos, state, player);

        if (!player.isCreative()) {
            ItemStack pedestal = new ItemStack(ModBlocks.DRAGON_PEDESTAL);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), pedestal));
            if(state.getValue(DragonPedestal.GILDED)){
                ItemStack plate = new ItemStack(ModBlocks.GILDED_PLATE);
                BlockPos dropPos = pos.above();
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), plate));
                if(state.getValue(DragonPedestal.FOSSIL_HEAD)){
                    ItemStack fossil = new ItemStack(ModItems.DRAGON_FOSSIL);
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), fossil));
                    if(state.getValue(DragonPedestal.HEART_SEA)){
                        ItemStack heart_sea = new ItemStack(Items.HEART_OF_THE_SEA);
                        level.addFreshEntity(new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), heart_sea));
                        if(state.getValue(DragonPedestal.ORB_INFINIUM)){
                            ItemStack orb_infinuim = new ItemStack(ModItems.ORB_INFINIUM);
                            level.addFreshEntity(new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), orb_infinuim));
                        }
                    }
                }
            }
        }
        return state;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.DRAGON_PEDESTAL_ENTITY ?
                (worldIn, pos, stateIn, blockEntity) -> DragonPedestalEntity.tick(worldIn, pos, stateIn, (DragonPedestalEntity) blockEntity) :
                null;
    }
}