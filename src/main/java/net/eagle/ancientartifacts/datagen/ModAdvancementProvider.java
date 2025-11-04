package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.component.DataComponentTypes;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry root = Advancement.Builder.create()
                .display(
                        ModBlocks.CHACHAPOYAN_IDOL.asItem(),              // icon must be an item
                        Text.literal("Not Today Dr. Jones!"),
                        Text.literal("Found the Chachapoyan Idol"),
                        Identifier.of("ancientartifacts", "textures/block/nender_brick.png"),
                        AdvancementFrame.TASK,
                        true,   // show toast
                        true,   // announce to chat
                        false   // hidden
                )
                .criterion("golden_head",
                        InventoryChangedCriterion.Conditions.items(ModBlocks.CHACHAPOYAN_IDOL.asItem()))
                // hand the built advancement to the sink:
                .build(consumer, "ancientartifacts/root");

        AdvancementEntry key_to_everything = Advancement.Builder.create()
                .parent(root)
                .display(
                        ModItems.EVOKER_KEY,
                        Text.literal("The Key to Everything!"),
                        Text.literal("Found the Evoker's Key"),
                        null,
                        AdvancementFrame.TASK,
                        true,                  // show_toast
                        true,                  // announce_to_chat
                        false                  // hidden
                )
                .criterion("key_nabbed",
                        InventoryChangedCriterion.Conditions.items(ModItems.EVOKER_KEY))
                .build(consumer, "ancientartifacts/evoker_key");

        AdvancementEntry ball_of_stars = Advancement.Builder.create()
                .parent(key_to_everything)
                .display(
                        ModItems.FIREFLY_ORB,
                        Text.literal("Ball of Stars"),
                        Text.literal("Crafted the Firefly Orb"),
                        null,
                        AdvancementFrame.TASK,
                        true,                  // show_toast
                        true,                  // announce_to_chat
                        false                  // hidden
                )
                .criterion("star_orb",
                        InventoryChangedCriterion.Conditions.items(ModItems.FIREFLY_ORB))
                .build(consumer, "ancientartifacts/firefly_orb");

        RegistryEntry<Potion> drakePotionEntry = Registries.POTION.getEntry(ModPotions.ELIXIR_OF_DRAKE);

        PotionContentsComponent potionContents = new PotionContentsComponent(drakePotionEntry);

        // 2. Create the ItemStack using Data Components
        ItemStack customPotionStack = new ItemStack(Items.POTION);
        customPotionStack.set(DataComponentTypes.POTION_CONTENTS, potionContents);

        ComponentPredicate potionPredicate = ComponentPredicate.builder() // Call the static method to get an instance
                .add(DataComponentTypes.POTION_CONTENTS, potionContents)
                .build();

        ItemPredicate itemPredicate = ItemPredicate.Builder.create()
                .items(Items.POTION) // Specify the item type
                .component(potionPredicate) // Add the constructed component predicate
                .build();

        // 2. Build the advancement
        AdvancementEntry dragons_potion = Advancement.Builder.create()
                .parent(ball_of_stars)
                .display(
                        customPotionStack.getItem(), // Item for icon
                        Text.literal("Taste Like Crap!"),
                        Text.literal("Brewed the Elixir of Drake"),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false
                )
                .criterion(
                "drake_potion",
                        InventoryChangedCriterion.Conditions.items(itemPredicate)
        )
                .build(consumer, "ancientartifacts/elixir_of_drake");


        AdvancementEntry magic_staff = Advancement.Builder.create()
                .parent(dragons_potion)
                .display(
                        ModItems.END_STAFF,
                        Text.literal("Wingardium Leviosa"),
                        Text.literal("Crafted the End Staff"),
                        null,
                        AdvancementFrame.TASK,
                        true,                  // show_toast
                        true,                  // announce_to_chat
                        false                  // hidden
                )
                .criterion("end_staff",
                        InventoryChangedCriterion.Conditions.items(ModItems.END_STAFF))
                .build(consumer, "ancientartifacts/end_staff");

        AdvancementEntry end_gate_activation = Advancement.Builder.create()
                .parent(magic_staff)
                .display(
                        ModItems.ORB_INFINIUM,
                        Text.literal("§5The Beginning of the End?"),
                        Text.literal("The Elderian Monument was activated and the End Gate has opened"),
                        null,
                        AdvancementFrame.GOAL,
                        true,                  // show_toast
                        true,                  // announce_to_chat
                        false                  // hidden
                )
                .criterion("pedestal_final",
                        ItemCriterion.Conditions.createItemUsedOnBlock(
                                LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create()
                                                .blocks(ModBlocks.DRAGON_PEDESTAL)),
                                ItemPredicate.Builder.create()
                                        .items(ModItems.END_STAFF)
                        )
                )
                // +500 xp reward
                .rewards(AdvancementRewards.Builder.experience(500).build())
                .build(consumer, "ancientartifacts/monument_opened");
    }
}
