package net.staraiz.persistent_ores.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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
import net.staraiz.persistent_ores.gui.ArdentDrillMenu;
import net.staraiz.persistent_ores.item.ModuleItem;
import net.staraiz.persistent_ores.item.PersistentOresItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ArdentDrillBlockEntity extends BlockEntity implements MenuProvider
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
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private static final int OUTPUT_SLOT = 0;
    private static final int[] MODULE_SLOTS = {1, 2, 3};

    private Item yield;
    private String yieldText = "none";
    private int density = 0;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;
    private int baseEnergyCost = 16;

    public ArdentDrillBlockEntity(BlockPos pos, BlockState state)
    {
        super(PersistentOresBlockEntities.ARDENT_DRILL.get(), pos, state);

        this.data = new ContainerData()
        {
            @Override
            public int get(int i)
            {
                return switch (i)
                {
                    case 0 -> ArdentDrillBlockEntity.this.progress;
                    case 1 -> ArdentDrillBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value)
            {
                switch (i)
                {
                    case 0 -> ArdentDrillBlockEntity.this.progress = value;
                    case 1 -> ArdentDrillBlockEntity.this.maxProgress = value;
                };
            }

            @Override
            public int getCount()
            {
                return 2;
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
        return new ArdentDrillMenu(id, playerInventory, this, this.data);
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

    public static void tick(Level level, BlockPos pos, BlockState state, ArdentDrillBlockEntity entity)
    {
        if (level.isClientSide())
        {
            return;
        }

        var modCalc = entity.getModuleCalculations();

        if (entity.canProcess(modCalc))
        {
            entity.increaseProgress(modCalc);

            if (entity.progress > entity.maxProgress)
            {
                entity.drillItem();
                entity.progress -= entity.maxProgress;
            }
        }
    }

    private boolean canProcess(ModuleCalculations modCalc)
    {
        boolean hasPower = energyHandler.getEnergyStored() > baseEnergyCost;
        ItemStack outputItem = itemHandler.getStackInSlot(OUTPUT_SLOT);
        boolean slotNotFull = outputItem.isEmpty() || outputItem.getCount() < outputItem.getMaxStackSize();
        boolean isHighEnoughRank = 1 + modCalc.rank >= density;

        return slotNotFull && hasPower && isHighEnoughRank;
    }

    private void increaseProgress(ModuleCalculations modCalc)
    {
        float A = 0;
        float B = energyHandler.getMaxEnergyStored() * 0.2f;
        float T = energyHandler.getEnergyStored();
        float efficiency = Math.min(1, (T - A)/(B - A));

        energyHandler.extractEnergy((int)((baseEnergyCost * modCalc.multiplier) * efficiency), false);
        progress += (int)((density + modCalc.productivity) * efficiency);
    }

    private void drillItem()
    {
        ItemStack result = new ItemStack(yield, 1);
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    public void setYield(String yield, int density)
    {
        yieldText = yield;
        this.yield = getYield(yield);
        this.density = density;
    }

    private Item getYield(String yield)
    {
        return PersistentOresItems.CLUSTERS.get(yield).get().asItem();
    }

    private class ModuleCalculations
    {
        public int rank = 0;
        public float multiplier = 0;
        public int productivity = 0;
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
}
