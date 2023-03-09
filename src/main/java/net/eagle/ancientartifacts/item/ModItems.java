package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.item.custom.DragonFossil;
import net.eagle.ancientartifacts.item.custom.EndStaff;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    
    public static final Item WARDEN_HEART = registerItem("warden_heart",
        new Item(new FabricItemSettings()));
    
    public static final Item ENDER_ROD = registerItem("ender_rod",
        new Item(new FabricItemSettings()));
    
    public static final Item MYCELIUM_DUST = registerItem("mycelium_dust",
        new Item(new FabricItemSettings()));
    
    public static final Item BLACK_ICE = registerItem("black_ice",
        new Item(new FabricItemSettings()));
        
    public static final Item RED_ICE = registerItem("red_ice",
        new Item(new FabricItemSettings()));
        
    public static final Item NETHER_GRASS = registerItem("nether_grass",
        new Item(new FabricItemSettings()));
        
    public static final Item OCHRE_FIREFLY_BUD = registerItem("ochre_firefly_bud",
        new Item(new FabricItemSettings()));
        
    public static final Item VERDANT_FIREFLY_BUD = registerItem("verdant_firefly_bud",
        new Item(new FabricItemSettings()));
        
    public static final Item PEARLESCENT_FIREFLY_BUD = registerItem("pearlescent_firefly_bud",
        new Item(new FabricItemSettings()));
        
    public static final Item FIREFLY_ORB = registerItem("firefly_orb",
        new Item(new FabricItemSettings()));
        
    public static final Item EVOKER_KEY = registerItem("evoker_key",
        new Item(new FabricItemSettings().maxCount(1)));
        
    public static final Item ELDER_GUARDIAN_SCALES = registerItem("elder_guardian_scales",
        new Item(new FabricItemSettings()));
        
    public static final Item END_STAFF = registerItem("end_staff",
        new EndStaff(new FabricItemSettings()));

    public static final Item ANKH_PENDANT = registerItem("ankh_pendant",
        new Item(new FabricItemSettings().maxCount(1)));

    public static final Item ORB_INFINIUM = registerItem("orb_infinium",
        new Item(new FabricItemSettings().maxCount(1)));

    public static final Item DRAGON_FOSSIL = registerItem("dragon_fossil",
        new DragonFossil(new FabricItemSettings().maxCount(1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(AncientArtifacts.MOD_ID, name), item);
    }

    public static void addItemsToItmeGroups() {
        addToItemGroup(ModItemGroup.ARTIFACTS, WARDEN_HEART);
        addToItemGroup(ModItemGroup.ARTIFACTS, ENDER_ROD);
        addToItemGroup(ModItemGroup.ARTIFACTS, MYCELIUM_DUST);
        addToItemGroup(ModItemGroup.ARTIFACTS, BLACK_ICE);
        addToItemGroup(ModItemGroup.ARTIFACTS, RED_ICE);
        addToItemGroup(ModItemGroup.ARTIFACTS, NETHER_GRASS);
        addToItemGroup(ModItemGroup.ARTIFACTS, OCHRE_FIREFLY_BUD);
        addToItemGroup(ModItemGroup.ARTIFACTS, PEARLESCENT_FIREFLY_BUD);
        addToItemGroup(ModItemGroup.ARTIFACTS, VERDANT_FIREFLY_BUD);
        addToItemGroup(ModItemGroup.ARTIFACTS, FIREFLY_ORB);
        addToItemGroup(ModItemGroup.ARTIFACTS, EVOKER_KEY);
        addToItemGroup(ModItemGroup.ARTIFACTS, ELDER_GUARDIAN_SCALES);
        addToItemGroup(ModItemGroup.ARTIFACTS, END_STAFF);
        addToItemGroup(ModItemGroup.ARTIFACTS, ANKH_PENDANT);
        addToItemGroup(ModItemGroup.ARTIFACTS, ORB_INFINIUM);
        addToItemGroup(ModItemGroup.ARTIFACTS, DRAGON_FOSSIL);
    }

    public static void addToItemGroup(ItemGroup group, Item item) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries ->
                entries.add(item));
    }

    public static void registerModItems() {
        AncientArtifacts.LOGGER.debug("Registering Mod Items for " + AncientArtifacts.MOD_ID);
        addItemsToItmeGroups();
    }

}
