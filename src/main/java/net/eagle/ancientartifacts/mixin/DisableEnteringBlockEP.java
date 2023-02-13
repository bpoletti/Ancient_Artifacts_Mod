package net.eagle.ancientartifacts.mixin;

import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnterBlockCriterion.class)
public abstract class DisableEnteringBlockEP {
    @Inject(method = "trigger", at = @At(value = "HEAD"), cancellable = true)
    private void preventGatewayAdvancement(ServerPlayerEntity player, BlockState state, CallbackInfo ci) {
        if (state.getBlock() == Blocks.END_GATEWAY && !isArtifactComplete()) {
            ci.cancel();
        }
    }

    private boolean isArtifactComplete() {
        // Check for the completion of ancient artifacts
        // Return true if the ancient artifact is complete and false otherwise
        return false;
    }
}