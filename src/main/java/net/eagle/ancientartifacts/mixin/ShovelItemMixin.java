package net.eagle.ancientartifacts.mixin;

import net.eagle.ancientartifacts.item.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {
    // Shadow the useOnBlock method

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        // create a set to keep track of already dug blocks

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        assert player != null;
        ItemStack stack = player.getStackInHand(hand);
        // Check if the block being right-clicked is mycelium
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.MYCELIUM) {

            // Create a new ItemStack of dust and drop it
            double rand = Math.random(); //spawns dust 20 percent of the time
            if(rand < 0.10) {
                ItemStack dust = new ItemStack(ModItems.MYCELIUM_DUST, 1);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , dust));
            } else {
                ItemStack dirt = new ItemStack(Blocks.DIRT, 1);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , dirt));
            }
            // Consume the ShovelItem's durability and cancel the event
            ShovelItem item = (ShovelItem)stack.getItem();
            if(!player.isCreative()){
                item.postMine(stack, world, state, pos, player);
            }
            BlockState dirt = Blocks.DIRT.getDefaultState();
            world.setBlockState(pos, dirt);
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }
        else if (state.getBlock() == Blocks.RED_SAND) {
            double rand = Math.random();
            if(rand < 0.05) {
                ItemStack ice = new ItemStack(ModItems.RED_ICE, 1);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , ice));
            } else {
                ItemStack bush = new ItemStack(Items.DEAD_BUSH, 1);
                BlockPos dropPos = pos.up();
                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , bush));
            }
            // Consume the ShovelItem's durability and cancel the event
            ShovelItem item = (ShovelItem)stack.getItem();
            if(!player.isCreative()){
                item.postMine(stack, world, state, pos, player);
            }
            BlockState sand = Blocks.SAND.getDefaultState();
            world.setBlockState(pos, sand);
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }
    }
}

