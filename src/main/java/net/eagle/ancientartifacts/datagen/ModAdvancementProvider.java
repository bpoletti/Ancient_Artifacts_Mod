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
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.block.Block;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {

        RegistryEntryLookup<Item> itemLookup = wrapperLookup.getOrThrow(RegistryKeys.ITEM);
        RegistryEntryLookup<Block> blockLookup = wrapperLookup.getOrThrow(RegistryKeys.BLOCK);

        AdvancementEntry root = Advancement.Builder.create()
                .display(
                        ModBlocks.CHACHAPOYAN_IDOL.asItem(),
                        Text.literal("Not Today Dr. Jones!"),
                        Text.literal("Found the Chachapoyan Idol"),
                        Identifier.of("ancientartifacts", "textures/block/nender_brick.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("golden_head",
                        InventoryChangedCriterion.Conditions.items(ModBlocks.CHACHAPOYAN_IDOL.asItem()))
                .build(consumer, "ancientartifacts/root");

        AdvancementEntry key_to_everything = Advancement.Builder.create()
                .parent(root)
                .display(
                        ModItems.EVOKER_KEY,
                        Text.literal("The Key to Everything!"),
                        Text.literal("Found the Evoker's Key"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
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
                        true,
                        true,
                        false
                )
                .criterion("star_orb",
                        InventoryChangedCriterion.Conditions.items(ModItems.FIREFLY_ORB))
                .build(consumer, "ancientartifacts/firefly_orb");

        RegistryEntry<Potion> drakePotionEntry = Registries.POTION.getEntry(ModPotions.ELIXIR_OF_DRAKE);

        PotionContentsComponent potionContents = new PotionContentsComponent(drakePotionEntry);
        ItemStack customPotionStack = new ItemStack(Items.POTION);
        customPotionStack.set(DataComponentTypes.POTION_CONTENTS, potionContents);

        ComponentMapPredicate componentMap = ComponentMapPredicate.builder()
                .add(DataComponentTypes.POTION_CONTENTS, potionContents)
                .build();

        ComponentsPredicate potionPredicate = ComponentsPredicate.Builder.create()
                .exact(componentMap)
                .build();

        ItemPredicate itemPredicate = ItemPredicate.Builder.create()
                .items(itemLookup, Items.POTION)
                .components(potionPredicate)
                .build();

        AdvancementEntry dragons_potion = Advancement.Builder.create()
                .parent(ball_of_stars)
                .display(
                        customPotionStack.getItem(),
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
                        true,
                        true,
                        false
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
                        true,
                        true,
                        false
                )
                .criterion("pedestal_final",
                        ItemCriterion.Conditions.createItemUsedOnBlock(
                                LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create()
                                                .blocks(blockLookup, ModBlocks.DRAGON_PEDESTAL)),
                                ItemPredicate.Builder.create()
                                        .items(itemLookup, ModItems.END_STAFF)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(500).build())
                .build(consumer, "ancientartifacts/monument_opened");
    }
}