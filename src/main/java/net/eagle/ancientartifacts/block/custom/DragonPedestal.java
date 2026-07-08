package net.eagle.ancientartifacts.block.custom;

import com.mojang.serialization.MapCodec;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.entity.DragonPedestalEntity;
import net.eagle.ancientartifacts.block.entity.ModBlockEntities;
import net.eagle.ancientartifacts.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.WATERLOGGED;


public class DragonPedestal extends BlockWithEntity implements BlockEntityProvider {

    public static final BooleanProperty GILDED = BooleanProperty.of("gilded");
    public static final BooleanProperty FOSSIL_HEAD = BooleanProperty.of("fossil_head");
    public static final BooleanProperty HEART_SEA = BooleanProperty.of("heart_sea");
    public static final BooleanProperty ORB_INFINIUM = BooleanProperty.of("orb_of_infinium");

    public static final BooleanProperty END_READY = BooleanProperty.of("end_ready");
    public static final EnumProperty<Direction> FACING = Properties.HOPPER_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape SHAPE_UPPER;
    protected static final VoxelShape SHAPE_UPPER_F;
    protected static final VoxelShape SHAPE_LOWER;
    protected static final VoxelShape SHAPE_LOWER_G;

    static {
        //TOP
        VoxelShape su1 = Block.createCuboidShape(5.5, 0, 5.5, 10.5, 1, 10.5);
        VoxelShape su2 = Block.createCuboidShape(3.5, 1, 3.5, 12.5, 4, 12.5);
        VoxelShape su3_f = Block.createCuboidShape(1, 3, 3.25, 15.5, 11.7, 12.75);
        VoxelShape su3 = Block.createCuboidShape(3.5, 3, 3.25, 12.5, 6, 12.75);

        //BOTTOM
        VoxelShape sl1_g = Block.createCuboidShape(1, 0, 1, 15, 1, 15);
        VoxelShape sl1 = Block.createCuboidShape(2, 0, 2, 14, 1, 14);
        VoxelShape sl2 = Block.createCuboidShape(3, 1, 3, 13, 3, 13);
        VoxelShape sl3 = Block.createCuboidShape(5.5, 3, 5.5, 10.5, 12, 10.5);

        SHAPE_UPPER = VoxelShapes.union(su1, su2, su3).simplify();
        SHAPE_UPPER_F = VoxelShapes.union(su1, su2, su3_f).simplify();
        SHAPE_LOWER = VoxelShapes.union(sl1, sl2, sl3).simplify();
        SHAPE_LOWER_G = VoxelShapes.union(sl1_g, sl2, sl3).simplify();

    }


    public DragonPedestal(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(FACING, Direction.NORTH)
                .with(GILDED, false)
                .with(FOSSIL_HEAD, false)
                .with(HEART_SEA, false)
                .with(ORB_INFINIUM, false)
                .with(END_READY, false)
                .with(WATERLOGGED, false));
    }

    public static final MapCodec<DragonPedestal> CODEC = createCodec(DragonPedestal::new);

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            return state.get(FOSSIL_HEAD) ? SHAPE_UPPER_F : SHAPE_UPPER;
        } else {
            return state.get(GILDED) ? SHAPE_LOWER_G : SHAPE_LOWER;
        }
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {

        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient()) {
            Block down = world.getBlockState(pos.down()).getBlock();
            if (down == ModBlocks.GILDED_PLATE && !state.get(GILDED)) {
                BlockState lower = state.with(HALF, DoubleBlockHalf.LOWER).with(GILDED, true);
                BlockState upper = lower.with(HALF, DoubleBlockHalf.UPPER);

                world.removeBlock(pos, false); // removing current (lower) placeholder
                world.setBlockState(pos.down(), lower, Block.NOTIFY_ALL);
                world.setBlockState(pos, upper, Block.NOTIFY_ALL);
            }
        }

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            super.onPlaced(world, pos, state, placer, itemStack);
            return;
        }
        // Mirror all props into the top half at place time
        BlockState top = state.with(HALF, DoubleBlockHalf.UPPER);
        world.setBlockState(pos.up(), top, Block.NOTIFY_ALL);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        var world = ctx.getWorld();

        if (blockPos.getY() < world.getTopYInclusive() && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            FluidState fluidState = world.getFluidState(blockPos);
            boolean isWaterlogged = fluidState.getFluid() == Fluids.WATER;
            return this.getDefaultState()
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(HALF, DoubleBlockHalf.LOWER)
                    .with(WATERLOGGED, isWaterlogged);
        } else {
            return null;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world,
                                         BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // Switch on the full registry id
        final String id = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).toString();

        // 1. ALWAYS identify the absolute bottom and top positions, regardless of which half the player clicked!
        BlockPos botPos = state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos;
        BlockPos topPos = botPos.up();

        // 2. Always base logic on the bottom state to ensure both halves stay in perfect sync
        BlockState botState = world.getBlockState(botPos);

        // Safety check in case the top half is missing or corrupted
        if (!world.getBlockState(topPos).isOf(this)) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        }

        switch (id) {
            case "ancientartifacts:end_staff" -> {
                if (!botState.get(END_READY) && botState.get(ORB_INFINIUM)) {
                    BlockState newBot = botState.with(END_READY, true);

                    world.setBlockState(botPos, newBot, Block.NOTIFY_ALL);
                    world.setBlockState(topPos, newBot.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);

                    world.playSound(null, pos, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.HOSTILE, 0.2f, 0.9f);
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP,   SoundCategory.NEUTRAL, 0.2f, 1.0f);
                    if (!player.isCreative()) {
                        player.sendMessage(Text.literal("End Gateway is now Unlocked!"), false);
                    }
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }

            case "ancientartifacts:orb_infinium" -> {
                if (!botState.get(ORB_INFINIUM) && botState.get(HEART_SEA)) {
                    BlockState newBot = botState.with(ORB_INFINIUM, true);

                    world.setBlockState(botPos, newBot, Block.NOTIFY_ALL);
                    world.setBlockState(topPos, newBot.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);

                    world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.AMBIENT, 1.0f, 0.6f);
                    if (!player.isCreative()) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }

            case "minecraft:heart_of_the_sea" -> {
                if (!botState.get(HEART_SEA) && botState.get(FOSSIL_HEAD)) {
                    BlockState newBot = botState.with(HEART_SEA, true);

                    world.setBlockState(botPos, newBot, Block.NOTIFY_ALL);
                    world.setBlockState(topPos, newBot.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);

                    world.playSound(null, pos, SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 0.4f);
                    if (!player.isCreative()) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }

            case "ancientartifacts:dragon_fossil" -> {
                if (!botState.get(FOSSIL_HEAD)
                        && botState.get(GILDED)
                        && !botState.get(HEART_SEA)
                        && hasNearbyRitual(world, botPos)) {

                    // Calculate the final state
                    BlockState newBot = botState.with(FOSSIL_HEAD, true);

                    world.setBlockState(botPos, newBot, Block.NOTIFY_ALL);
                    world.setBlockState(topPos, newBot.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);

                    world.playSound(null, botPos, SoundEvents.BLOCK_BONE_BLOCK_PLACE, SoundCategory.BLOCKS, 0.8f, 0.3f);
                    if (!player.isCreative()) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }

            default -> {
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }
        }
    }

    private static boolean hasNearbyRitual(World world, BlockPos basePos) {
        final int r = 2;
        // scan one block below to one block above, and 2 blocks out in X/Z
        BlockPos min = basePos.add(-r, -1, -r);
        BlockPos max = basePos.add( r,  1,  r);

        boolean hasDirt  = false;
        boolean hasBrick = false;
        boolean hasLever = false;

        for (BlockPos p : BlockPos.iterate(min, max)) {
            BlockState s = world.getBlockState(p);

            if (!hasDirt && (s.isOf(Blocks.DIRT) /* or a tag: || s.isIn(BlockTags.DIRT) */)) {
                hasDirt = true;
            }
            if (!hasBrick && s.isOf(ModBlocks.NENDER_BRICK)) {
                hasBrick = true;
            }
            if (!hasLever && s.isOf(ModBlocks.ETHER_LEVER)) {
                hasLever = true;
            }

            if (hasDirt && hasBrick && hasLever) return true;
        }
        return false;
    }



    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.UPPER ? null : new DragonPedestalEntity(pos, state);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
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

        if (!player.isCreative()) {
            ItemStack pedestal = new ItemStack(ModBlocks.DRAGON_PEDESTAL);
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), pedestal));
            if(state.get(DragonPedestal.GILDED)){
                ItemStack plate = new ItemStack(ModBlocks.GILDED_PLATE);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), plate));
                if(state.get(DragonPedestal.FOSSIL_HEAD)){
                    ItemStack fossil = new ItemStack(ModItems.DRAGON_FOSSIL);
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), fossil));
                    if(state.get(DragonPedestal.HEART_SEA)){
                        ItemStack heart_sea = new ItemStack(Items.HEART_OF_THE_SEA);
                        world.spawnEntity(new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), heart_sea));
                        if(state.get(DragonPedestal.ORB_INFINIUM)){
                            ItemStack orb_infinuim = new ItemStack(ModItems.ORB_INFINIUM);
                            world.spawnEntity(new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), orb_infinuim));
                        }
                    }
                }
            }
        }

        return state;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.DRAGON_PEDESTAL_ENTITY ?
                (worldIn, pos, stateIn, blockEntity) -> DragonPedestalEntity.tick(worldIn, pos, stateIn, (DragonPedestalEntity) blockEntity) :
                null;
    }
}
