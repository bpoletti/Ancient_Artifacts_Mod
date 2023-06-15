package net.eagle.ancientartifacts.block.custom;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ChachapoyanIdol extends HorizontalFacingBlock {

    public static final DirectionProperty FACING;

    private static final VoxelShape SHAPE;
    @Nullable
    private BlockPattern elderianMonumentPatter;

    public static final BooleanProperty KEY = BooleanProperty.of("key");
    public static final BooleanProperty PENDANT = BooleanProperty.of("pendant");
    public static final BooleanProperty SCALES = BooleanProperty.of("scales");
    public static final BooleanProperty ELDERIAN_MONUMENT = BooleanProperty.of("elderian_monument");

    public ChachapoyanIdol(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(KEY, false)
                .with(PENDANT, false)
                .with(SCALES, false)
                .with(ELDERIAN_MONUMENT, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS,0.2f, 0.4f);
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS,0.4f, 0.4f);
        world.setBlockState(pos, state);
        // Check for nearby Creeper entities and trigger their explosion
        if (!world.isClient) {
            int radius = 16;
            boolean creeperExploded = false;
            for (CreeperEntity entity : world.getEntitiesByClass(CreeperEntity.class, new Box(pos).expand(radius), entity -> entity instanceof CreeperEntity)) {
                CreeperEntity creeper = entity;
                creeper.ignite(); // Ignite the Creeper
                creeper.setFuseSpeed(5); // Set the fuse time to 3 (immediate explosion)
                creeperExploded = true;
            }
            // Destroy the Chachapoyan Idol if a Creeper has exploded
            if (creeperExploded) {
                world.playSound(null, pos, SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.NEUTRAL, 1.0f, 0.3f);
                world.breakBlock(pos, false);
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        Potion elixir = PotionUtil.getPotion(heldItem);
        BlockPattern.Result result;
        result = this.getMonumentPattern().searchAround(world,pos);
    if(result != null){
        if (heldItem.getItem() instanceof PotionItem
                && elixir == ModPotions.ELIXIR_OF_DRAKE) {
            if(!state.get(ChachapoyanIdol.ELDERIAN_MONUMENT)
                    && state.get(ChachapoyanIdol.SCALES)){
                world.setBlockState(pos, state.with(ChachapoyanIdol.ELDERIAN_MONUMENT, true));
                if(!player.isCreative()){
                    heldItem.decrement(1);
                    player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE));
                }
                ItemStack orb = new ItemStack(ModItems.ORB_INFINIUM);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), orb));
                world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL,0.7f, 1.0f);
                return ActionResult.CONSUME;
            }
        } else if (heldItem.getItem() == ModItems.ELDER_GUARDIAN_SCALES) {
            if (!state.get(ChachapoyanIdol.SCALES)
                    && state.get(ChachapoyanIdol.PENDANT)) {
                world.setBlockState(pos, state.with(ChachapoyanIdol.SCALES, true));
                if (!player.isCreative()) {
                    heldItem.decrement(1);
                }
                world.playSound(null, pos, SoundEvents.BLOCK_FLOWERING_AZALEA_PLACE, SoundCategory.NEUTRAL,0.7f, 0.2f);
                return ActionResult.CONSUME;
            }
        } else if (heldItem.getItem() == ModItems.ANKH_PENDANT) {
            if (!state.get(ChachapoyanIdol.PENDANT)
                    && state.get(ChachapoyanIdol.KEY)) {
                world.setBlockState(pos, state.with(ChachapoyanIdol.PENDANT, true));
                if (!player.isCreative()) {
                    heldItem.decrement(1);
                }
                world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.NEUTRAL,0.8f, 0.3f);
                return ActionResult.CONSUME;
            }
        } else if (heldItem.getItem() == ModItems.EVOKER_KEY) {
            if (!state.get(ChachapoyanIdol.KEY)) {
                world.setBlockState(pos, state.with(ChachapoyanIdol.KEY, true));
                world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.NEUTRAL,0.7f, 0.45f);
                return ActionResult.CONSUME;
            }
        }
    }
        if (!world.isClient) {
            player.sendMessage(Text.literal("Full Monument needs to be built first"));
        }

        return ActionResult.PASS;


    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if(!player.isCreative()){
            if(state.get(ChachapoyanIdol.PENDANT)){
                ItemStack pendant = new ItemStack(ModItems.ANKH_PENDANT);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), pendant));
                if(state.get(ChachapoyanIdol.SCALES)){
                    ItemStack scales = new ItemStack(ModItems.ELDER_GUARDIAN_SCALES);
                    world.spawnEntity(new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), scales));
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public BlockPattern getMonumentPattern() {
        if(this.elderianMonumentPatter == null){
            this.elderianMonumentPatter = BlockPatternBuilder.start()
                    .aisle("OIC", "NDN", "~N~")
                    .where('O', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.TOTEM_OF_ORDER)))
                    .where('I', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.CHACHAPOYAN_IDOL)))
                    .where('C', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.TOTEM_OF_CHAOS)))
                    .where('N', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.NENDER_BRICK)))
                    .where('D', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.DIRT)))
                    .where('~', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.AIR)))
                    .build();
        }
        return this.elderianMonumentPatter;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {

        builder.add(FACING, KEY, PENDANT, SCALES, ELDERIAN_MONUMENT);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        SHAPE = Block.createCuboidShape(1, 0, 2, 14, 15.5, 14);

    }
}
