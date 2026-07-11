package net.eagle.ancientartifacts.mixin;

import net.eagle.ancientartifacts.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {

    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        assert player != null;
        InteractionHand hand = context.getHand();
        ItemStack stack = player.getItemInHand(hand);

        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if (state.is(Blocks.MYCELIUM)) {
            // Create a new ItemStack of dust and drop it
            double rand = Math.random(); //spawns dust 20 percent of the time
            if (rand < 0.10) {
                ItemStack dust = new ItemStack(ModItems.MYCELIUM_DUST, 1);
                BlockPos dropPos = pos.above();
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5, dust));
            } else {
                ItemStack dirt = new ItemStack(Blocks.DIRT, 1);
                BlockPos dropPos = pos.above();
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5, dirt));
            }
            // Consume the ShovelItem's durability and cancel the event
            ShovelItem item = (ShovelItem) stack.getItem();
            if (!player.isCreative()) {
                item.mineBlock(stack, world, state, pos, player);
            }
            BlockState dirt = Blocks.DIRT.defaultBlockState();
            world.setBlockAndUpdate(pos, dirt);
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
        else if (state.is(Blocks.RED_SAND)) {
            double rand = Math.random();
            if(rand < 0.05) {
                ItemStack ice = new ItemStack(ModItems.RED_ICE, 1);
                BlockPos dropPos = pos.above();
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , ice));
            } else {
                ItemStack bush = new ItemStack(Items.DEAD_BUSH, 1);
                BlockPos dropPos = pos.above();
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , bush));
            }
            // Consume the ShovelItem's durability and cancel the event
            ShovelItem item = (ShovelItem)stack.getItem();
            if(!player.isCreative()){
                item.mineBlock(stack, world, state, pos, player);
            }
            BlockState sand = Blocks.SAND.defaultBlockState();
            world.setBlockAndUpdate(pos, sand);
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
    }
}