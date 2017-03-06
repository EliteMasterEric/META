package com.mastereric.meta.client.particles;

import com.mastereric.meta.Reference;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;

@SideOnly(Side.CLIENT)
public class ParticleModMaker extends Particle {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private final float oSize;
    private final double coordX;
    private final double coordY;
    private final double coordZ;

    public ParticleModMaker(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.coordX = xCoordIn;
        this.coordY = yCoordIn;
        this.coordZ = zCoordIn;
        this.prevPosX = xCoordIn + xSpeedIn;
        this.prevPosY = yCoordIn + ySpeedIn;
        this.prevPosZ = zCoordIn + zSpeedIn;
        this.posX = this.prevPosX;
        this.posY = this.prevPosY;
        this.posZ = this.prevPosZ;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
        this.oSize = this.particleScale;
        this.particleRed = 0.9F * f;
        this.particleGreen = 0.9F * f;
        this.particleBlue = f;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 30;
        TextureAtlasSprite sprite = Reference.TEXTURE_PARTICLES_MOD_MAKER[worldIn.rand.nextInt(5)];
        this.setParticleTexture(sprite);
    }

    public void moveParticle(double x, double y, double z) {
        // TODO The creation of this method was the eighth deadly sin destroy it as soon as McJty creates CompatParticle.
        // Thanks to TehNut's Version Proxy for helping me figure out how to do this.
        try {
            final Method getBox;
            final Method setBox;
            // The mappings have a breaking change from setEntityBoundingBox
            // to setBoundingBox, but their implementation and their obfuscated
            // names are the same.
            final String getName = "func_187116_l";
            final String setName = "func_187108_a";
            getBox = Particle.class.getMethod(getName);
            getBox.setAccessible(true);
            setBox = Particle.class.getMethod(setName);
            setBox.setAccessible(true);
            setBox.invoke(this, ((AxisAlignedBB) getBox.invoke(this)).offset(x, y, z));
            this.resetPositionToBB();
        } catch (Exception e) {
            LogUtility.error("ERROR: Exception when calling moveParticle()");
            LogUtility.error(e.toString());
        }
    }

    public void move(double x, double y, double z) {
        moveParticle(x, y, z);
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        int i = super.getBrightnessForRender(p_189214_1_);
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int)(f * 15.0F * 16.0F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = 1.0F - f;
        float f1 = 1.0F - f;
        f1 = f1 * f1;
        f1 = f1 * f1;
        this.posX = this.coordX + this.motionX * (double)f;
        this.posY = this.coordY + this.motionY * (double)f - (double)(f1 * 1.2F);
        this.posZ = this.coordZ + this.motionZ * (double)f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
