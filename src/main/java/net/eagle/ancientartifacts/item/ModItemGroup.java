package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static ItemGroup ARTIFACTS = Registry.register(Registries.ITEM_GROUP, new Identifier(AncientArtifacts.MOD_ID, "artifacts"),
            FabricItemGroup.builder()
                    .displayName(Text.literal("Artifacts Itm Group"))
                    .icon(() -> new ItemStack(ModBlocks.DRAGON_PEDESTAL)).entries((displayContext, entries) -> {
                        entries.add(ModItems.ANKH_PENDANT);
                        entries.add(ModItems.EVOKER_KEY);
                        entries.add(ModItems.ELDER_GUARDIAN_SCALES);

                        entries.add(ModItems.BLACK_ICE);
                        entries.add(ModItems.RED_ICE);

                        entries.add(ModItems.DRAGON_FOSSIL);
                        entries.add(ModBlocks.DRAGON_PEDESTAL);


                        entries.add(ModItems.END_STAFF);
                        entries.add(ModItems.ENDER_ROD);


                        entries.add(ModItems.PEARLESCENT_FIREFLY_BUD);
                        entries.add(ModItems.OCHRE_FIREFLY_BUD);
                        entries.add(ModItems.VERDANT_FIREFLY_BUD);
                        entries.add(ModItems.FIREFLY_ORB);

                        entries.add(ModItems.MYCELIUM_DUST);
                        entries.add(ModItems.NETHER_GRASS);

                        entries.add(ModBlocks.CHACHAPOYAN_IDOL);
                        entries.add(ModBlocks.TOTEM_OF_ORDER);
                        entries.add(ModBlocks.TOTEM_OF_CHAOS);
                        entries.add(ModItems.ORB_INFINIUM);

                        entries.add(ModItems.WARDEN_HEART);
                        entries.add(ModBlocks.NENDER_BRICK);
                        entries.add(ModBlocks.GILDED_PLATE);

                        entries.add(ModBlocks.ETHER_LEVER);





                    }).build());

    public static void registerItemGroup() {
    }

}
