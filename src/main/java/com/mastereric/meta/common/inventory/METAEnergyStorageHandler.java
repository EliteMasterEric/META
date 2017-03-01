package com.mastereric.meta.common.inventory;

import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.energy.EnergyStorage;

public class METAEnergyStorageHandler extends EnergyStorage {
    // The maximum amount received per tick. Since this is a generator this is 0.
    public static final int MAX_RECEIVE = 0;
    // The maximum output per tick.
    public static final int MAX_EXTRACT = 1280;

    public METAEnergyStorageHandler(int capacity) {
        super(capacity, MAX_RECEIVE, MAX_EXTRACT);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        LogUtility.info("EXTRACT ENERGY?");
        return super.extractEnergy(maxExtract, simulate);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }
}
