package net.eagle.ancientartifacts.mixin;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.custom.CopperWire;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public abstract class CopperToRedstoneWire {

    @Inject(
            method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isSignalSource()Z"),
            cancellable = true
    )
    private static void shouldConnectTo(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir){
        if (dir != null && state.is(ModBlocks.COPPER_WIRE)) {
            cir.setReturnValue(state.getValue(CopperWire.FACING) == dir);
        }
    }
}