package gg.moonflower.etched.common.network;

import gg.moonflower.etched.common.network.play.*;
import gg.moonflower.etched.common.network.play.handler.EtchedClientPlayPacketHandler;
import gg.moonflower.etched.common.network.play.handler.EtchedServerPlayPacketHandler;
import gg.moonflower.etched.core.Etched;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class EtchedMessages {

    public static final PacketType<ClientboundInvalidEtchUrlPacket> CLIENTBOUND_INVALID_ETCH_URL = PacketType.create(new ResourceLocation(Etched.MOD_ID, "clientbound_invalid_etch_url"), ClientboundInvalidEtchUrlPacket::new);
    public static final PacketType<ClientboundPlayEntityMusicPacket> CLIENTBOUND_PLAY_ENTITY_MUSIC = PacketType.create(new ResourceLocation(Etched.MOD_ID, "clientbound_play_entity_music"), ClientboundPlayEntityMusicPacket::new);
    public static final PacketType<ClientboundPlayMusicPacket> CLIENTBOUND_PLAY_MUSIC = PacketType.create(new ResourceLocation(Etched.MOD_ID, "clientbound_play_music"), ClientboundPlayMusicPacket::new);
    public static final PacketType<ClientboundSetUrlPacket> CLIENTBOUND_SET_URL = PacketType.create(new ResourceLocation(Etched.MOD_ID, "clientbound_set_url"), ClientboundSetUrlPacket::new);
    public static final PacketType<ServerboundEditMusicLabelPacket> SERVERBOUND_EDIT_MUSIC_LABEL = PacketType.create(new ResourceLocation(Etched.MOD_ID, "serverbound_edit_music_label"), ServerboundEditMusicLabelPacket::new);
    public static final PacketType<ServerboundSetUrlPacket> SERVERBOUND_SET_URL = PacketType.create(new ResourceLocation(Etched.MOD_ID, "serverbound_set_url"), ServerboundSetUrlPacket::new);
    public static final PacketType<SetAlbumJukeboxTrackPacket> SET_ALBUM_JUKEBOX_TRACK = PacketType.create(new ResourceLocation(Etched.MOD_ID, "set_album_jukebox_track"), SetAlbumJukeboxTrackPacket::new);

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(CLIENTBOUND_INVALID_ETCH_URL, (packet, player, responseSender) -> EtchedClientPlayPacketHandler.handleSetInvalidEtch(packet));
        ClientPlayNetworking.registerGlobalReceiver(CLIENTBOUND_PLAY_ENTITY_MUSIC, (packet, player, responseSender) -> EtchedClientPlayPacketHandler.handlePlayEntityMusicPacket(packet));
        ClientPlayNetworking.registerGlobalReceiver(CLIENTBOUND_PLAY_MUSIC, (packet, player, responseSender) -> EtchedClientPlayPacketHandler.handlePlayMusicPacket(packet));
        ClientPlayNetworking.registerGlobalReceiver(CLIENTBOUND_SET_URL, (packet, player, responseSender) -> EtchedClientPlayPacketHandler.handleSetUrl(packet));

        ClientPlayNetworking.registerGlobalReceiver(SET_ALBUM_JUKEBOX_TRACK, (packet, player, responseSender) -> EtchedClientPlayPacketHandler.handleSetAlbumJukeboxTrack(packet));
    }

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(SERVERBOUND_EDIT_MUSIC_LABEL, (packet, player, responseSender) -> {
            EtchedServerPlayPacketHandler.handleEditMusicLabel(packet, player);
        });
        ServerPlayNetworking.registerGlobalReceiver(SERVERBOUND_SET_URL, (packet, player, responseSender) -> {
            EtchedServerPlayPacketHandler.handleSetUrl(packet, player);
        });

        ServerPlayNetworking.registerGlobalReceiver(SET_ALBUM_JUKEBOX_TRACK, (packet, player, responseSender) -> {
            EtchedServerPlayPacketHandler.handleSetAlbumJukeboxTrack(packet, player);
        });
    }
}
