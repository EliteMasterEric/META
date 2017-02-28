package com.mastereric.meta.client.particles;

import com.mastereric.meta.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleClientEventHandler {
    @SubscribeEvent
    public void onPreTextureStitch(TextureStitchEvent event) {
        TextureMap map = event.getMap();
        if (map == Minecraft.getMinecraft().getTextureMapBlocks()) {
            Reference.TEXTURE_PARTICLES_MOD_MAKER = new TextureAtlasSprite[Reference.TEXTURE_PARTICLES_MOD_MAKER_SIZE];
            for (int i = 0; i < Reference.TEXTURE_PARTICLES_MOD_MAKER_SIZE; i++) {
                Reference.TEXTURE_PARTICLES_MOD_MAKER[i] = map.registerSprite(
                        new ResourceLocation(Reference.MOD_ID, String.format(Reference.NAME_TEXTURE_PARTICLE_MOD_MAKER_FORMAT, i)));
            }
        }
    }
}
