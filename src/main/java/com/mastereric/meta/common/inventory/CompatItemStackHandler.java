package com.mastereric.meta.common.inventory;

import com.mastereric.meta.util.LogUtility;
import mcjty.lib.compat.CompatItemHandler;
import mcjty.lib.compat.CompatItemHandlerModifiable;
import mcjty.lib.tools.ItemStackList;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

public class CompatItemStackHandler implements CompatItemHandler, CompatItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
    protected ItemStackList stacks;

    public CompatItemStackHandler()
    {
        this(1);
    }

    public CompatItemStackHandler(int size)
    {
        stacks = ItemStackList.create(1);
    }

    public CompatItemStackHandler(ItemStackList stacks)
    {
        this.stacks = stacks;
    }

    // WARNING: this will clear the list.
    public void setSize(int size) { stacks = ItemStackList.create(size); }

    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        validateSlotIndex(slot);
        if (ItemStack.areItemStacksEqual(stacks.get(slot), stack))
            return;
        stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public int getSlots()
    {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        //LogUtility.infoSided("CompatItemStackHandler.getStackInSlot");
        validateSlotIndex(slot);
        return stacks.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        LogUtility.infoSided("CompatItemStackHandler.insertItem");
        if (ItemStackTools.isEmpty(stack) || ItemStackTools.getStackSize(stack) == 0)
            return ItemStackTools.getEmptyStack();

        validateSlotIndex(slot);

        ItemStack existing = stacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!ItemStackTools.isEmpty(existing))
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= ItemStackTools.getStackSize(existing);
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = ItemStackTools.getStackSize(stack) > limit;

        if (!simulate)
        {
            if (existing == null)
            {
                stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            }
            else
            {
                ItemStackTools.setStackSize(existing, ItemStackTools.getStackSize(existing) + (reachedLimit ? limit : ItemStackTools.getStackSize(stack)));
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, ItemStackTools.getStackSize(stack) - limit) : ItemStackTools.getEmptyStack();
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        LogUtility.infoSided("CompatItemStackHandler.extractItem");
        if (amount == 0) {
            LogUtility.infoSided("Amount to extract was 0.");
            return null;
        }

        validateSlotIndex(slot);

        ItemStack existing = stacks.get(slot);

        if (ItemStackTools.isEmpty(existing)) {
            LogUtility.infoSided("existing is empty.");
            return null;
        }

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        LogUtility.infoSided("Extracting one item...");

        if (ItemStackTools.getStackSize(existing) <= toExtract) {
            LogUtility.infoSided("Amount to extract less than actual amount.");
            if (!simulate) {
                LogUtility.infoSided("Setting slot to empty stack.");
                stacks.set(slot, ItemStackTools.getEmptyStack());
                onContentsChanged(slot);
            } else {
                LogUtility.infoSided("Only simulating...");
            }
            LogUtility.infoSided("Returning existing...");
            LogUtility.infoSided(existing.toString());
            return existing;
        } else {
            LogUtility.infoSided("Amount to extract less than amount there.");
            if (!simulate) {
                LogUtility.infoSided("Setting slot to less...");
                stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, ItemStackTools.getStackSize(existing) - toExtract));
                onContentsChanged(slot);
            }
            LogUtility.infoSided("Returning existing with amount to extract...");
            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    private int getStackLimit(int slot, ItemStack stack) {
        return stack.getMaxStackSize();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        //LogUtility.infoSided("CompatItemStackHandler.serializeNBT");
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!ItemStackTools.isEmpty(stacks.get(i)))
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size());
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.set(slot, ItemStackTools.loadFromNBT(itemTags));
            }
        }
        onLoad();
    }

    protected void validateSlotIndex(int slot)
    {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    protected void onLoad()
    {

    }

    protected void onContentsChanged(int slot)
    {

    }

    public int getSlotMaxLimit() {
        return getStackLimit(0, stacks.get(0));
    }

    public int getSlotLimit(int slot) {
        return getStackLimit(slot, stacks.get(slot));
    }
}