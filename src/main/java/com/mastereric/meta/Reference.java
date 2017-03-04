package com.mastereric.meta;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class Reference {
	public static final String MOD_ID       = "meta";
	public static final String MOD_VERSION  = "1.0";
	public static final String MC_VERSION   = "[1.11.2]";
	public static final String CLIENT_PROXY = "com.mastereric.meta.proxy.ClientProxy";
	public static final String SERVER_PROXY = "com.mastereric.meta.proxy.ServerProxy";

	public static final String NAME_ITEM_MOD = "mod";
	public static final String NAME_ITEM_MOD_DUMB = "mod_dumb";
	public static final String NAME_BLOCK_MOD_MAKER = "mod_maker";
	public static final String NAME_BLOCK_META = "meta";

    public static final String NAME_ACHIEVEMENT_PAGE= "achievements.meta";
	public static final String NAME_ACHIEVEMENT_CREATE_MOD_MAKER = "meta.create_mod_maker";
	public static final String NAME_ACHIEVEMENT_CREATE_MOD = "meta.create_mod";
	public static final String NAME_ACHIEVEMENT_CREATE_META = "meta.create_meta";
	public static final String NAME_ACHIEVEMENT_CREATE_MOD_DUMB = "meta.create_mod_dumb";
	public static final String NAME_ACHIEVEMENT_USE_MOD = "meta.use_mod";

	public static final String NAME_TEXTURE_PARTICLE_MOD_MAKER_FORMAT = "particles/mod_maker_%d";

	public static final String NAME_LANG_MOD_MAKER_PROGRESS = "gui." + MOD_ID + ".mod_maker_progress";
	public static final String NAME_LANG_MOD_MAKER_COMPLETE = "gui." + MOD_ID + ".mod_maker_complete";
	public static final String NAME_LANG_MOD_MAKER_ZERO_MULT = "gui." + MOD_ID + ".mod_maker_zero_mult";

	public static final String NAME_LANG_META_STORAGE = "gui." + MOD_ID + ".meta_storage";

	public static final int TEXTURE_PARTICLES_MOD_MAKER_SIZE = 5;

	public static TextureAtlasSprite[] TEXTURE_PARTICLES_MOD_MAKER;

	public static final ResourceLocation TEXTURE_GUI_MOD_MAKER = new ResourceLocation(MOD_ID, "textures/gui/mod_maker.png");
	public static final ResourceLocation TEXTURE_GUI_META = new ResourceLocation(MOD_ID, "textures/gui/meta.png");

	public static final int GUI_MOD_MAKER = 1;
	public static final int GUI_META = 2;
}
