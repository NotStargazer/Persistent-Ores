package net.stargazer.persistent_ores.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.stargazer.persistent_ores.block.entity.PersistentDrillBlockEntity;

import java.util.function.Supplier;

public class DrillDensitySyncC2SPacket
{
    private final int density;
    private final BlockPos pos;

    public DrillDensitySyncC2SPacket(int density, BlockPos pos)
    {
        this.density = density;
        this.pos = pos;
    }

    public DrillDensitySyncC2SPacket(FriendlyByteBuf buf)
    {
        density = buf.readInt();
        pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(density);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context content = supplier.get();

        content.enqueueWork(() ->
        {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PersistentDrillBlockEntity blockEntity)
            {
                blockEntity.setDensityS2C(density);
            }
        });

        return true;
    }
}
