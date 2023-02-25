package net.eagle.ancientartifacts.item.custom;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.custom.BlockA;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemC extends Item {
    public ItemC(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.isOf(ModBlocks.BLOCK_A) && !(Boolean) blockState.get(BlockA.BLOCK_A_V3)) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                BlockState blockState2 = blockState.with(BlockA.BLOCK_A_V3, true);
                world.setBlockState(blockPos, blockState2);
                return ActionResult.CONSUME;
            }
        } else {
            return ActionResult.PASS;
        }
    }
}
