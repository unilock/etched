package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.block.AlbumJukeboxBlock;
import gg.moonflower.etched.common.block.EtchingTableBlock;
import gg.moonflower.etched.common.block.RadioBlock;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import gg.moonflower.etched.common.blockentity.RadioBlockEntity;
import gg.moonflower.etched.common.item.PortalRadioItem;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class EtchedBlocks {

    public static final Block ETCHING_TABLE = registerWithItem("etching_table", new EtchingTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).strength(2.5F).sound(SoundType.WOOD)), new Item.Properties());
    public static final Block ALBUM_JUKEBOX = registerWithItem("album_jukebox", new AlbumJukeboxBlock(BlockBehaviour.Properties.copy(Blocks.JUKEBOX)), new Item.Properties());
    public static final Block RADIO = registerWithItem("radio", new RadioBlock(BlockBehaviour.Properties.copy(Blocks.JUKEBOX).noOcclusion()), new Item.Properties());
    public static final Item PORTAL_RADIO_ITEM = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Etched.MOD_ID, "portal_radio"), new PortalRadioItem(RADIO, new Item.Properties()));

    public static final BlockEntityType<AlbumJukeboxBlockEntity> ALBUM_JUKEBOX_BE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Etched.MOD_ID, "album_jukebox"), BlockEntityType.Builder.of(AlbumJukeboxBlockEntity::new, ALBUM_JUKEBOX).build(null));
    public static final BlockEntityType<RadioBlockEntity> RADIO_BE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Etched.MOD_ID, "radio"), BlockEntityType.Builder.of(RadioBlockEntity::new, RADIO).build(null));

    public static void init() {}

    /**
     * Registers a block with a simple item.
     *
     * @param id         The id of the block
     * @param block      The block to register
     * @param properties The properties of the item to register
     * @param <R>        The type of block being registered
     * @return The registered block
     */
    private static <R extends Block> R registerWithItem(String id, R block, Item.Properties properties) {
        return registerWithItem(id, block, object -> new BlockItem(object, properties));
    }

    /**
     * Registers a block with an item.
     *
     * @param id          The id of the block
     * @param block       The block to register
     * @param itemFactory The factory to create a new item from the registered block
     * @param <R>         The type of block being registered
     * @return The registered block
     */
    private static <R extends Block> R registerWithItem(String id, R block, Function<R, Item> itemFactory) {
        R register = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Etched.MOD_ID, id), block);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Etched.MOD_ID, id), itemFactory.apply(register));
        return register;
    }
}
