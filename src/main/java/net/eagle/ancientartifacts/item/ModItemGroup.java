package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup ARTIFACTS = FabricItemGroupBuilder.build(
            new Identifier(AncientArtifacts.MOD_ID, "dragon_pedestal"), () -> new ItemStack(ModBlocks.DRAGON_PEDESTAL));
}
