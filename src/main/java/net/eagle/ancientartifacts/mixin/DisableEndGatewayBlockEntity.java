package net.eagle.ancientartifacts.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndGatewayBlockEntity.class)
public abstract class DisableEndGatewayBlockEntity {
    @Inject(method = "tryTeleportingEntity", at = @At(value = "HEAD"), cancellable = true)
    private static void disableEndGateway(World world, BlockPos pos, BlockState state, Entity entity, EndGatewayBlockEntity blockEntity, CallbackInfo ci) {
        if (!isArtifactComplete()) {
            ci.cancel();
        }

    }



    private static boolean isArtifactComplete() {
        // Check for the completion of ancient artifacts
        // Return true if the ancient artifact is complete and false otherwise
        return false;
    }
}