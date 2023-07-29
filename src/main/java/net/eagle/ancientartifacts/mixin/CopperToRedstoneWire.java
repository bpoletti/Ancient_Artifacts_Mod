package net.eagle.ancientartifacts.mixin;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.custom.CopperWire;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public abstract class CopperToRedstoneWire {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;emitsRedstonePower()Z"), method = "connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z", cancellable = true)
    private static void connectsTo(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir){
        if (dir != null && state.isOf(ModBlocks.COPPER_WIRE)) {
            cir.setReturnValue(state.get(CopperWire.FACING) == dir);
        }
    }
}