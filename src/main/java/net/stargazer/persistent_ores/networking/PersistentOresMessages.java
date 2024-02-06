package net.stargazer.persistent_ores.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.stargazer.persistent_ores.PersistentOres;
import net.stargazer.persistent_ores.networking.packet.DrillAnimationSyncC2SPacket;
import net.stargazer.persistent_ores.networking.packet.DrillDensitySyncC2SPacket;
import net.stargazer.persistent_ores.networking.packet.DrillEnergySyncC2SPacket;

public class PersistentOresMessages
{
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id()
    {
        return packetId++;
    }

    public static void register()
    {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(PersistentOres.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DrillAnimationSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillAnimationSyncC2SPacket::new)
                .encoder(DrillAnimationSyncC2SPacket::toBytes)
                .consumerMainThread(DrillAnimationSyncC2SPacket::handle)
                .add();

        net.messageBuilder(DrillEnergySyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillEnergySyncC2SPacket::new)
                .encoder(DrillEnergySyncC2SPacket::toBytes)
                .consumerMainThread(DrillEnergySyncC2SPacket::handle)
                .add();

        net.messageBuilder(DrillDensitySyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillDensitySyncC2SPacket::new)
                .encoder(DrillDensitySyncC2SPacket::toBytes)
                .consumerMainThread(DrillDensitySyncC2SPacket::handle)
                .add();
    }


    public static <MSG> void sendToServer(MSG message)
    {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message)
    {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
