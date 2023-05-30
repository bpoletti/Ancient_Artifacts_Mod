package net.eagle.ancientartifacts.util;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifies {

    ////MOBS
    private static final Identifier WARDEN_ID
            = new Identifier("minecraft", "entities/warden");
    private static final Identifier EVOKER_ID
            = new Identifier("minecraft", "entities/evoker");
    private static final Identifier ELDER_GUARDIAN_ID
            = new Identifier("minecraft", "entities/elder_guardian");

    ////STRUCTURE CHEST

    private static final Identifier IGLOO_STRUCTURE_CHEST_ID
            = new Identifier("minecraft", "chests/igloo_chest");
    private static final Identifier ABANDON_MINESHAFT_CHEST_ID
            = new Identifier("minecraft", "chests/abandoned_mineshaft");
    private static final Identifier BASTION_HOGLIN_STABLE_CHEST_ID
            = new Identifier("minecraft", "chests/bastion_hoglin_stable");
    private static final Identifier BASTION_OTHER_CHEST_ID
            = new Identifier("minecraft", "chests/bastion_other");
    private static final Identifier BASTION_TREASURE_ID
            = new Identifier("minecraft", "chests/bastion_treasure");
    private static final Identifier DESERT_PYRAMID_CHEST_ID
            = new Identifier("minecraft", "chests/desert_pyramid");
    private static final Identifier JUNGLE_TEMPLE_CHEST_ID
            = new Identifier("minecraft", "chests/jungle_temple");
    private static final Identifier NETHER_FORTRESS_CHEST_ID
            = new Identifier("minecraft", "chests/nether_bridge");
    private static final Identifier PILLAGER_OUTPOST_CHEST_ID
            = new Identifier("minecraft", "chests/pillager_outpost");
    private static final Identifier RUINED_PORTAL_CHEST_ID
            = new Identifier("minecraft", "chests/ruined_portal");
    private static final Identifier DUNGEON_CHEST_ID
            = new Identifier("minecraft", "chests/simple_dungeon");
    private static final Identifier STRONGHOLD_CORRIDOR_CHEST_ID
            = new Identifier("minecraft", "chests/stronghold_corridor");
    private static final Identifier WOODLAND_MANSION_CHEST_ID
            = new Identifier("minecraft", "chests/woodland_mansion");

    ////VILLAGES
    private static final Identifier ARMORER_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_armorer");
    private static final Identifier DESERT_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_desert_house");
    private static final Identifier MASON_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_mason");
    private static final Identifier PLAINS_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_plains_house");
    private static final Identifier SAVANNA_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_savanna_house");
    private static final Identifier SNOWY_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_snowy_house");
    private static final Identifier TAIGA_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_taiga_house");
    private static final Identifier VILLAGE_TEMPLE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_temple");
    private static final Identifier TOOLSMITH_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_toolsmith");
    private static final Identifier WEAPONSMITH_VILLAGE_CHEST_ID
            = new Identifier("minecraft", "chests/village/village_weaponsmith");


    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            ////MOBS
            if (WARDEN_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.WARDEN_HEART).weight(9))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (EVOKER_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.EVOKER_KEY).weight(3))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (ELDER_GUARDIAN_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_SCALES).weight(7))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            ////STRUCTURES
            if (IGLOO_STRUCTURE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.9f)) // Drops 90% of the time
                        .with(ItemEntry.builder(ModItems.BLACK_ICE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (ABANDON_MINESHAFT_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.60f)) // Drops 60% of the time
                        .with(ItemEntry.builder(ModItems.DRAGON_FOSSIL))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (BASTION_HOGLIN_STABLE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.60f)) // Drops 60% of the time
                        .with(ItemEntry.builder(ModBlocks.GILDED_PLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (BASTION_OTHER_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.50f)) // Drops 50% of the time
                        .with(ItemEntry.builder(ModBlocks.GILDED_PLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (BASTION_TREASURE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.70f)) // Drops 70% of the time
                        .with(ItemEntry.builder(ModBlocks.GILDED_PLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (DESERT_PYRAMID_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.40f)) // Drops 40% of the time
                        .with(ItemEntry.builder(ModItems.ANKH_PENDANT))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (JUNGLE_TEMPLE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.70f)) // Drops 70% of the time
                        .with(ItemEntry.builder(ModBlocks.CHACHAPOYAN_IDOL))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (NETHER_FORTRESS_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.85f)) // Drops 85% of the time
                        .with(ItemEntry.builder(ModBlocks.NENDER_BRICK))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 7.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (PILLAGER_OUTPOST_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.75f)) // Drops 75% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_CHAOS))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (RUINED_PORTAL_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.55f)) // Drops 55% of the time
                        .with(ItemEntry.builder(ModItems.NETHER_GRASS))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (DUNGEON_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.30f)) // Drops 30% of the time
                        .with(ItemEntry.builder(ModBlocks.DRAGON_PEDESTAL))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (STRONGHOLD_CORRIDOR_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.80f)) // Drops 80% of the time
                        .with(ItemEntry.builder(ModItems.ENDER_ROD))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (WOODLAND_MANSION_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.60f)) // Drops 60% of the time
                        .with(ItemEntry.builder(ModItems.EVOKER_KEY))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            ////VILLAGES
            if (ARMORER_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.30f)) // Drops 30% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (DESERT_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.35f)) // Drops 35% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (MASON_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.30f)) // Drops 30% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (PLAINS_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.35f)) // Drops 35% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (SAVANNA_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.35f)) // Drops 35% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (SNOWY_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.35f)) // Drops 35% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (TAIGA_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.35f)) // Drops 35% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (VILLAGE_TEMPLE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.50f)) // Drops 50% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (TOOLSMITH_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.20f)) // Drops 20% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }if (WEAPONSMITH_VILLAGE_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.20f)) // Drops 20% of the time
                        .with(ItemEntry.builder(ModBlocks.TOTEM_OF_ORDER))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

        });
    }

}
