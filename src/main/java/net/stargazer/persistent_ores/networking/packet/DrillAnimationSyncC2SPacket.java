package net.stargazer.persistent_ores.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.stargazer.persistent_ores.block.entity.PersistentDrillBlockEntity;

import java.util.function.Supplier;

public class DrillAnimationSyncC2SPacket
{
    private final int[] syncData;
    private final BlockPos pos;

    public DrillAnimationSyncC2SPacket(int spin, int density, BlockPos pos)
    {
        this.syncData = new int[]{ spin, density };
        this.pos = pos;
    }

    public DrillAnimationSyncC2SPacket(FriendlyByteBuf buf)
    {
        syncData = buf.readVarIntArray();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeVarIntArray(syncData);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context content = supplier.get();

        content.enqueueWork(() ->
        {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PersistentDrillBlockEntity blockEntity)
            {
                blockEntity.setSpinS2C(syncData[0]);
                blockEntity.setDensityS2C(syncData[1]);
            }
        });

        return true;
    }
}
