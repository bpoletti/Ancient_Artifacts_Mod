package net.eagle.ancientartifacts.potion;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
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

    private static Potion registerPotion(String name) {
        // Tip: Identifier.of(...) is the newer helper in 1.21.x
        Identifier id = Identifier.of(AncientArtifacts.MOD_ID, name);

        if ("elixir_of_drake".equals(name)) {
            return Registry.register(
                    Registries.POTION, id,
                    new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 200, 0))
            );
        } else {
            return Registry.register(
                    Registries.POTION, id,
                    new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0))
            );
        }
    }

    /** Call this during mod init (before you add brewing recipes). */
    public static void registerPotions() {
        ELIXIR_BASE_I   = registerPotion("elixir_base_i");
        ELIXIR_BASE_II  = registerPotion("elixir_base_ii");
        ELIXIR_BASE_III = registerPotion("elixir_base_iii");
        ELIXIR_BASE_IV  = registerPotion("elixir_base_iv");
        ELIXIR_BASE_V   = registerPotion("elixir_base_v");
        ELIXIR_OF_DRAKE = registerPotion("elixir_of_drake");
        // Brewing recipes are no longer registered here.
    }
}
