package net.eagle.ancientartifacts.util;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public final class ModLootTableModifies {

    private ModLootTableModifies() {} // no instances

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((
                RegistryKey<LootTable> key,
                LootTable.Builder tableBuilder,
                LootTableSource source,
                RegistryWrapper.WrapperLookup lookup
        ) -> {
            final String id = key.getValue().toString();

            switch (id) {
                // ==== MOBS ====
                case "minecraft:entities/warden" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.WARDEN_HEART).weight(9))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:entities/evoker" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.EVOKER_KEY).weight(3))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:entities/elder_guardian" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_SCALES).weight(7))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }

                // ==== STRUCTURE CHESTS ====
                case "minecraft:chests/igloo_chest" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.90f))
                            .with(ItemEntry.builder(ModItems.BLACK_ICE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/abandoned_mineshaft" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.40f))
                            .with(ItemEntry.builder(ModItems.DRAGON_FOSSIL))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/bastion_hoglin_stable", "minecraft:chests/bastion_other" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.40f))
                            .with(ItemEntry.builder(ModBlocks.GILDED_PLATE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/bastion_treasure" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.70f))
                            .with(ItemEntry.builder(ModBlocks.GILDED_PLATE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/desert_pyramid" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.40f))
                            .with(ItemEntry.builder(ModItems.ANKH_PENDANT))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/jungle_temple" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.70f))
                            .with(ItemEntry.builder(ModBlocks.CHACHAPOYAN_IDOL))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/nether_bridge" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.85f))
                            .with(ItemEntry.builder(ModBlocks.NENDER_BRICK))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 7.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/pillager_outpost" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.65f))
                            .with(ItemEntry.builder(ModBlocks.TOTEM_OF_CHAOS))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/ruined_portal" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.40f))
                            .with(ItemEntry.builder(ModItems.NETHER_GRASS))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/simple_dungeon" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.30f))
                            .with(ItemEntry.builder(ModBlocks.DRAGON_PEDESTAL))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/stronghold_corridor" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.80f))
                            .with(ItemEntry.builder(ModItems.ENDER_ROD))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/woodland_mansion" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.60f))
                            .with(ItemEntry.builder(ModItems.EVOKER_KEY))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }

                // ==== VILLAGES ====
                case "minecraft:chests/village/village_armorer" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.80f))
                            .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/village/village_desert_house",
                     "minecraft:chests/village/village_plains_house",
                     "minecraft:chests/village/village_savanna_house",
                     "minecraft:chests/village/village_snowy_house",
                     "minecraft:chests/village/village_taiga_house" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.75f))
                            .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/village/village_mason" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.60f))
                            .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/village/village_temple" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(1.00f))
                            .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }
                case "minecraft:chests/village/village_toolsmith",
                     "minecraft:chests/village/village_weaponsmith" -> {
                    LootPool.Builder pool = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(0.50f))
                            .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
                    tableBuilder.pool(pool);
                }

                default -> { /* no-op */ }
            }
        });
    }
}
