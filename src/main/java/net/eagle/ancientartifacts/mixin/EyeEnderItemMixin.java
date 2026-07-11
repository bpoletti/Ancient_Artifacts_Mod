package net.eagle.ancientartifacts.mixin;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.custom.ChachapoyanIdol;
import net.eagle.ancientartifacts.block.custom.DragonPedestal;
import net.eagle.ancientartifacts.block.custom.EtherLever;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public abstract class EyeEnderItemMixin {

    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos blockPos;
        Level world = context.getLevel();
        BlockState blockState = world.getBlockState(blockPos = context.getClickedPos());
        Block idol = ModBlocks.CHACHAPOYAN_IDOL;
        BooleanProperty elderianMonument = ChachapoyanIdol.ELDERIAN_MONUMENT;
        boolean foundIdol = canUse(blockPos, world, idol, elderianMonument);

        Block lever = ModBlocks.ETHER_LEVER;
        BooleanProperty etherOn = EtherLever.POWERED;
        boolean foundEtherLever = canUse(blockPos, world, lever, etherOn);

        Block pedestal = ModBlocks.DRAGON_PEDESTAL;
        BooleanProperty pedestalInf = DragonPedestal.END_READY;
        boolean foundPedestal = canUse(blockPos, world, pedestal, pedestalInf);

        if(!ritualComplete(foundIdol, foundEtherLever, foundPedestal) && blockState.is(Blocks.END_PORTAL_FRAME)){
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }

    public boolean canUse(BlockPos pos, Level world, Block block, BooleanProperty property) {
        boolean foundBlock = false;

        for (int x = pos.getX() - 12; x <= pos.getX() + 12; x++) {
            for (int y = pos.getY() - 7; y <= pos.getY() + 4; y++) {
                for (int z = pos.getZ() - 12; z <= pos.getZ() + 12; z++) {
                    BlockState state = world.getBlockState(new BlockPos(x, y, z));
                    if (state.getBlock() == block) {
                        if(state.getValue(property)){
                            foundBlock = true;
                            break;
                        }
                    }
                }
                if (foundBlock) {
                    break;
                }
            }
            if (foundBlock) {
                break;
            }
        }

        return foundBlock;
    }

    public boolean ritualComplete(boolean isIdol, boolean isLever, boolean isPedestal){
        return isIdol && isLever && isPedestal;
    }
}