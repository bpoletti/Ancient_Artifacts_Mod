package net.eagle.ancientartifacts.block.custom;

import com.mojang.serialization.MapCodec;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPattern.*;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChachapoyanIdol extends HorizontalDirectionalBlock {

    public static final MapCodec<ChachapoyanIdol> CODEC = simpleCodec(ChachapoyanIdol::new);

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty KEY = BooleanProperty.create("key");
    public static final BooleanProperty PENDANT = BooleanProperty.create("pendant");
    public static final BooleanProperty SCALES = BooleanProperty.create("scales");
    public static final BooleanProperty ELDERIAN_MONUMENT = BooleanProperty.create("elderian_monument");

    private static final VoxelShape SHAPE;
    private BlockPattern elderianMonumentPatternOC; // O ^ C
    private BlockPattern elderianMonumentPatternCO; // C ^ O

    public ChachapoyanIdol(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(KEY, false)
                .setValue(PENDANT, false)
                .setValue(SCALES, false)
                .setValue(ELDERIAN_MONUMENT, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        level.playSound(
                null,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.ANVIL_PLACE,
                SoundSource.BLOCKS, 0.2f, 0.4f
        );
        level.playSound(
                null,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.ANVIL_PLACE,
                SoundSource.BLOCKS, 0.4f, 0.4f
        );

        level.setBlock(pos, state, 3);

        if (!level.isClientSide()) {
            int radius = 16;
            boolean creeperExploded = false;

            // Box is now AABB (Axis-Aligned Bounding Box), expand is inflate
            for (Creeper entity : level.getEntitiesOfClass(Creeper.class, new AABB(pos).inflate(radius), e -> true)) {
                entity.ignite();
                creeperExploded = true;
            }
            if (creeperExploded) {
                level.playSound(
                        null,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.TRIDENT_THUNDER,
                        SoundSource.NEUTRAL, 1.0f, 0.3f
                );
                level.destroyBlock(pos, false);
            }
        }
        super.setPlacedBy(level, pos, state, placer, itemStack);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {

        BlockPatternMatch result = findMonument(level, pos);
        if (result == null) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.literal("Full Monument needs to be built first"));
            }
            return InteractionResult.PASS;
        }

        // Registries -> BuiltInRegistries
        final String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        switch (id) {
            case "minecraft:potion" -> {
                // DataComponentTypes -> DataComponents
                var contents = stack.get(DataComponents.POTION_CONTENTS);
                if (contents != null && contents.potion().isPresent() && contents.potion().get().value().equals(ModPotions.ELIXIR_OF_DRAKE)) {

                    if (!state.getValue(ChachapoyanIdol.ELDERIAN_MONUMENT)
                            && state.getValue(ChachapoyanIdol.SCALES)) {

                        level.setBlock(pos, state.setValue(ChachapoyanIdol.ELDERIAN_MONUMENT, true), 3);

                        if (!player.isCreative()) {
                            stack.shrink(1);
                            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                        }

                        BlockPos dropPos = pos.above();
                        level.addFreshEntity(new ItemEntity(level,
                                dropPos.getX(), dropPos.getY(), dropPos.getZ(),
                                new ItemStack(ModItems.ORB_INFINIUM)));

                        level.playSound(
                                null,
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                SoundEvents.PLAYER_LEVELUP,
                                SoundSource.NEUTRAL, 0.7f, 1.0f
                        );
                        return InteractionResult.CONSUME;
                    }
                }
                return InteractionResult.PASS;
            }

            case "ancientartifacts:elder_guardian_scales" -> {
                if (!state.getValue(ChachapoyanIdol.SCALES)
                        && state.getValue(ChachapoyanIdol.PENDANT)) {
                    level.setBlock(pos, state.setValue(ChachapoyanIdol.SCALES, true), 3);
                    if (!player.isCreative()) stack.shrink(1);

                    level.playSound(
                            null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            SoundEvents.FLOWERING_AZALEA_PLACE, SoundSource.NEUTRAL, 0.7f, 0.2f
                    );
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            case "ancientartifacts:ankh_pendant" -> {
                if (!state.getValue(ChachapoyanIdol.PENDANT)
                        && state.getValue(ChachapoyanIdol.KEY)) {
                    level.setBlock(pos, state.setValue(ChachapoyanIdol.PENDANT, true), 3);
                    if (!player.isCreative()) stack.shrink(1);

                    level.playSound(
                            null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.NEUTRAL, 0.8f, 0.3f
                    );
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            case "ancientartifacts:evoker_key" -> {
                if (!state.getValue(ChachapoyanIdol.KEY)) {
                    level.setBlock(pos, state.setValue(ChachapoyanIdol.KEY, true), 3);
                    level.playSound(
                            null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            SoundEvents.IRON_DOOR_OPEN, SoundSource.NEUTRAL, 0.7f, 0.45f
                    );
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }

            default -> {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (!player.isCreative()) {
            if (state.getValue(ChachapoyanIdol.PENDANT)) {
                ItemStack pendant = new ItemStack(ModItems.ANKH_PENDANT);
                BlockPos dropPos = pos.above();
                level.addFreshEntity(new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), pendant));
                if (state.getValue(ChachapoyanIdol.SCALES)) {
                    ItemStack scales = new ItemStack(ModItems.ELDER_GUARDIAN_SCALES);
                    level.addFreshEntity(new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), scales));
                }
            }
        }
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    private BlockPattern getMonumentPatternOC() {
        if (this.elderianMonumentPatternOC == null) {
            this.elderianMonumentPatternOC = BlockPatternBuilder.start()
                    .aisle("O^C",
                            "NDN",
                            "~N~")
                    .where('O', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.TOTEM_OF_ORDER)))
                    .where('C', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.TOTEM_OF_CHAOS)))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.CHACHAPOYAN_IDOL)))
                    .where('N', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.NENDER_BRICK)))
                    .where('D', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.DIRT)))
                    .where('~', pos -> pos.getState().isAir())
                    .build();
        }
        return this.elderianMonumentPatternOC;
    }

    private BlockPattern getMonumentPatternCO() {
        if (this.elderianMonumentPatternCO == null) {
            this.elderianMonumentPatternCO = BlockPatternBuilder.start()
                    .aisle("C^O",
                            "NDN",
                            "~N~")
                    .where('O', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.TOTEM_OF_ORDER)))
                    .where('C', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.TOTEM_OF_CHAOS)))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.CHACHAPOYAN_IDOL)))
                    .where('N', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.NENDER_BRICK)))
                    .where('D', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.DIRT)))
                    .where('~', pos -> pos.getState().isAir())
                    .build();
        }
        return this.elderianMonumentPatternCO;
    }

    @Nullable
    private BlockPatternMatch findMonument(Level level, BlockPos pos) {
        BlockPatternMatch res = this.getMonumentPatternOC().find(level, pos);
        if (res == null) {
            res = this.getMonumentPatternCO().find(level, pos);
        }
        return res;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, KEY, PENDANT, SCALES, ELDERIAN_MONUMENT);
    }

    static {
        SHAPE = Block.box(1, 0, 2, 14, 15.5, 14);
    }
}