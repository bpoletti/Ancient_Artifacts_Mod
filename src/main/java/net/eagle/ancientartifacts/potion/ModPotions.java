package net.eagle.ancientartifacts.potion;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {

    public static Potion ELIXIR_BASE_I;
    public static Potion ELIXIR_BASE_II;
    public static Potion ELIXIR_BASE_III;
    public static Potion ELIXIR_BASE_IV;
    public static Potion ELIXIR_BASE_V;
    public static Potion ELIXIR_OF_DRAKE;

    private static Potion registerPotion(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, name);

        if ("elixir_of_drake".equals(name)) {
            return Registry.register(
                    BuiltInRegistries.POTION, id,
                    new Potion(name ,new MobEffectInstance(MobEffects.LEVITATION, 200, 0))
            );
        } else {
            return Registry.register(
                    BuiltInRegistries.POTION, id,

                    new Potion(name ,new MobEffectInstance(MobEffects.NAUSEA, 100, 0))
            );
        }
    }

    public static void registerPotions() {
        ELIXIR_BASE_I   = registerPotion("elixir_base_i");
        ELIXIR_BASE_II  = registerPotion("elixir_base_ii");
        ELIXIR_BASE_III = registerPotion("elixir_base_iii");
        ELIXIR_BASE_IV  = registerPotion("elixir_base_iv");
        ELIXIR_BASE_V   = registerPotion("elixir_base_v");
        ELIXIR_OF_DRAKE = registerPotion("elixir_of_drake");
    }
}