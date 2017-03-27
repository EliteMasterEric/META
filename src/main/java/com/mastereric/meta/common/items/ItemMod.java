package com.mastereric.meta.common.items;

import com.mastereric.meta.util.LangUtility;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class ItemMod extends Item {
	//TODO add more mods
	private static final int DESCRIPTION_COUNT = 26;

	public ItemMod() {
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

    public static void createInfo(ItemStack stack) {
	    if (!(stack.getItem() instanceof ItemMod))
	        return;
        LogUtility.debug("Creating mod item...");
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if (nbtTagCompound == null || !nbtTagCompound.hasKey("Random")) {
            nbtTagCompound = new NBTTagCompound();
            int rand = new Random().nextInt(DESCRIPTION_COUNT);
            nbtTagCompound.setInteger("Random", rand);
            stack.setTagCompound(nbtTagCompound);
            stack.setItemDamage(rand % 5);
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
		if (nbtTagCompound != null && nbtTagCompound.hasKey("Random")) {
            tooltip.add(LangUtility.getTranslation(this.getUnlocalizedName() + ".desc.rand." + nbtTagCompound.getInteger("Random")));
        }
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

}
