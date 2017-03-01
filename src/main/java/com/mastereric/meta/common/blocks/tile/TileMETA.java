package com.mastereric.meta.common.blocks.tile;

import com.mastereric.meta.common.inventory.METAEnergyStorageHandler;
import com.mastereric.meta.common.inventory.METAItemStackHandler;
import com.mastereric.meta.common.inventory.ModMakerItemStackHandler;
import com.mastereric.meta.common.items.ItemMod;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import sun.rmi.runtime.Log;

public class TileMETA extends TileEntity implements ITickable {
    public static final int MAX_ENERGY_STORED = 80000; // A little over half of one mod's worth of energy.
    public static final int FE_PER_MOD = 150000; // A little under 1 coal block of power.
    public static final int FE_PER_TICK = 60; // 50% higher than an Extra Utilities furnace generator.
    public static final int TICKS_PER_MOD = FE_PER_MOD / FE_PER_TICK; // 2500 ticks per mod.

    private METAItemStackHandler inventoryItemHandler = new METAItemStackHandler(1);
    private METAEnergyStorageHandler energyStorageHandler = new METAEnergyStorageHandler(MAX_ENERGY_STORED);
    private int currentRemainingTicks;
    private String customName;

    public TileMETA() {
        currentRemainingTicks = 0;
        customName = "";
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            LogUtility.info("HAS ENERGY CAPABILITY?");
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return  CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryItemHandler);
        if (capability == CapabilityEnergy.ENERGY) {
            LogUtility.info("GET ENERGY CAPABILITY");
            return  CapabilityEnergy.ENERGY.cast(energyStorageHandler);
        }

        return super.getCapability(capability, facing);
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
        tag.setInteger("RemainingTicks", getTicksRemaining());
        tag.setInteger("EnergyStorage", this.energyStorageHandler.getEnergyStored());
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        customName = tag.getString("CustomName");
        inventoryItemHandler.deserializeNBT(tag.getCompoundTag("Inventory"));
        currentRemainingTicks = tag.getInteger("RemainingTicks");
        energyStorageHandler.setEnergyStored(tag.getInteger("EnergyStorage"));
        markDirty();
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : LangUtility.getTranslation("container.meta.meta");
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
        currentRemainingTicks = compound.getInteger("RemainingTicks");
        energyStorageHandler.setEnergyStored(compound.getInteger("EnergyStorage"));
        inventoryItemHandler.deserializeNBT(compound.getCompoundTag("Inventory"));
        markDirty();

        if (compound.hasKey("CustomName")) {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("RemainingTicks", this.currentRemainingTicks);
        compound.setInteger("EnergyStorage", this.energyStorageHandler.getEnergyStored());
        compound.setTag("Inventory", inventoryItemHandler.serializeNBT());

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    public boolean isActive() {
        // Whether the M.E.T.A. is active for external visual purposes.
        return getTicksRemaining() > 0 && energyStorageHandler.getEnergyStored() < MAX_ENERGY_STORED;
    }

    private void tryConsumeMod() {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if(inventoryItemHandler.getStackInSlot(0).getItem().equals(ModItems.itemMod)
                    && getTicksRemaining() == 0) {
                LogUtility.infoSided("Consuming mod in META...");
                currentRemainingTicks = TICKS_PER_MOD;
                inventoryItemHandler.setStackInSlot(0, ItemStack.EMPTY);
                markDirty();
            }
        }
    }

    public void update() {
        tryConsumeMod();

        if (getTicksRemaining() > 0) {
            if(energyStorageHandler.getEnergyStored() < MAX_ENERGY_STORED) {
                currentRemainingTicks--;
                energyStorageHandler.setEnergyStored(energyStorageHandler.getEnergyStored() + FE_PER_TICK);
            } else {
                // On hold.
            }
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
        return (stack.getItem().equals(ModItems.itemMod));
    }

    public int getTicksRemaining() {
        //LogUtility.infoSided("Remaining: %d", currentRemainingTicks);
        return currentRemainingTicks;
    }

    public int getEnergyStored() {
        // Tell others what my current energy storage is.
        return energyStorageHandler.getEnergyStored();
    }

    public void setCurrentEnergyStorage(int currentEnergyStorage) {
        this.energyStorageHandler.setEnergyStored(currentEnergyStorage);
        markDirty();
    }

    public void setRemainingTicks(int remainingTicks) {
        //LogUtility.info("Set Remaining = %d", remainingTicks);
        this.currentRemainingTicks = remainingTicks;
        markDirty();
    }
}