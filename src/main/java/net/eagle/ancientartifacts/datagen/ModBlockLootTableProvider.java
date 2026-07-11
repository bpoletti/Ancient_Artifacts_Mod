package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.custom.ChachapoyanIdol;
import net.eagle.ancientartifacts.block.custom.DragonPedestal;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {
    public ModBlockLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.NENDER_BRICK);
        dropSelf(ModBlocks.COPPER_WIRE);
        dropSelf(ModBlocks.ETHER_LEVER);
        dropSelf(ModBlocks.GILDED_PLATE);
        dropSelf(ModBlocks.TOTEM_OF_CHAOS);
        dropSelf(ModBlocks.TOTEM_OF_ORDER);

        this.add(ModBlocks.CHACHAPOYAN_IDOL, createChachapoyanIdolConditionalDrops(
                ModBlocks.CHACHAPOYAN_IDOL,
                ModBlocks.CHACHAPOYAN_IDOL.asItem(),
                ChachapoyanIdol.PENDANT, ModItems.ANKH_PENDANT,
                ChachapoyanIdol.SCALES, ModItems.ELDER_GUARDIAN_SCALES
        ));
        add(ModBlocks.DRAGON_PEDESTAL, createDragonPedestalDrops(
                ModBlocks.DRAGON_PEDESTAL,
                ModBlocks.DRAGON_PEDESTAL.asItem(),
                DragonPedestal.GILDED, ModBlocks.GILDED_PLATE.asItem(),
                DragonPedestal.FOSSIL_HEAD, ModItems.DRAGON_FOSSIL,
                DragonPedestal.HEART_SEA, Items.HEART_OF_THE_SEA,
                DragonPedestal.ORB_INFINIUM, ModItems.ORB_INFINIUM
                ));
    }
    protected LootTable.Builder createChachapoyanIdolConditionalDrops(Block block, Item baseDrop,
                                                           BooleanProperty prop1, Item drop1,
                                                           BooleanProperty prop2, Item drop2) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(baseDrop))
                )

                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop1))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(prop1, true)
                                )
                        )
                )

                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop2))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(prop2, true)
                                )
                        )
                );
    }

    protected LootTable.Builder createDragonPedestalDrops(Block block, Item baseDrop,
                                                          BooleanProperty prop1, Item drop1,
                                                          BooleanProperty prop2, Item drop2,
                                                          BooleanProperty prop3, Item drop3,
                                                          BooleanProperty prop4, Item drop4) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(baseDrop))
                )

                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop1))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(prop1, true)
                                )
                        )
                )

                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop2))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(prop2, true)
                                )
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop3))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(prop3, true)
                                )
                        )
                ).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop4))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(prop4, true)
                                )
                        )
                );
    }
}
