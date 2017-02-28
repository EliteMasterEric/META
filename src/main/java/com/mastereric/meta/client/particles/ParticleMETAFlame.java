package com.mastereric.meta.client.particles;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.world.World;

public class ParticleMETAFlame extends ParticleFlame{
    public ParticleMETAFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.particleRed = 0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 0F;
    }
}
