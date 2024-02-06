package net.stargazer.persistent_ores.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.stargazer.persistent_ores.block.entity.PersistentDrillBlockEntity;
import net.stargazer.persistent_ores.gui.PersistentDrillMenu;

import java.util.function.Supplier;

public class DrillEnergySyncC2SPacket
{
    private final int energy;
    private final BlockPos pos;

    public DrillEnergySyncC2SPacket(int energy, BlockPos pos)
    {
        this.energy = energy;
        this.pos = pos;
    }

    public DrillEnergySyncC2SPacket(FriendlyByteBuf buf)
    {
        energy = buf.readInt();
        pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context content = supplier.get();

        content.enqueueWork(() ->
        {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PersistentDrillBlockEntity blockEntity)
            {
                blockEntity.setEnergyS2C(energy);

                if (Minecraft.getInstance().player.containerMenu instanceof PersistentDrillMenu menu &&
                menu.getBlockEntity().getBlockPos().equals(pos))
                {
                    blockEntity.setEnergyS2C(energy);
                }
            }
        });

        return true;
    }
}
