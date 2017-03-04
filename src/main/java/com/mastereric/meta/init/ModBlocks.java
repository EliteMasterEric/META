package com.mastereric.meta.init;

        import com.mastereric.meta.META;
        import com.mastereric.meta.Reference;
        import com.mastereric.meta.common.blocks.BlockMETA;
        import com.mastereric.meta.common.blocks.BlockModMaker;

        import com.mastereric.meta.common.blocks.tile.TileMETA;
        import com.mastereric.meta.common.blocks.tile.TileModMaker;
        import com.mastereric.meta.common.items.ItemBlockDesc;
        import net.minecraft.block.Block;
        import net.minecraft.client.renderer.block.model.ModelResourceLocation;
        import net.minecraft.item.Item;
        import net.minecraft.item.ItemBlock;
        import net.minecraft.tileentity.TileEntity;
        import net.minecraftforge.client.model.ModelLoader;
        import net.minecraftforge.fml.common.registry.GameRegistry;
        import net.minecraftforge.fml.relauncher.Side;
        import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
    public static Block blockModMaker;
    public static Block blockMETA;

    public static ItemBlock itemBlockModMaker;
    public static ItemBlock itemBlockMETA;

    public static void initializeBlocks() {
        blockModMaker = new BlockModMaker();
        itemBlockModMaker = new ItemBlockDesc(blockModMaker);
        registerBlock(blockModMaker, Reference.NAME_BLOCK_MOD_MAKER, itemBlockModMaker);
        registerTileEntity(TileModMaker.class, Reference.NAME_BLOCK_MOD_MAKER);

        blockMETA = new BlockMETA();
        itemBlockMETA = new ItemBlockDesc(blockMETA);
        registerBlock(blockMETA, Reference.NAME_BLOCK_META, itemBlockMETA);
        registerTileEntity(TileMETA.class, Reference.NAME_BLOCK_META);
    }

    @SideOnly(Side.CLIENT)
    public static void initializeBlockModels() {
        // Run this on the ClientProxy after running initializeItems.
        registerBlockModel(blockModMaker);
        registerBlockModel(blockMETA);
    }

    private static void registerBlock(Block block, String registryName) {
        registerBlock(block, registryName, new ItemBlock(block), true);
    }

    private static void registerBlock(Block block, String registryName, boolean inCreativeTab) {
        registerBlock(block, registryName, new ItemBlock(block), inCreativeTab);
    }

    private static void registerBlock(Block block, String registryName, ItemBlock itemBlock) {
        registerBlock(block, registryName, itemBlock, true);
    }

    private static void registerBlock(Block block, String registryName, ItemBlock itemBlock, boolean inCreativeTab) {
        // Set the registry name.
        block.setRegistryName(Reference.MOD_ID, registryName);
        block.setUnlocalizedName(Reference.MOD_ID + "." + registryName);
        // Add to the game registry.
        GameRegistry.register(block);
        GameRegistry.register(itemBlock, block.getRegistryName());

        if (inCreativeTab)
            block.setCreativeTab(META.creativeTab);

        System.out.println("Registered block ~ "+block.getRegistryName());

    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(Block block) {
        // Function overloads make everything simpler.
        registerBlockModel(block, 0);
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(Block block, int metadata) {
        // Register the item model.
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));

        System.out.println("Registered block model ~ " + block.getRegistryName() + " ~ " + block.getUnlocalizedName());
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        GameRegistry.registerTileEntity(tileEntityClass, id);
    }
}