package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output,
                             CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "AncientArtifacts Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {

                // --- Smelting Recipes ---
                SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.OCHRE_FROGLIGHT), RecipeCategory.MISC, CookingBookCategory.MISC,
                                ModItems.OCHRE_FIREFLY_BUD, 0.1f, 160)
                        .group("firefly_buds")
                        .unlockedBy("has_ochre_froglight", has(Items.OCHRE_FROGLIGHT))
                        .save(output);

                SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.PEARLESCENT_FROGLIGHT), RecipeCategory.MISC, CookingBookCategory.MISC,
                                ModItems.PEARLESCENT_FIREFLY_BUD, 0.1f, 160)
                        .group("firefly_buds")
                        .unlockedBy("has_pearlescent_froglight", has(Items.PEARLESCENT_FROGLIGHT))
                        .save(output);

                SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.VERDANT_FROGLIGHT), RecipeCategory.MISC, CookingBookCategory.MISC,
                                ModItems.VERDANT_FIREFLY_BUD, 0.1f, 160)
                        .group("firefly_buds")
                        .unlockedBy("has_verdant_froglight", has(Items.VERDANT_FROGLIGHT))
                        .save(output);

                // --- Shaped Recipes ---
                shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_WIRE)
                        .pattern(" C ")
                        .pattern(" R ")
                        .pattern(" C ")
                        .define('C', Items.COPPER_INGOT)
                        .define('R', Items.LIGHTNING_ROD)
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .unlockedBy("has_lightning_rod", has(Items.LIGHTNING_ROD))
                        .save(output, "copper_wire_from_copper_and_lightning_rod");

                shaped(RecipeCategory.COMBAT, ModItems.END_STAFF)
                        .group("end_staff")
                        .pattern("  I")
                        .pattern(" S ")
                        .pattern("E  ")
                        .define('I', Items.ENDER_EYE)
                        .define('S', Items.NETHER_STAR)
                        .define('E', ModItems.ENDER_ROD)
                        .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
                        .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                        .unlockedBy("has_ender_rod", has(ModItems.ENDER_ROD))
                        .save(output, "end_staff_right");

                shaped(RecipeCategory.COMBAT, ModItems.END_STAFF)
                        .group("end_staff")
                        .pattern("I  ")
                        .pattern(" S ")
                        .pattern("  E")
                        .define('I', Items.ENDER_EYE)
                        .define('S', Items.NETHER_STAR)
                        .define('E', ModItems.ENDER_ROD)
                        .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
                        .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                        .unlockedBy("has_ender_rod", has(ModItems.ENDER_ROD))
                        .save(output,"end_staff_left");

                shaped(RecipeCategory.REDSTONE, ModBlocks.ETHER_LEVER)
                        .pattern(" W ")
                        .pattern(" B ")
                        .pattern(" E ")
                        .define('W', Items.WITHER_SKELETON_SKULL)
                        .define('B', Items.BLAZE_ROD)
                        .define('E', Items.ECHO_SHARD)
                        .unlockedBy("has_wither_skeleton_skull", has(Items.WITHER_SKELETON_SKULL))
                        .unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
                        .unlockedBy("has_echo_shard", has(Items.ECHO_SHARD))
                        .save(output);

                offerFireflyOrbPermutations(output);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.NENDER_BRICK, 1)
                        .pattern("NW ")
                        .pattern("WN ")
                        .pattern("   ")
                        .define('W', Items.WARPED_FUNGUS)
                        .define('N', Items.NETHER_BRICK)
                        .unlockedBy("has_warped_fungus", has(Items.WARPED_FUNGUS))
                        .unlockedBy("has_nether_brick", has(Items.NETHER_BRICK))
                        .save(output,"nender_bricks_craftable");
            }

            private void offerFireflyOrbPermutations(RecipeOutput output) {
                var G = Blocks.GLASS;
                ItemLike[] buds = new ItemLike[] {
                        ModItems.OCHRE_FIREFLY_BUD,
                        ModItems.VERDANT_FIREFLY_BUD,
                        ModItems.PEARLESCENT_FIREFLY_BUD
                };
                int[][] perms = {
                        {0,1,2},{0,2,1},{1,0,2},
                        {1,2,0},{2,0,1},{2,1,0}
                };

                for (int[] perm : perms) {
                    int a = perm[0], b = perm[1], c = perm[2];

                    shaped(RecipeCategory.BREWING, ModItems.FIREFLY_ORB)
                            .group("firefly_orb")
                            .pattern("GGG")
                            .pattern("OVP")
                            .pattern("GGG")
                            .define('G', G)
                            .define('O', buds[a])
                            .define('V', buds[b])
                            .define('P', buds[c])
                            .unlockedBy("has_glass", has(G))
                            .unlockedBy("has_bud_0", has(buds[0]))
                            .unlockedBy("has_bud_1", has(buds[1]))
                            .unlockedBy("has_bud_2", has(buds[2]))
                            .save(output,"firefly_orb_" + a + b + c);
                }
            }
        };
    }
}