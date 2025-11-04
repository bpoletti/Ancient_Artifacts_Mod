package net.eagle.ancientartifacts.datagen;

import net.eagle.ancientartifacts.AncientArtifacts;
import net.eagle.ancientartifacts.block.ModBlocks;
import net.eagle.ancientartifacts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
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
    public void generate(RecipeExporter exporter) {
        // ochre -> ochre_firefly_bud
        smelt(exporter, Items.OCHRE_FROGLIGHT, ModItems.OCHRE_FIREFLY_BUD);

        // pearlescent -> pearlescent_firefly_bud
        smelt(exporter, Items.PEARLESCENT_FROGLIGHT, ModItems.PEARLESCENT_FIREFLY_BUD);

        // verdant -> verdant_firefly_bud
        smelt(exporter, Items.VERDANT_FROGLIGHT, ModItems.VERDANT_FIREFLY_BUD);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.COPPER_WIRE , 1)
                .pattern(" C ")
                .pattern(" R ")
                .pattern(" C ")
                .input('C', Items.COPPER_INGOT)
                .input('R', Items.LIGHTNING_ROD)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.LIGHTNING_ROD), conditionsFromItem(Items.LIGHTNING_ROD))
                .offerTo(exporter, Identifier.of(AncientArtifacts.MOD_ID, "copper_wire_from_copper_and_lightning_rod"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.END_STAFF)
                .group("end_staff")
                .pattern("  I")
                .pattern(" S ")
                .pattern("E  ")
                .input('I', Items.ENDER_EYE)
                .input('S', Items.NETHER_STAR)
                .input('E', ModItems.ENDER_ROD)
                .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                .criterion(hasItem(Items.NETHER_STAR), conditionsFromItem(Items.NETHER_STAR))
                .criterion(hasItem(ModItems.ENDER_ROD), conditionsFromItem(ModItems.ENDER_ROD))
                .offerTo(exporter, Identifier.of(AncientArtifacts.MOD_ID, "end_staff_right"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.END_STAFF)
                .group("end_staff")
                .pattern("I  ")
                .pattern(" S ")
                .pattern("  E")
                .input('I', Items.ENDER_EYE)
                .input('S', Items.NETHER_STAR)
                .input('E', ModItems.ENDER_ROD)
                .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                .criterion(hasItem(Items.NETHER_STAR), conditionsFromItem(Items.NETHER_STAR))
                .criterion(hasItem(ModItems.ENDER_ROD), conditionsFromItem(ModItems.ENDER_ROD))
                .offerTo(exporter, Identifier.of(AncientArtifacts.MOD_ID, "end_staff_left"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ETHER_LEVER)
                .pattern(" W ")
                .pattern(" B ")
                .pattern(" E ")
                .input('W', Items.WITHER_SKELETON_SKULL)
                .input('B', Items.BLAZE_ROD)
                .input('E', Items.ECHO_SHARD)
                .criterion(hasItem(Items.WITHER_SKELETON_SKULL), conditionsFromItem(Items.WITHER_SKELETON_SKULL))
                .criterion(hasItem(Items.BLAZE_ROD), conditionsFromItem(Items.BLAZE_ROD))
                .criterion(hasItem(Items.ECHO_SHARD), conditionsFromItem(Items.ECHO_SHARD))
                .offerTo(exporter);

        offerFireflyOrbPermutations(exporter); // for firefly orbs

    }

    private void smelt(RecipeExporter exporter, ItemConvertible input, ItemConvertible output) {
        // 160 ticks @ 0.1 xp, put under the MISC recipe book tab
        offerSmelting(exporter,
                List.of(input),
                RecipeCategory.MISC,
                output,
                0.1f,
                160,
                "firefly_buds"); // group (optional): groups these 3 in the book
    }

    private void offerFireflyOrbPermutations(RecipeExporter exporter) {
        var G = Blocks.GLASS;
        var buds = new net.minecraft.item.ItemConvertible[] {
                ModItems.OCHRE_FIREFLY_BUD,     // 0
                ModItems.VERDANT_FIREFLY_BUD,   // 1
                ModItems.PEARLESCENT_FIREFLY_BUD// 2
        };
        int[][] perms = {
                {0,1,2},{0,2,1},{1,0,2},
                {1,2,0},{2,0,1},{2,1,0}
        };

        for (int[] perm : perms) {
            int a = perm[0], b = perm[1], c = perm[2];

            ShapedRecipeJsonBuilder.create(RecipeCategory.BREWING, ModItems.FIREFLY_ORB)
                    .group("firefly_orb")      // same group => merged in recipe book UI
                    .pattern("GGG")
                    .pattern("OVP")
                    .pattern("GGG")
                    .input('G', G)
                    .input('O', buds[a])
                    .input('V', buds[b])
                    .input('P', buds[c])
                    // unlocks — keep all three so either recipe grants it
                    .criterion(hasItem(G), conditionsFromItem(G))
                    .criterion(hasItem(buds[0]), conditionsFromItem(buds[0]))
                    .criterion(hasItem(buds[1]), conditionsFromItem(buds[1]))
                    .criterion(hasItem(buds[2]), conditionsFromItem(buds[2]))
                    .offerTo(exporter, Identifier.of(AncientArtifacts.MOD_ID, "firefly_orb_" + a + b + c));
        }
    }
}
