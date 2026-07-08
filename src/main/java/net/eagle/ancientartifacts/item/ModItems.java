package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.item.custom.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

    public static final Item WARDEN_HEART = registerItem("warden_heart", WardensHeart::new, new Item.Settings());

    public static final Item ENDER_ROD = registerItem("ender_rod", Item::new, new Item.Settings());

    public static final Item MYCELIUM_DUST = registerItem("mycelium_dust", Item::new, new Item.Settings());

    public static final Item BLACK_ICE = registerItem("black_ice", Item::new, new Item.Settings());

    public static final Item RED_ICE = registerItem("red_ice", Item::new, new Item.Settings());

    public static final Item NETHER_GRASS = registerItem("nether_grass", Item::new, new Item.Settings());

    public static final Item OCHRE_FIREFLY_BUD = registerItem("ochre_firefly_bud", Item::new, new Item.Settings());

    public static final Item VERDANT_FIREFLY_BUD = registerItem("verdant_firefly_bud", Item::new, new Item.Settings());

    public static final Item PEARLESCENT_FIREFLY_BUD = registerItem("pearlescent_firefly_bud", Item::new, new Item.Settings());

    public static final Item FIREFLY_ORB = registerItem("firefly_orb", Item::new, new Item.Settings());

    public static final Item EVOKER_KEY = registerItem("evoker_key", EvokerKey::new, new Item.Settings().maxCount(1));

    public static final Item ELDER_GUARDIAN_SCALES = registerItem("elder_guardian_scales", ElderGuardianScales::new, new Item.Settings());

    public static final Item END_STAFF = registerItem("end_staff", EndStaff::new, new Item.Settings());

    public static final Item ANKH_PENDANT = registerItem("ankh_pendant", AnkhPendant::new, new Item.Settings().maxCount(1));

    public static final Item ORB_INFINIUM = registerItem("orb_infinium", OrbOfInfinium::new, new Item.Settings().maxCount(1));

    public static final Item DRAGON_FOSSIL = registerItem("dragon_fossil", DragonFossil::new, new Item.Settings().maxCount(1));


    private static <T extends Item> T registerItem(String name, Function<Item.Settings, T> factory, Item.Settings settings) {
        // Generate the RegistryKey
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AncientArtifacts.MOD_ID, name));

        T item = factory.apply(settings.registryKey(key));


        return Registry.register(Registries.ITEM, key, item);
    }

    public static void registerModItems() {

    }
}