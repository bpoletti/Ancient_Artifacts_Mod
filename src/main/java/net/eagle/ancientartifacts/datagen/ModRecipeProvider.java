package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output,
                             CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {

                this.offerSmelting(List.of(Items.OCHRE_FROGLIGHT), RecipeCategory.MISC, ModItems.OCHRE_FIREFLY_BUD, 0.1f, 160, "firefly_buds");
                this.offerSmelting(List.of(Items.PEARLESCENT_FROGLIGHT), RecipeCategory.MISC, ModItems.PEARLESCENT_FIREFLY_BUD, 0.1f, 160, "firefly_buds");
                this.offerSmelting(List.of(Items.VERDANT_FROGLIGHT), RecipeCategory.MISC, ModItems.VERDANT_FIREFLY_BUD, 0.1f, 160, "firefly_buds");

                this.createShaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_WIRE , 1)
                        .pattern(" C ")
                        .pattern(" R ")
                        .pattern(" C ")
                        .input('C', Items.COPPER_INGOT)
                        .input('R', Items.LIGHTNING_ROD)
                        .criterion(this.hasItem(Items.COPPER_INGOT), this.conditionsFromItem(Items.COPPER_INGOT))
                        .criterion(this.hasItem(Items.LIGHTNING_ROD), this.conditionsFromItem(Items.LIGHTNING_ROD))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AncientArtifacts.MOD_ID, "copper_wire_from_copper_and_lightning_rod")));

                this.createShaped(RecipeCategory.COMBAT, ModItems.END_STAFF)
                        .group("end_staff")
                        .pattern("  I")
                        .pattern(" S ")
                        .pattern("E  ")
                        .input('I', Items.ENDER_EYE)
                        .input('S', Items.NETHER_STAR)
                        .input('E', ModItems.ENDER_ROD)
                        .criterion(this.hasItem(Items.ENDER_EYE), this.conditionsFromItem(Items.ENDER_EYE))
                        .criterion(this.hasItem(Items.NETHER_STAR), this.conditionsFromItem(Items.NETHER_STAR))
                        .criterion(this.hasItem(ModItems.ENDER_ROD), this.conditionsFromItem(ModItems.ENDER_ROD))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AncientArtifacts.MOD_ID, "end_staff_right")));

                this.createShaped(RecipeCategory.COMBAT, ModItems.END_STAFF)
                        .group("end_staff")
                        .pattern("I  ")
                        .pattern(" S ")
                        .pattern("  E")
                        .input('I', Items.ENDER_EYE)
                        .input('S', Items.NETHER_STAR)
                        .input('E', ModItems.ENDER_ROD)
                        .criterion(this.hasItem(Items.ENDER_EYE), this.conditionsFromItem(Items.ENDER_EYE))
                        .criterion(this.hasItem(Items.NETHER_STAR), this.conditionsFromItem(Items.NETHER_STAR))
                        .criterion(this.hasItem(ModItems.ENDER_ROD), this.conditionsFromItem(ModItems.ENDER_ROD))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AncientArtifacts.MOD_ID, "end_staff_left")));

                this.createShaped(RecipeCategory.REDSTONE, ModBlocks.ETHER_LEVER)
                        .pattern(" W ")
                        .pattern(" B ")
                        .pattern(" E ")
                        .input('W', Items.WITHER_SKELETON_SKULL)
                        .input('B', Items.BLAZE_ROD)
                        .input('E', Items.ECHO_SHARD)
                        .criterion(this.hasItem(Items.WITHER_SKELETON_SKULL), this.conditionsFromItem(Items.WITHER_SKELETON_SKULL))
                        .criterion(this.hasItem(Items.BLAZE_ROD), this.conditionsFromItem(Items.BLAZE_ROD))
                        .criterion(this.hasItem(Items.ECHO_SHARD), this.conditionsFromItem(Items.ECHO_SHARD))
                        .offerTo(recipeExporter);

                this.offerFireflyOrbPermutations(recipeExporter);

                this.createShaped(RecipeCategory.DECORATIONS, ModBlocks.NENDER_BRICK , 1)
                        .pattern("NW ")
                        .pattern("WN ")
                        .pattern("   ")
                        .input('W', Items.WARPED_FUNGUS)
                        .input('N', Items.NETHER_BRICK)
                        .criterion(this.hasItem(Items.WARPED_FUNGUS), this.conditionsFromItem(Items.WARPED_FUNGUS))
                        .criterion(this.hasItem(Items.NETHER_BRICK), this.conditionsFromItem(Items.NETHER_BRICK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AncientArtifacts.MOD_ID, "nender_bricks_craftable")));
            }

            private void offerFireflyOrbPermutations(RecipeExporter recipeExporter) {
                var G = Blocks.GLASS;
                var buds = new net.minecraft.item.ItemConvertible[] {
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

                    this.createShaped(RecipeCategory.BREWING, ModItems.FIREFLY_ORB)
                            .group("firefly_orb")
                            .pattern("GGG")
                            .pattern("OVP")
                            .pattern("GGG")
                            .input('G', G)
                            .input('O', buds[a])
                            .input('V', buds[b])
                            .input('P', buds[c])
                            .criterion(this.hasItem(G), this.conditionsFromItem(G))
                            .criterion(this.hasItem(buds[0]), this.conditionsFromItem(buds[0]))
                            .criterion(this.hasItem(buds[1]), this.conditionsFromItem(buds[1]))
                            .criterion(this.hasItem(buds[2]), this.conditionsFromItem(buds[2]))
                            .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AncientArtifacts.MOD_ID, "firefly_orb_" + a + b + c)));
                }
            }
        };
    }

    @Override
    public String getName() {
        return "AncientArtifacts Recipes";
    }
}