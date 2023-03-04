package net.eagle.ancientartifacts.mixin;

import net.eagle.ancientartifacts.item.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemUsageContext;
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
public class ShovelItemMixin {

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
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
            ItemStack dust = new ItemStack(ModItems.MYCELIUM_DUST, 1);
            BlockPos dropPos = pos.up();
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , dust));
            // Consume the ShovelItem's durability and cancel the event
            ShovelItem item = (ShovelItem)stack.getItem();
            if(!player.isCreative()){
                item.postMine(stack, world, state, pos, player);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }
        else if (state.getBlock() == Blocks.RED_SAND) {
            // Create a new ItemStack of dust and drop it
            ItemStack ice = new ItemStack(ModItems.RED_ICE, 1);
            BlockPos dropPos = pos.up();
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, dropPos.getY(), pos.getZ() + 0.5 , ice));
            // Consume the ShovelItem's durability and cancel the event
            ShovelItem item = (ShovelItem)stack.getItem();
            if(!player.isCreative()){
                item.postMine(stack, world, state, pos, player);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }
    }
}

