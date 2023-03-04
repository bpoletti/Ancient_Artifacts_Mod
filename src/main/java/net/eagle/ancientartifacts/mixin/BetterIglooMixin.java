package net.eagle.ancientartifacts.mixin;

import net.minecraft.structure.IglooGenerator;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(IglooGenerator.class)
public class BetterIglooMixin {
    private static final Identifier TOP_TEMPLATE = new Identifier("igloo/top");
    private static final Identifier MIDDLE_TEMPLATE = new Identifier("igloo/middle");
    private static final Identifier BOTTOM_TEMPLATE = new Identifier("igloo/bottom");

    @Inject(at = @At("HEAD"), method = "addPieces", cancellable = true)
    private static void addPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder holder, Random random, CallbackInfo ci) {
        if (random.nextDouble() < 0.9) {
            int i = random.nextInt(8) + 4;
            holder.addPiece(new IglooGenerator.Piece(manager, BOTTOM_TEMPLATE, pos, rotation, i * 3));
            for (int j = 0; j < i - 1; ++j) {
                holder.addPiece(new IglooGenerator.Piece(manager, MIDDLE_TEMPLATE, pos, rotation, j * 3));
            }
        }
        holder.addPiece(new IglooGenerator.Piece(manager, TOP_TEMPLATE, pos, rotation, 0));
    }
}
