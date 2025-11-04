package net.eagle.ancientartifacts;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.entity.ModBlockEntities;
import net.eagle.ancientartifacts.item.ModItemGroup;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.eagle.ancientartifacts.util.ModLootTableModifies;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AncientArtifacts implements ModInitializer {

	public static final String MOD_ID = "ancientartifacts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItemGroup.registerItemGroup();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModPotions.registerPotions();

		ModLootTableModifies.modifyLootTables();  // keep this after ModItems

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(
					Potions.AWKWARD,                          // input
					ModItems.NETHER_GRASS,                    // ingredient
					Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_I) // output (RegistryEntry<Potion>)
			);

			builder.registerPotionRecipe(Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_I), ModItems.MYCELIUM_DUST,
					Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_II));

			builder.registerPotionRecipe(Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_II), ModItems.BLACK_ICE,
					Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_III));

			builder.registerPotionRecipe(Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_III), ModItems.RED_ICE,
					Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_IV));

			builder.registerPotionRecipe(Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_IV), ModItems.FIREFLY_ORB,
					Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_V));

			builder.registerPotionRecipe(Registries.POTION.getEntry(ModPotions.ELIXIR_BASE_V), ModItems.WARDEN_HEART,
					Registries.POTION.getEntry(ModPotions.ELIXIR_OF_DRAKE));
		});
	}
}