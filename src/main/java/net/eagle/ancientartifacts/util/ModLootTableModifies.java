package net.eagle.ancientartifacts.util;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class ModLootTableModifies {

    private ModLootTableModifies() {}

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((
                ResourceKey<LootTable> key, // Using ResourceKey
                LootTable.Builder tableBuilder,
                LootTableSource source,
                HolderLookup.Provider lookup
        ) -> {
            final String id = key.identifier().toString();

            switch (id) {
                // ==== MOBS ====
                case "minecraft:entities/warden" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .add(LootItem.lootTableItem(ModItems.WARDEN_HEART).setWeight(9))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:entities/evoker" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .add(LootItem.lootTableItem(ModItems.EVOKER_KEY).setWeight(3))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:entities/elder_guardian" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .add(LootItem.lootTableItem(ModItems.ELDER_GUARDIAN_SCALES).setWeight(7))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }

                // ==== STRUCTURE CHESTS ====
                case "minecraft:chests/igloo_chest" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.90f))
                            .add(LootItem.lootTableItem(ModItems.BLACK_ICE))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/abandoned_mineshaft" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.40f))
                            .add(LootItem.lootTableItem(ModItems.DRAGON_FOSSIL))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/bastion_hoglin_stable", "minecraft:chests/bastion_other" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.40f))
                            .add(LootItem.lootTableItem(ModBlocks.GILDED_PLATE))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/bastion_treasure" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.70f))
                            .add(LootItem.lootTableItem(ModBlocks.GILDED_PLATE))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/desert_pyramid" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.40f))
                            .add(LootItem.lootTableItem(ModItems.ANKH_PENDANT))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/jungle_temple" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.70f))
                            .add(LootItem.lootTableItem(ModBlocks.CHACHAPOYAN_IDOL))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/nether_bridge" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.85f))
                            .add(LootItem.lootTableItem(ModBlocks.NENDER_BRICK))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 7.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/pillager_outpost" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.65f))
                            .add(LootItem.lootTableItem(ModBlocks.TOTEM_OF_CHAOS))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/ruined_portal" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.40f))
                            .add(LootItem.lootTableItem(ModItems.NETHER_GRASS))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/simple_dungeon" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.30f))
                            .add(LootItem.lootTableItem(ModBlocks.DRAGON_PEDESTAL))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/stronghold_corridor" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.80f))
                            .add(LootItem.lootTableItem(ModItems.ENDER_ROD))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/woodland_mansion" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.60f))
                            .add(LootItem.lootTableItem(ModItems.EVOKER_KEY))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }

                // ==== VILLAGES ====
                case "minecraft:chests/village/village_armorer" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.80f))
                            .add(LootItem.lootTableItem(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/village/village_desert_house",
                     "minecraft:chests/village/village_plains_house",
                     "minecraft:chests/village/village_savanna_house",
                     "minecraft:chests/village/village_snowy_house",
                     "minecraft:chests/village/village_taiga_house" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.75f))
                            .add(LootItem.lootTableItem(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/village/village_mason" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.60f))
                            .add(LootItem.lootTableItem(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/village/village_temple" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(1.00f))
                            .add(LootItem.lootTableItem(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }
                case "minecraft:chests/village/village_toolsmith",
                     "minecraft:chests/village/village_weaponsmith" -> {
                    LootPool.Builder pool = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0f))
                            .when(LootItemRandomChanceCondition.randomChance(0.50f))
                            .add(LootItem.lootTableItem(ModBlocks.TOTEM_OF_ORDER))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
                    tableBuilder.withPool(pool);
                }

                default -> { /* no-op */ }
            }
        });
    }
}