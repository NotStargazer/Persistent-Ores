package net.stargazer.persistent_ores.block.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.stargazer.persistent_ores.gui.PersistentDrillMenu;
import net.stargazer.persistent_ores.item.ModuleItem;
import net.stargazer.persistent_ores.item.PersistentOresItems;
import net.stargazer.persistent_ores.networking.PersistentOresMessages;
import net.stargazer.persistent_ores.networking.packet.DrillAnimationSyncC2SPacket;
import net.stargazer.persistent_ores.networking.packet.DrillDensitySyncC2SPacket;
import net.stargazer.persistent_ores.networking.packet.DrillEnergySyncC2SPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.manager.SingletonAnimationFactory;

import java.util.*;

public class PersistentDrillBlockEntity extends BlockEntity implements MenuProvider, IAnimatable, WorldlyContainer
{
    private final ItemStackHandler itemHandler = new ItemStackHandler(4)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            if (slot == OUTPUT_SLOT)
            {
                return false;
            }

            if (Arrays.stream(MODULE_SLOTS).anyMatch(slotIndex -> slotIndex == slot))
            {
                return stack.getItem() instanceof ModuleItem;
            }

            return true;
        }
    };
    private final PersistentOresEnergyStorage energyHandler = new PersistentOresEnergyStorage(100000, 512)
    {
        @Override
        public void onEnergyChanged()
        {
            setChanged();
            PersistentOresMessages.sendToClients(new DrillEnergySyncC2SPacket(energyHandler.getEnergyStored(), getBlockPos()));
        }
    };

    private AnimationFactory factory = new SingletonAnimationFactory(this);

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private static final int OUTPUT_SLOT = 0;
    private static final int[] MODULE_SLOTS = {1, 2, 3};

    private Item yield;
    private String yieldText = "none";
    private int density = 0;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 2000;
    private int baseEnergyCost = 16;
    private boolean drillActive;
    private int spinSpeed;
    private float spin;

    public PersistentDrillBlockEntity(BlockPos pos, BlockState state)
    {
        super(PersistentOresBlockEntities.ARDENT_DRILL.get(), pos, state);

        this.data = new ContainerData()
        {
            @Override
            public int get(int i)
            {
                return switch (i)
                {
                    case 0 -> PersistentDrillBlockEntity.this.progress;
                    case 1 -> PersistentDrillBlockEntity.this.maxProgress;
                    case 2 -> PersistentDrillBlockEntity.this.drillActive ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value)
            {
                switch (i)
                {
                    case 0 -> PersistentDrillBlockEntity.this.progress = value;
                    case 1 -> PersistentDrillBlockEntity.this.maxProgress = value;
                    case 2 -> PersistentDrillBlockEntity.this.drillActive = value > 0;
                };
            }

            @Override
            public int getCount()
            {
                return 3;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return lazyItemHandler.cast();
        }

        if (cap == ForgeCapabilities.ENERGY)
        {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }



    @Override
    public void onLoad()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);

        PersistentOresMessages.sendToClients(new DrillAnimationSyncC2SPacket(spinSpeed, getBlockPos()));
        PersistentOresMessages.sendToClients(new DrillDensitySyncC2SPacket(density, getBlockPos()));
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    public void drop()
    {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(level, worldPosition, inventory);
        energyHandler.setEnergy(0);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.translatable("block.persistent_ores.ardent_drill_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player)
    {
        return new PersistentDrillMenu(id, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("ardent_drill.energy", energyHandler.getEnergyStored());
        tag.putInt("ardent_drill.progress", progress);
        tag.putInt("ardent_drill.density", density);
        tag.putString("ardent_drill.yield", yieldText);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        energyHandler.setEnergy(tag.getInt("ardent_drill.energy"));
        progress = tag.getInt("ardent_drill.progress");
        density = tag.getInt("ardent_drill.density");
        yieldText = tag.getString("ardent_drill.yield");
        yield = getYield(yieldText);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PersistentDrillBlockEntity entity)
    {
        if (level.isClientSide())
        {
            return;
        }

        entity.energyHandler.receiveEnergy(32, false);

        var modCalc = entity.getModuleCalculations();

        if (entity.canProcess(modCalc))
        {
            entity.increaseProgress(modCalc);

            entity.spinSpeed++;

            if (entity.progress > entity.maxProgress)
            {
                entity.drillItem();
                entity.progress -= entity.maxProgress;
            }
        }
        else
        {
            entity.spinSpeed--;
        }

        entity.spinSpeed = Math.max(0, Math.min(20, entity.spinSpeed));

        PersistentOresMessages.sendToClients(new DrillAnimationSyncC2SPacket(entity.spinSpeed, pos));
    }

    private boolean canProcess(ModuleCalculations modCalc)
    {
        boolean hasPower = energyHandler.getEnergyStored() > baseEnergyCost * modCalc.multiplier;
        ItemStack outputItem = itemHandler.getStackInSlot(OUTPUT_SLOT);
        boolean slotNotFull = outputItem.isEmpty() || outputItem.getCount() < outputItem.getMaxStackSize();
        boolean isHighEnoughRank = 1 + modCalc.rank >= density;

        drillActive = slotNotFull && hasPower && isHighEnoughRank;
        return drillActive;
    }

    public float getSpinProgress()
    {
        return spinSpeed / 20f;
    }

    public float getSpinRotation()
    {
        spin = (spin + Minecraft.getInstance().getDeltaFrameTime() * spinSpeed / 2) % 360;
        return spin;
    }

    private void increaseProgress(ModuleCalculations modCalc)
    {
        float A = 0;
        float B = energyHandler.getMaxEnergyStored() * 0.2f;
        float T = energyHandler.getEnergyStored();
        float efficiency = Math.min(1, (T - A)/(B - A));

        energyHandler.extractEnergy((int)((baseEnergyCost * modCalc.multiplier) * efficiency), false);
        progress += (int)((10 * density * modCalc.productivity) * efficiency);
    }

    private void drillItem()
    {
        ItemStack result = new ItemStack(yield, 1);
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    public void setYield(String yield, int density, BlockPos pos)
    {
        yieldText = yield;
        this.yield = getYield(yield);
        this.density = density;
        PersistentOresMessages.sendToClients(new DrillDensitySyncC2SPacket(density, pos));
    }

    private Item getYield(String yield)
    {
        return PersistentOresItems.CLUSTERS.get(yield).get().asItem();
    }

    @Override
    public void registerControllers(AnimationData animationData) {}

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public IEnergyStorage getEnergyStorage()
    {
        return energyHandler;
    }

    public List<Component> getProblems()
    {
        var modCalc = getModuleCalculations();
        boolean hasPower = energyHandler.getEnergyStored() > baseEnergyCost * modCalc.multiplier;
        ItemStack outputItem = itemHandler.getStackInSlot(OUTPUT_SLOT);
        boolean slotNotFull = outputItem.isEmpty() || outputItem.getCount() < outputItem.getMaxStackSize();
        boolean isHighEnoughRank = 1 + modCalc.rank >= density;

        List<Component> problemsList = new ArrayList<>(Collections.emptyList());

        if (!hasPower)
        {
            problemsList.add(Component.translatable("gui.persistent_drill.low_power").withStyle(ChatFormatting.RED));
        }
        if (!slotNotFull)
        {
            problemsList.add(Component.translatable("gui.persistent_drill.output_full").withStyle(ChatFormatting.RED));
        }
        if (!isHighEnoughRank)
        {
            problemsList.add(Component.translatable("gui.persistent_drill.low_rank").withStyle(ChatFormatting.RED));
        }

        return problemsList;
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        return new int[] {0,1,2,3};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction)
    {
        return i == 0;
    }

    @Override
    public int getContainerSize()
    {
        return 4;
    }

    @Override
    public boolean isEmpty()
    {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int i)
    {
        return itemHandler.getStackInSlot(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1)
    {
        return itemHandler.extractItem(i, i1, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i)
    {
        itemHandler.extractItem(i, 1, false);
        return itemHandler.getStackInSlot(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack)
    {
        itemHandler.setStackInSlot(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return level.getBlockEntity(getBlockPos()) != null;
    }

    @Override
    public void clearContent()
    {
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    private class ModuleCalculations
    {
        public int rank = 0;
        public float multiplier = 0;
        public float productivity = 1;
    }

    private ModuleCalculations getModuleCalculations()
    {
        var moduleCalculations = new ModuleCalculations();
        var preMultiplier = 0f;
        var postMultiplier = 1f;

        for (int slotIndex : MODULE_SLOTS)
        {
            var item = itemHandler.getStackInSlot(slotIndex).getItem();

            if (item instanceof ModuleItem module)
            {
                if (module.powerMultiplier < 1)
                {
                    postMultiplier *= module.powerMultiplier;
                }
                else
                {
                    preMultiplier += module.powerMultiplier;
                }

                moduleCalculations.productivity += module.productivity;
                moduleCalculations.rank = Math.max(moduleCalculations.rank, module.rank);
            }
        }

        moduleCalculations.multiplier = Math.max(1, preMultiplier) * postMultiplier;


        return moduleCalculations;
    }

    public void setEnergyS2C(int energy)
    {
        energyHandler.setEnergy(energy);
    }

    public void setSpinS2C(int spin)
    {
        this.spinSpeed = spin;
    }

    public void setDensityS2C(int density)
    {
        this.density = density;
    }
}