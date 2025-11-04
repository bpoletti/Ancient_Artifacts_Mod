package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static ItemGroup ARTIFACTS;

    public static void registerItemGroup() {
        ARTIFACTS = Registry.register(
                Registries.ITEM_GROUP,
                Identifier.of(AncientArtifacts.MOD_ID, "artifacts"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModBlocks.DRAGON_PEDESTAL))
                        // use a translatable key so you can localize it later
                        .displayName(Text.translatable("Artifacts"))
                        .entries((ctx, entries) -> {
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
                            entries.add(ModBlocks.COPPER_WIRE);

                            //potions
                            entries.add(potionStack(ModPotions.ELIXIR_BASE_I));
                            entries.add(potionStack(ModPotions.ELIXIR_BASE_II));
                            entries.add(potionStack(ModPotions.ELIXIR_BASE_III));
                            entries.add(potionStack(ModPotions.ELIXIR_BASE_IV));
                            entries.add(potionStack(ModPotions.ELIXIR_BASE_V));
                            entries.add(potionStack(ModPotions.ELIXIR_OF_DRAKE));


                            //vanilla items
                            entries.add(Items.ENDER_EYE);
                            entries.add(Items.HEART_OF_THE_SEA);
                            entries.add(Items.DIRT);
                            entries.add(Items.NETHER_STAR);
                            entries.add(potionStack(Potions.AWKWARD));
                        })
                        .build()
        );
    }
    private static ItemStack potionStack(RegistryEntry<Potion> potionEntry) {
        return PotionContentsComponent.createStack(Items.POTION, potionEntry);
    }

    // Overload if you still have raw Potion instances:
    private static ItemStack potionStack(Potion potion) {
        return potionStack(Registries.POTION.getEntry(potion));
    }
}
