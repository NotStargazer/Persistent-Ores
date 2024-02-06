package net.stargazer.persistent_ores.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.stargazer.persistent_ores.block.entity.PersistentDrillBlockEntity;

import java.util.function.Supplier;

public class DrillAnimationSyncC2SPacket
{
    private final int spin;
    private final BlockPos pos;

    public DrillAnimationSyncC2SPacket(int spin, BlockPos pos)
    {
        this.spin = spin;
        this.pos = pos;
    }

    public DrillAnimationSyncC2SPacket(FriendlyByteBuf buf)
    {
        this.spin = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(spin);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context content = supplier.get();

        content.enqueueWork(() ->
        {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PersistentDrillBlockEntity blockEntity)
            {
                blockEntity.setSpinS2C(spin);
            }
        });

        return true;
    }
}
