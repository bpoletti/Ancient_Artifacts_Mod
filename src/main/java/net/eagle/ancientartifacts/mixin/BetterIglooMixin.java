package net.eagle.ancientartifacts.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.structures.IglooPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IglooPieces.class)
public class BetterIglooMixin {

    private static final Identifier TOP_TEMPLATE = Identifier.fromNamespaceAndPath("minecraft", "igloo/top");
    private static final Identifier MIDDLE_TEMPLATE = Identifier.fromNamespaceAndPath("minecraft", "igloo/middle");
    private static final Identifier BOTTOM_TEMPLATE = Identifier.fromNamespaceAndPath("minecraft", "igloo/bottom");

    @Inject(method = "addPieces", at = @At("HEAD"), cancellable = true)
    private static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation,
                                  StructurePieceAccessor holder, RandomSource random, CallbackInfo ci) {
        if (random.nextDouble() < 0.9) {
            int i = random.nextInt(8) + 4;
            // IglooGenerator.Piece is now IglooPieces.IglooPiece
            holder.addPiece(new IglooPieces.IglooPiece(manager, BOTTOM_TEMPLATE, pos, rotation, i * 3));
            for (int j = 0; j < i - 1; ++j) {
                holder.addPiece(new IglooPieces.IglooPiece(manager, MIDDLE_TEMPLATE, pos, rotation, j * 3));
            }
        }
        holder.addPiece(new IglooPieces.IglooPiece(manager, TOP_TEMPLATE, pos, rotation, 0));

        ci.cancel();
    }
}