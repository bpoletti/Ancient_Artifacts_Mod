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
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
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
    public static final BooleanProperty FOSSIL_HEAD = BooleanProperty.of("fossil_head");
    public static final BooleanProperty HEART_SEA = BooleanProperty.of("heart_sea");
    public static final BooleanProperty ORB_INFINIUM = BooleanProperty.of("orb_of_infinium");

    public static final BooleanProperty END_READY = BooleanProperty.of("end_ready");
    public final static DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape SHAPE_UPPER;
    protected static final VoxelShape SHAPE_UPPER_F;
    protected static final VoxelShape SHAPE_LOWER;
    protected static final VoxelShape SHAPE_LOWER_G;

    @Nullable
    private BlockPattern ritualPattern;
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
                .with(END_READY, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == DoubleBlockHalf.LOWER
                ? (state.get(GILDED)
                ? state.get(FOSSIL_HEAD)
                ? SHAPE_UPPER_F
                : SHAPE_LOWER_G
                : SHAPE_LOWER)
                : SHAPE_UPPER;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {

        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            Block down = world.getBlockState(pos.down()).getBlock();
            if (down == ModBlocks.GILDED_PLATE && !state.get(DragonPedestal.GILDED)) {
                world.removeBlock(pos, false);
                world.setBlockState(pos.down(), state.with(HALF, DoubleBlockHalf.LOWER)
                        .with(GILDED, true), 3);
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
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    public BlockPattern getRitualPattern() {
        if(this.ritualPattern == null){
            this.ritualPattern = BlockPatternBuilder.start()
                    .aisle(" LD","P~N")
                    .where('N', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.NENDER_BRICK)))
                    .where('P', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.DRAGON_PEDESTAL)))
                    .where('~', pos -> pos.getBlockState().isAir())
                    .where('L', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.ETHER_LEVER)))
                    .where('D', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.DIRT)))
                    .build();
        }
        return this.ritualPattern;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF,
                FACING,
                GILDED,
                FOSSIL_HEAD,
                HEART_SEA,
                ORB_INFINIUM,
                END_READY);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DragonPedestalEntity) {
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world,
                                             BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // if you also want empty-hand behavior, override onUseWithoutItem as well
        final var correctPattern = this.getRitualPattern().searchAround(world, pos);

        // Switch on the full registry id, e.g. "ancientartifacts:orb_infinium", "minecraft:heart_of_the_sea"
        final String id = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).toString();

        switch (id) {
            case "ancientartifacts:end_staff" -> {
                if (!state.get(DragonPedestal.END_READY) && state.get(DragonPedestal.ORB_INFINIUM)) {
                    world.setBlockState(pos, state.with(END_READY, true).with(HALF, DoubleBlockHalf.UPPER));
                    world.playSound(null, pos, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.HOSTILE, 0.2f, 0.9f);
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP,   SoundCategory.NEUTRAL, 0.2f, 1.0f);
                    if (!player.isCreative()) {
                        player.sendMessage(Text.literal("End Gateway is now Unlocked!"));
                    }
                }
                // Do not consume the staff; let default block action run if needed
                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            case "ancientartifacts:orb_infinium" -> {
                if (!state.get(DragonPedestal.ORB_INFINIUM) && state.get(DragonPedestal.HEART_SEA)) {
                    world.setBlockState(pos, state.with(ORB_INFINIUM, true).with(HALF, DoubleBlockHalf.UPPER));
                    world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.AMBIENT, 1.0f, 0.6f);
                    if (!player.isCreative()) stack.decrement(1);
                }
                return ItemActionResult.CONSUME;
            }

            case "minecraft:heart_of_the_sea" -> {
                if (!state.get(DragonPedestal.HEART_SEA) && state.get(DragonPedestal.FOSSIL_HEAD)) {
                    world.setBlockState(pos, state.with(HEART_SEA, true).with(HALF, DoubleBlockHalf.UPPER));
                    world.playSound(null, pos, SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 0.4f);
                    if (!player.isCreative()) stack.decrement(1);
                }
                return ItemActionResult.CONSUME;
            }

            case "ancientartifacts:dragon_fossil" -> {
                if (!state.get(DragonPedestal.FOSSIL_HEAD)
                        && state.get(DragonPedestal.GILDED)
                        && !state.get(DragonPedestal.HEART_SEA)
                        && correctPattern != null) {
                    world.setBlockState(pos.up(), state.with(FOSSIL_HEAD, true).with(HALF, DoubleBlockHalf.UPPER));
                    world.playSound(null, pos, SoundEvents.BLOCK_BONE_BLOCK_PLACE, SoundCategory.BLOCKS, 0.8f, 0.3f);
                    if (!player.isCreative()) stack.decrement(1);
                }
                return ItemActionResult.CONSUME;
            }

            default -> {

                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
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
