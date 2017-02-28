package com.mastereric.meta.common.items;

import com.mastereric.meta.util.LangUtility;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemModDumb extends Item {
	// Enabled by a config option, this item makes the Mod Maker and META not work together.
	public ItemModDumb() {
		super();
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);
	}
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(LangUtility.getTranslation(this.getUnlocalizedName() + ".desc"));
    }

}
