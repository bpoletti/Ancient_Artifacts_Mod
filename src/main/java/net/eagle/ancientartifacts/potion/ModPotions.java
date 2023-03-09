package net.eagle.ancientartifacts.potion;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.mixin.BrewingRecipeRegistryMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    
         public static Potion ELIXIR_BASE_I;
         public static Potion ELIXIR_BASE_II;
         public static Potion ELIXIR_BASE_III;
         public static Potion ELIXIR_BASE_IV;
         public static Potion ELIXIR_BASE_V;
        public static Potion ELIXIR_OF_DRAKE;

        public static Potion registerPotion(String name) {
            if(name.equals("elixir_of_drake")) {
                return Registry.register(Registries.POTION, new Identifier(AncientArtifacts.MOD_ID, name),
                        new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 200,0)));
            }
            else {
                return Registry.register(Registries.POTION, new Identifier(AncientArtifacts.MOD_ID, name),
                        new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 100,0)));
            }
        }


        public static void registerPotions() {
            ELIXIR_BASE_I = registerPotion("elixir_base_i");
            ELIXIR_BASE_II = registerPotion("elixir_base_ii");
            ELIXIR_BASE_III = registerPotion("elixir_base_iii");
            ELIXIR_BASE_IV = registerPotion("elixir_base_iv");
            ELIXIR_BASE_V = registerPotion("elixir_base_v");
            ELIXIR_OF_DRAKE = registerPotion("elixir_of_drake");

            registerPotionRecipes();
        }


     private static void registerPotionRecipes() {
         BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD,
          ModItems.NETHER_GRASS, ModPotions.ELIXIR_BASE_I);
         BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ELIXIR_BASE_I,
          ModItems.MYCELIUM_DUST, ModPotions.ELIXIR_BASE_II);
         BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ELIXIR_BASE_II,
          ModItems.BLACK_ICE, ModPotions.ELIXIR_BASE_III);
         BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ELIXIR_BASE_III,
          ModItems.RED_ICE, ModPotions.ELIXIR_BASE_IV);
         BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ELIXIR_BASE_IV,
          ModItems.FIREFLY_ORB, ModPotions.ELIXIR_BASE_V);
         BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.ELIXIR_BASE_V,
          ModItems.WARDEN_HEART, ModPotions.ELIXIR_OF_DRAKE);
     }
}
