package net.eagle.ancientartifacts.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public abstract class DisabledEndPortalBlock {
    @Inject(method = "onEntityCollision", at = @At(value = "HEAD"), cancellable = true)
    private void disableEndPortal(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (!isArtifactComplete()) {
            ci.cancel();
        }
    }



    private boolean isArtifactComplete() {
        // Check for the completion of ancient artifacts
        // Return true if the ancient artifact is complete and false otherwise
        return false;
    }
}
