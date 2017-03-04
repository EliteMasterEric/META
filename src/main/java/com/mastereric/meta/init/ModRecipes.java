package com.mastereric.meta.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModRecipes {
    public static void initializeCraftingRecipes() {
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockMETA),
				"LRL", "RFR", "IRI",
				'L', new ItemStack(Blocks.LAPIS_BLOCK),
				'R', new ItemStack(Items.REDSTONE),
                'F', new ItemStack(Blocks.FURNACE),
                'I', new ItemStack(Blocks.IRON_BLOCK));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockModMaker),
                " L ", "ESE", "SIS",
                'L', new ItemStack(Blocks.LAPIS_BLOCK),
                'E', new ItemStack(Items.EMERALD),
                'S', new ItemStack(Blocks.STONE),
                'I', new ItemStack(Blocks.IRON_BLOCK));
	}
}