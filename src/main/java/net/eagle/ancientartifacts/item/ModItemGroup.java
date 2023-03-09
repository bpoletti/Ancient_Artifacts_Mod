package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static ItemGroup ARTIFACTS;

    public static void registerItemGroup() {
        ARTIFACTS = FabricItemGroup.builder(new Identifier((AncientArtifacts.MOD_ID), "dragon_pedestal"))
                .displayName(Text.literal("Artifacts Itm Group"))
                .icon(() -> new ItemStack(ModBlocks.DRAGON_PEDESTAL)).build();
    }

}
