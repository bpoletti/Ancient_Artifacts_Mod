package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider wrapperLookup, @NonNull Consumer<AdvancementHolder> consumer) {

        HolderGetter<Item> itemLookup = wrapperLookup.lookupOrThrow(Registries.ITEM);
        HolderGetter<Block> blockLookup = wrapperLookup.lookupOrThrow(Registries.BLOCK);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        ModBlocks.CHACHAPOYAN_IDOL.asItem(), // Restored to your working state
                        Component.literal("Not Today Dr. Jones!"),
                        Component.literal("Found the Chachapoyan Idol"),
                        Identifier.fromNamespaceAndPath("ancientartifacts", "block/nender_brick"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("golden_head",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CHACHAPOYAN_IDOL.asItem()))
                .save(consumer, "ancientartifacts:root");

        AdvancementHolder key_to_everything = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.EVOKER_KEY, // Restored
                        Component.literal("The Key to Everything!"),
                        Component.literal("Found the Evoker's Key"),
                        Identifier.fromNamespaceAndPath("ancientartifacts", "block/nender_brick"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("key_nabbed",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.EVOKER_KEY))
                .save(consumer, "ancientartifacts:evoker_key");

        AdvancementHolder ball_of_stars = Advancement.Builder.advancement()
                .parent(key_to_everything)
                .display(
                        ModItems.FIREFLY_ORB, // Restored
                        Component.literal("Ball of Stars"),
                        Component.literal("Crafted the Firefly Orb"),
                        Identifier.fromNamespaceAndPath("ancientartifacts", "block/nender_brick"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("star_orb",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FIREFLY_ORB))
                .save(consumer, "ancientartifacts:firefly_orb");

        ItemPredicate itemPredicate = ItemPredicate.Builder.item()
                .of(itemLookup, Items.POTION)
                .build();

        AdvancementHolder dragons_potion = Advancement.Builder.advancement()
                .parent(ball_of_stars)
                .display(
                        Items.POTION,
                        Component.literal("Taste Like Crap!"),
                        Component.literal("Brewed the Elixir of Drake"),
                        Identifier.fromNamespaceAndPath("ancientartifacts", "block/nender_brick"),
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion(
                        "drake_potion",
                        InventoryChangeTrigger.TriggerInstance.hasItems(itemPredicate)
                )
                .save(consumer, "ancientartifacts:elixir_of_drake");


        AdvancementHolder magic_staff = Advancement.Builder.advancement()
                .parent(dragons_potion)
                .display(
                        ModItems.END_STAFF, // Restored
                        Component.literal("Wingardium Leviosa"),
                        Component.literal("Crafted the End Staff"),
                        Identifier.fromNamespaceAndPath("ancientartifacts", "block/nender_brick"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("end_staff",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.END_STAFF))
                .save(consumer, "ancientartifacts:end_staff");

        AdvancementHolder end_gate_activation = Advancement.Builder.advancement()
                .parent(magic_staff)
                .display(
                        ModItems.ORB_INFINIUM, // Restored
                        Component.literal("§5The Beginning of the End?"),
                        Component.literal("The Elderian Monument was activated and the End Gate has opened"),
                        Identifier.fromNamespaceAndPath("ancientartifacts", "block/nender_brick"),
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("pedestal_final",
                        ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location()
                                        .setBlock(BlockPredicate.Builder.block()
                                                .of(blockLookup, ModBlocks.DRAGON_PEDESTAL)),
                                ItemPredicate.Builder.item()
                                        .of(itemLookup, ModItems.END_STAFF)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(500).build())
                .save(consumer, "ancientartifacts:monument_opened");
    }
}