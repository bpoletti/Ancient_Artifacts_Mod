package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.item.custom.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item WARDEN_HEART = registerItem("warden_heart",
            new WardensHeart(new Item.Settings()));

    public static final Item ENDER_ROD = registerItem("ender_rod",
            new Item(new Item.Settings()));

    public static final Item MYCELIUM_DUST = registerItem("mycelium_dust",
            new Item(new Item.Settings()));

    public static final Item BLACK_ICE = registerItem("black_ice",
            new Item(new Item.Settings()));

    public static final Item RED_ICE = registerItem("red_ice",
            new Item(new Item.Settings()));

    public static final Item NETHER_GRASS = registerItem("nether_grass",
            new Item(new Item.Settings()));

    public static final Item OCHRE_FIREFLY_BUD = registerItem("ochre_firefly_bud",
            new Item(new Item.Settings()));

    public static final Item VERDANT_FIREFLY_BUD = registerItem("verdant_firefly_bud",
            new Item(new Item.Settings()));

    public static final Item PEARLESCENT_FIREFLY_BUD = registerItem("pearlescent_firefly_bud",
            new Item(new Item.Settings()));

    public static final Item FIREFLY_ORB = registerItem("firefly_orb",
            new Item(new Item.Settings()));

    public static final Item EVOKER_KEY = registerItem("evoker_key",
            new EvokerKey(new Item.Settings().maxCount(1)));

    public static final Item ELDER_GUARDIAN_SCALES = registerItem("elder_guardian_scales",
            new ElderGuardianScales(new Item.Settings()));

    public static final Item END_STAFF = registerItem("end_staff",
            new EndStaff(new Item.Settings()));

    public static final Item ANKH_PENDANT = registerItem("ankh_pendant",
            new AnkhPendant(new Item.Settings().maxCount(1)));

    public static final Item ORB_INFINIUM = registerItem("orb_infinium",
            new OrbOfInfinium(new Item.Settings().maxCount(1)));

    public static final Item DRAGON_FOSSIL = registerItem("dragon_fossil",
            new DragonFossil(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AncientArtifacts.MOD_ID, name), item);
    }
    public static void registerModItems() {
        // Intentionally empty. Calling this ensures ModItems class loads now,
        // so static fields (and registrations) run during mod init, not later.
    }
}
