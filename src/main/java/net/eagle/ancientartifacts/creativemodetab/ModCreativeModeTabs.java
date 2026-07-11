package net.eagle.ancientartifacts.creativemodetab;


import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionContents;

public class ModCreativeModeTabs {

    public static final CreativeModeTab ARTIFACTS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, "artifacts"),
            FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.DRAGON_PEDESTAL))
                    .title(Component.translatable("creativemodetab.ancientartifacts.artifacts"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ANKH_PENDANT);
                        output.accept(ModItems.EVOKER_KEY);
                        output.accept(ModItems.ELDER_GUARDIAN_SCALES);
                        output.accept(ModItems.BLACK_ICE);
                        output.accept(ModItems.RED_ICE);
                        output.accept(ModItems.DRAGON_FOSSIL);
                        output.accept(ModBlocks.DRAGON_PEDESTAL);
                        output.accept(ModItems.END_STAFF);
                        output.accept(ModItems.ENDER_ROD);
                        output.accept(ModItems.PEARLESCENT_FIREFLY_BUD);
                        output.accept(ModItems.OCHRE_FIREFLY_BUD);
                        output.accept(ModItems.VERDANT_FIREFLY_BUD);
                        output.accept(ModItems.FIREFLY_ORB);

                        output.accept(ModItems.MYCELIUM_DUST);
                        output.accept(ModItems.NETHER_GRASS);

                        output.accept(ModBlocks.CHACHAPOYAN_IDOL);
                        output.accept(ModBlocks.TOTEM_OF_ORDER);
                        output.accept(ModBlocks.TOTEM_OF_CHAOS);
                        output.accept(ModItems.ORB_INFINIUM);

                        output.accept(ModItems.WARDEN_HEART);
                        output.accept(ModBlocks.NENDER_BRICK);
                        output.accept(ModBlocks.GILDED_PLATE);

                        output.accept(ModBlocks.ETHER_LEVER);
                        output.accept(ModBlocks.COPPER_WIRE);

                        //potions
                        output.accept(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_I)));
                        output.accept(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_II)));
                        output.accept(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_III)));
                        output.accept(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_IV)));
                        output.accept(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_V)));
                        output.accept(PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_OF_DRAKE)));


                        //vanilla items
                        output.accept(Items.ENDER_EYE);
                        output.accept(Items.HEART_OF_THE_SEA);
                        output.accept(Items.DIRT);
                        output.accept(Items.NETHER_STAR);
                        output.accept(PotionContents.createItemStack(Items.POTION ,BuiltInRegistries.POTION.wrapAsHolder(Potions.AWKWARD.value())));
                    })
                    .build());

    public static void registerModCreativeTab() {
        AncientArtifacts.LOGGER.info("Registering Creative Mode Tabs for " + AncientArtifacts.MOD_ID);
    }
}
