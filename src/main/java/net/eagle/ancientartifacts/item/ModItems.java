package net.eagle.ancientartifacts.item;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    
    public static final Item WARDEN_HEART = registerItem("warden_heart",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    
    public static final Item ENDER_ROD = registerItem("ender_rod",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    
    public static final Item MYCELIUM_DUST = registerItem("mycelium_dust",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    
    public static final Item BLACK_ICE = registerItem("black_ice",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item RED_ICE = registerItem("red_ice",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item NETHER_GRASS = registerItem("nether_grass",
        new Item(new FabricItemSettings().group(ItemGroup.BREWING)));
        
    public static final Item OCHRE_FIREFLY_BUD = registerItem("ochre_firefly_bud",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item VERDANT_FIREFLY_BUD = registerItem("verdant_firefly_bud",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item PEARLESCENT_FIREFLY_BUD = registerItem("pearlescent_firefly_bud",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item FIREFLY_ORB = registerItem("firefly_orb",
        new Item(new FabricItemSettings().group(ItemGroup.BREWING))); 
        
    public static final Item TOTEM_OF_ORDER = registerItem("totem_of_order",
        new Item(new FabricItemSettings().group(ItemGroup.MISC))); 
        
    public static final Item TOTEM_OF_CHAOS = registerItem("totem_of_chaos",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item EVOKER_KEY = registerItem("evoker_key",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        
    public static final Item ELDER_GUARDIAN_SCALES = registerItem("elder_guardian_scales",
        new Item(new FabricItemSettings().group(ItemGroup.BREWING)));
        
    public static final Item END_STAFF = registerItem("end_staff",
        new Item(new FabricItemSettings().group(ItemGroup.COMBAT)));
        
    public static final Item ETHER_LEVER = registerItem("ether_lever",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));

    public static final Item ANKH_PENDANT = registerItem("ankh_pendant",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(AncientArtifacts.MOD_ID, name), item);
    }

    public static void registerModItems() {
        AncientArtifacts.LOGGER.debug("Registering Mod Items for " + AncientArtifacts.MOD_ID);
    }

}
