package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.item.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

    public static final Item WARDEN_HEART = registerItem("warden_heart", WardensHeart::new);

    public static final Item ENDER_ROD = registerItem("ender_rod", Item::new);

    public static final Item MYCELIUM_DUST = registerItem("mycelium_dust", Item::new);

    public static final Item BLACK_ICE = registerItem("black_ice", Item::new);

    public static final Item RED_ICE = registerItem("red_ice", Item::new);

    public static final Item NETHER_GRASS = registerItem("nether_grass", Item::new);

    public static final Item OCHRE_FIREFLY_BUD = registerItem("ochre_firefly_bud", Item::new);

    public static final Item VERDANT_FIREFLY_BUD = registerItem("verdant_firefly_bud", Item::new);

    public static final Item PEARLESCENT_FIREFLY_BUD = registerItem("pearlescent_firefly_bud", Item::new);

    public static final Item FIREFLY_ORB = registerItem("firefly_orb", Item::new);

    public static final Item EVOKER_KEY = registerItem("evoker_key", properties -> new EvokerKey(properties.stacksTo(1)));

    public static final Item ELDER_GUARDIAN_SCALES = registerItem("elder_guardian_scales", ElderGuardianScales::new);

    public static final Item END_STAFF = registerItem("end_staff", EndStaff::new);

    public static final Item ANKH_PENDANT = registerItem("ankh_pendant", properties -> new AnkhPendant(properties.stacksTo(1)));

    public static final Item ORB_INFINIUM = registerItem("orb_infinium", properties -> new OrbOfInfinium(properties.stacksTo(1)));

    public static final Item DRAGON_FOSSIL = registerItem("dragon_fossil", properties -> new DragonFossil(properties.stacksTo(1)));

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {

        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath(AncientArtifacts.MOD_ID, name)))));
    }

    public static  void registerModItems() {
        AncientArtifacts.LOGGER.info("Registering Mod Items" + AncientArtifacts.MOD_ID);
    }
}