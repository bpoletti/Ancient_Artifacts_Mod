package net.eagle.ancientartifacts;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.block.entity.ModBlockEntities;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.potion.ModPotions;
import net.fabricmc.api.ModInitializer;

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
	}
}