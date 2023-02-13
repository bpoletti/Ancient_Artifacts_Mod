package net.eagle.ancientartifacts.mixin;


import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlockEntityRenderer.class)
public abstract class PreventEPBRender<T extends EndPortalBlockEntity> {
    @Inject(method = "render(Lnet/minecraft/block/entity/EndPortalBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "HEAD"), cancellable = true)
    private void disableEndPortalBlockEntity(T endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
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
