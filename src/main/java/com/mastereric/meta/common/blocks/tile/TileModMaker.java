package com.mastereric.meta.common.blocks.tile;

import com.mastereric.meta.common.inventory.ModMakerItemStackHandler;
import com.mastereric.meta.common.items.ItemMod;
import com.mastereric.meta.init.ModConfig;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.util.LangUtility;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileModMaker extends TileEntity implements ITickable {
    // The number of ticks for one mod to be created.
    public static final int DEFAULT_WAIT_TIME = 1800;

    private ModMakerItemStackHandler inventoryItemHandler = new ModMakerItemStackHandler(1);
    // Current wait time in ticks.
    private int modMakerCurrentWaitTime = DEFAULT_WAIT_TIME;
    private int lastSpeedMultiplier = 0;
    private String customName = "";

    //TODO add mod maker craft achievement trigger
    //TODO add JEI progress display

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        //TODO add CommonCapabilites working to Mod Maker
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return  CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryItemHandler);

        return super.getCapability(capability, facing);
    }

    private int getSpeedMultiplier(int bookcaseCount) {
        // Manual values are easier than an equation.
        if (bookcaseCount >= 20)
            return 4;
        else if (bookcaseCount >= 10)
            return 3;
        else if (bookcaseCount >= 4)
            return 2;
        else if (bookcaseCount >= 1)
            return 1;
        else
            return 0;
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
    }

    @Override
    @SuppressWarnings("MethodCallSideOnly")
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        if(net.getDirection() == EnumPacketDirection.CLIENTBOUND) {
            readFromNBT(pkt.getNbtCompound());
            markDirty();
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setString("CustomName", customName);
        tag.setTag("Inventory", inventoryItemHandler.serializeNBT());
        tag.setInteger("TicksRemaining", modMakerCurrentWaitTime);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        customName = tag.getString("CustomName");
        inventoryItemHandler.deserializeNBT(tag.getCompoundTag("Inventory"));
        modMakerCurrentWaitTime = tag.getInteger("TicksRemaining");
        markDirty();
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : LangUtility.getTranslation("container.meta.mod_maker");
    }
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }
    public void setCustomInventoryName(String name) {
        this.customName = name;
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.modMakerCurrentWaitTime = compound.getInteger("CurrentWaitTime");
        inventoryItemHandler.deserializeNBT(compound.getCompoundTag("Inventory"));
        markDirty();

        if (compound.hasKey("CustomName")) {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CurrentWaitTime", (short) this.modMakerCurrentWaitTime);
        compound.setTag("Inventory", inventoryItemHandler.serializeNBT());

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    public boolean isActive() {
        return modMakerCurrentWaitTime > 0 && inventoryItemHandler.getStackInSlot(0).isEmpty() && getLastSpeedMultiplier() != 0;
    }

    private void createMod() {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            LogUtility.infoSided("Creating mod in Mod Maker...");
            ItemStack stack;
            if (ModConfig.MOD_IDEA_190)
                stack = new ItemStack(ModItems.itemModDumb, 1);
            else
                stack = new ItemStack(ModItems.itemMod, 1);
            ItemMod.createInfo(stack);
            inventoryItemHandler.setStackInSlot(0, stack);
            markDirty();
        }
    }

    public void update() {
        int nearbyShelves = 0;
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if ((j != 0 || k != 0) && world.isAirBlock(pos.add(k, 0, j)) && world.isAirBlock(pos.add(k, 1, j))) {
                    nearbyShelves += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, pos.add(k * 2, 0, j * 2));
                    nearbyShelves += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, pos.add(k * 2, 1, j * 2));
                    if (k != 0 && j != 0) {
                        nearbyShelves += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, pos.add(k * 2, 0, j));
                        nearbyShelves += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, pos.add(k * 2, 1, j));
                        nearbyShelves += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, pos.add(k, 0, j * 2));
                        nearbyShelves += net.minecraftforge.common.ForgeHooks.getEnchantPower(world, pos.add(k, 1, j * 2));
                    }
                }
            }
        }

        lastSpeedMultiplier = getSpeedMultiplier(nearbyShelves);
        if (isActive()) {
            if (modMakerCurrentWaitTime < lastSpeedMultiplier)
                modMakerCurrentWaitTime = 0;
            else
                modMakerCurrentWaitTime -= lastSpeedMultiplier;
        } else if (modMakerCurrentWaitTime == 0 && inventoryItemHandler.getStackInSlot(0).isEmpty()) {
            createMod();
            modMakerCurrentWaitTime = DEFAULT_WAIT_TIME;
        }
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    public int getTicksRemaining() {
        //LogUtility.infoSided("TicksRemaining: %d", modMakerCurrentWaitTime);
        if (inventoryItemHandler.getStackInSlot(0).isEmpty())
            return modMakerCurrentWaitTime;
        return 0;
    }

    public int getLastSpeedMultiplier() {
        //LogUtility.infoSided("LastSpeed: %d", lastSpeedMultiplier);
        return lastSpeedMultiplier;
    }
}