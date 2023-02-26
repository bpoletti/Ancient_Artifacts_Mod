package net.eagle.ancientartifacts.block.custom;

import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
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
import org.jetbrains.annotations.Nullable;

public class ChachapoyanIdol extends HorizontalFacingBlock {

    public static final DirectionProperty FACING;

    private static VoxelShape SHAPE;

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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        Potion elixir = PotionUtil.getPotion(heldItem);
        if (heldItem.getItem() instanceof PotionItem
                && elixir == ModPotions.ELIXIR_OF_DRAKE) {
            if(!state.get(ChachapoyanIdol.ELDERIAN_MONUMENT)
                    && state.get(ChachapoyanIdol.SCALES)){
                world.setBlockState(pos, state.with(ChachapoyanIdol.ELDERIAN_MONUMENT, true));
                if(!player.isCreative()){
                    heldItem.decrement(1);
                }
                ItemStack orb = new ItemStack(ModItems.ORB_INFINIUM);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), orb));
                return ActionResult.CONSUME;
            }
        } else if (heldItem.getItem() == ModItems.ELDER_GUARDIAN_SCALES) {
            if (!state.get(ChachapoyanIdol.SCALES)
                    && state.get(ChachapoyanIdol.PENDANT)) {
                world.setBlockState(pos, state.with(ChachapoyanIdol.SCALES, true));
                if (!player.isCreative()) {
                    heldItem.decrement(1);
                }
                return ActionResult.CONSUME;
            }
        } else if (heldItem.getItem() == ModItems.ANKH_PENDANT) {
            if (!state.get(ChachapoyanIdol.PENDANT)
                    && state.get(ChachapoyanIdol.KEY)) {
                world.setBlockState(pos, state.with(ChachapoyanIdol.PENDANT, true));
                if (!player.isCreative()) {
                    heldItem.decrement(1);
                }
                return ActionResult.CONSUME;
            }
        } else if (heldItem.getItem() == ModItems.EVOKER_KEY) {
            if (!state.get(ChachapoyanIdol.KEY)) {
                world.setBlockState(pos, state.with(ChachapoyanIdol.KEY, true));
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;


    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
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

        builder.add(FACING, KEY, PENDANT, SCALES, ELDERIAN_MONUMENT);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        SHAPE = Block.createCuboidShape(1, 0, 2, 14, 15.5, 14);

    }
}
