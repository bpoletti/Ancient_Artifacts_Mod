package net.eagle.ancientartifacts;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.entity.ModBlockEntities;
import net.eagle.ancientartifacts.creativemodetab.ModCreativeModeTabs;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.eagle.ancientartifacts.util.ModLootTableModifies;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AncientArtifacts implements ModInitializer {

	public static final String MOD_ID = "ancientartifacts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModPotions.registerPotions();
		ModLootTableModifies.modifyLootTables();
		ModCreativeModeTabs.registerModCreativeTab();

		FabricPotionBrewingBuilder.BUILD.register(builder -> {

			builder.registerPotionRecipe(
					Potions.AWKWARD,
					Ingredient.of(ModItems.NETHER_GRASS),
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_I)
			);

			builder.registerPotionRecipe(
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_I),
					Ingredient.of(ModItems.MYCELIUM_DUST),
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_II)
			);

			builder.registerPotionRecipe(
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_II),
					Ingredient.of(ModItems.BLACK_ICE),
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_III)
			);

			builder.registerPotionRecipe(
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_III),
					Ingredient.of(ModItems.RED_ICE),
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_IV)
			);

			builder.registerPotionRecipe(
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_IV),
					Ingredient.of(ModItems.FIREFLY_ORB),
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_V)
			);

			builder.registerPotionRecipe(
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_BASE_V),
					Ingredient.of(ModItems.WARDEN_HEART),
					BuiltInRegistries.POTION.wrapAsHolder(ModPotions.ELIXIR_OF_DRAKE)
			);
		});
	}
}