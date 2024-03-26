package net.stargazer.persistent_ores.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.stargazer.persistent_ores.block.PersistentOresBlocks;
import net.stargazer.persistent_ores.block.entity.PersistentDrillBlockEntity;

public class PersistentDrillMenu extends AbstractContainerMenu
{
    public final PersistentDrillBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public PersistentDrillMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData)
    {
            this(id, playerInventory, playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public PersistentDrillMenu(int id, Inventory playerInventory, BlockEntity entity, ContainerData data)
    {
        super(PersistentOresMenuTypes.ARDENT_DRILL_MENU.get(), id);
        checkContainerSize(playerInventory, 4);
        blockEntity = (PersistentDrillBlockEntity) entity;
        level = playerInventory.player.level;
        this.data = data;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(
                handler -> addSlot(new SlotItemHandler(handler, 0, 80, 25)));

        blockEntity.getModuleCapability().ifPresent(handler -> {
                addSlot(new SlotItemHandler(handler, 0, 12, 43 - 18));
                addSlot(new SlotItemHandler(handler, 1, 12, 43));
                addSlot(new SlotItemHandler(handler, 2, 12, 43 + 18));
            }
        );

        addDataSlots(data);
    }

    public int getScaledProgress()
    {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int progressTextureSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressTextureSize / maxProgress : 0;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, PersistentOresBlocks.ARDENT_DRILL.get());
    }

    private void addPlayerInventory(Inventory playerInventory)
    {
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory)
    {
        for (int x = 0; x < 9; x++)
        {
            addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    public PersistentDrillBlockEntity getBlockEntity()
    {
        return this.blockEntity;
    }

    public boolean isProcessing()
    {
        return data.get(2) == 1;
    }
}
