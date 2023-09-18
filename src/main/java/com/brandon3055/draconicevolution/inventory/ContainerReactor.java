package com.brandon3055.draconicevolution.inventory;

import codechicken.lib.math.MathHelper;
import com.brandon3055.brandonscore.inventory.ContainerBCBase;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.brandon3055.draconicevolution.world.EnergyCoreStructure;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.registry.MaterialRegistry;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItem1;
import gregtech.common.items.MetaItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by brandon3055 on 10/02/2017.
 */
public class ContainerReactor extends ContainerBCBase<TileReactorCore> {

    public boolean fuelSlots = false;

    public ContainerReactor(EntityPlayer player, TileReactorCore tile) {
        super(player, tile);
        setSlotState();
    }

    public void setSlotState() {
        fuelSlots = tile.reactorState.value == TileReactorCore.ReactorState.COLD;

        inventorySlots.clear();
        inventoryItemStacks.clear();

        if (fuelSlots) {
            addPlayerSlots(44 - 31, 140);

            for (int x = 0; x < 3; x++) {
                addSlotToContainer(new SlotReactor(tile, x, 183 + (x * 18), 149));
            }

            for (int x = 0; x < 3; x++) {
                addSlotToContainer(new SlotReactor(tile, 3 + x, 183 + (x * 18), 180));
            }

//            for (int y = 0; y < 2; y++) {
//                for (int x = 0; x < 2; x++) {
//                    addSlotToContainer(new SlotReactor(tile, 3 + x + y * 2, 192 + (x * 18), 180 + (y * 18)));
//                }
//            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (tile.reactorState.value == TileReactorCore.ReactorState.COLD != fuelSlots) {
            setSlotState();
        }
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        int maxFuel = 10368 + 15;
        int installedFuel = (int) (tile.reactableFuel.value + tile.convertedFuel.value);
        int free = maxFuel - installedFuel;

        Slot slot = getSlot(slotId);
        if (slot instanceof SlotReactor && clickTypeIn == ClickType.PICKUP) {
            InventoryPlayer inventory = player.inventory;
            ItemStack stackInSlot = slot.getStack();
            ItemStack heldStack = inventory.getItemStack();

            if (!heldStack.isEmpty()) {
                int value;
                ItemStack copy = heldStack.copy();
                copy.setCount(1);

                if ((value = getFuelValue(copy)) > 0) {
                    int maxInsert = free / value;
                    int insert = Math.min(Math.min(heldStack.getCount(), maxInsert), dragType == 1 ? 1 : 64);
                    tile.reactableFuel.value += insert * value;
                    heldStack.shrink(insert);
                }
                else if ((value = getChaosValue(copy)) > 0) {
                    int maxInsert = free / value;
                    int insert = Math.min(Math.min(heldStack.getCount(), maxInsert), dragType == 1 ? 1 : 64);
                    tile.convertedFuel.value += insert * value;
                    heldStack.shrink(insert);
                }
//                else if ((value = getChaosValue(copy)) > 0) {
//                    int maxInsert = free / value;
//                    int insert = Math.min(Math.min(heldStack.stackSize, maxInsert), dragType == 1 ? 1 : 64);
//                    tile.convertedFuel.value += insert * value;
//                    heldStack.stackSize -= insert;
//                }

                if (heldStack.getCount() <= 0) {
                    inventory.setItemStack(ItemStack.EMPTY);
                }
            }
            else if (!stackInSlot.isEmpty()) {
                tile.reactableFuel.value -= getFuelValue(stackInSlot);
                tile.convertedFuel.value -= getChaosValue(stackInSlot);
                inventory.setItemStack(stackInSlot);
            }

            return ItemStack.EMPTY;
        }
        else if (slotId <= 35) {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }
        return ItemStack.EMPTY;
    }

    // Returns list with [0] = block, [1] = ingot, [2] = nugget
    public static ItemStack[] getGTDraconium(){
        Material gtDraconiumMaterial = MaterialRegistry.get("awakened_draconium");
        IBlockState gtDraconiumState = MetaBlocks.COMPRESSED.get(gtDraconiumMaterial).getBlock(gtDraconiumMaterial);
        ItemStack gtDraconiumBlock = new ItemStack(gtDraconiumState.getBlock(), 1, gtDraconiumState.getBlock().getMetaFromState(gtDraconiumState));
        ItemStack gtDraconiumIngot = OreDictUnifier.get(OrePrefix.ingot, gtDraconiumMaterial, 1);
        ItemStack gtDraconiumNugget = OreDictUnifier.get(OrePrefix.nugget, gtDraconiumMaterial, 1);
        return new ItemStack[]{gtDraconiumBlock, gtDraconiumIngot, gtDraconiumNugget};
    }

    private int getFuelValue(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        else if (stack.getItem() == Item.getItemFromBlock(DEFeatures.draconicBlock)) {
            return stack.getCount() * 1296;
        }
        else if (stack.getItem() == DEFeatures.draconicIngot) {
            return stack.getCount() * 144;
        }
        else if (stack.getItem() == DEFeatures.nugget && stack.getItemDamage() == 1) {
            return stack.getCount() * 16;
        }
        else if (MaterialRegistry.get("awakened_draconium") != null){
        }
            ItemStack[] gtDraconium = getGTDraconium();
            if (stack.getItem().equals(gtDraconium[0].getItem()) && stack.getMetadata() == gtDraconium[0].getMetadata()) {
                return stack.getCount() * 1296;
            }
            else if (stack.getItem().equals(gtDraconium[1].getItem()) && stack.getMetadata() == gtDraconium[1].getMetadata()) {
                return stack.getCount() * 144;
            }
            else if (stack.getItem().equals(gtDraconium[2].getItem()) && stack.getMetadata() == gtDraconium[2].getMetadata()) {
                return stack.getCount() * 16;
            }
        return 0;
    }

    private int getChaosValue(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        else if (stack.getItem() == DEFeatures.chaosShard && stack.getItemDamage() == 1) {
            return stack.getCount() * 1296;
        }
        else if (stack.getItem() == DEFeatures.chaosShard && stack.getItemDamage() == 2) {
            return stack.getCount() * 144;
        }
        else if (stack.getItem() == DEFeatures.chaosShard && stack.getItemDamage() == 3) {
            return stack.getCount() * 16;
        }
        return 0;
    }

    public static class SlotReactor extends Slot {

        private final TileReactorCore tile;

        public SlotReactor(TileReactorCore tile, int index, int xPosition, int yPosition) {
            super(null, index, xPosition, yPosition);
            this.tile = tile;
        }

        @Override
        public void onSlotChange(ItemStack before, ItemStack after) {
            if (!before.isEmpty() && !after.isEmpty()) {
                if (before.getItem() == after.getItem()) {
                    int i = after.getCount() - before.getCount();

                    if (i > 0) {
                        this.onCrafting(before, i);
                    }
                }
            }
        }

        @Override
        public boolean isItemValid(@Nullable ItemStack stack) {
            return false;
        }

        @Nullable
        @Override
        public ItemStack getStack() {
            int index = getSlotIndex();
            if (index < 3) {
                int fuel = MathHelper.floor(tile.reactableFuel.value);
                int block = fuel / 1296;
                int ingot = (fuel % 1296) / 144;
                int nugget = ((fuel % 1296) % 144) / 16;

                if (MaterialRegistry.get("awakened_draconium") != null) {
                    ItemStack[] gtDraconium = ContainerReactor.getGTDraconium();
                    if (index == 0 && block > 0) {
                        return new ItemStack(gtDraconium[0].getItem(), block, gtDraconium[0].getMetadata());
                    }
                    else if (index == 1 && ingot > 0) {
                        return new ItemStack(gtDraconium[1].getItem(), ingot, gtDraconium[1].getMetadata());
                    }
                    else if (index == 2 && nugget > 0) {
                        return new ItemStack(gtDraconium[2].getItem(), nugget, gtDraconium[2].getMetadata());
                    }
                }
                else{
                    if (index == 0 && block > 0) {
                        return new ItemStack(DEFeatures.draconicBlock, block);
                    }
                    else if (index == 1 && ingot > 0) {
                        return new ItemStack(DEFeatures.draconicIngot, ingot);
                    }
                    else if (index == 2 && nugget > 0) {
                        return new ItemStack(DEFeatures.nugget, nugget, 1);
                    }
                }
            }
            else {
                int chaos = MathHelper.floor(tile.convertedFuel.value);
                int block = chaos / 1296;
                int ingot = (chaos % 1296) / 144;
                int nugget = ((chaos % 1296) % 144) / 16;

                if (index == 3 && block > 0) {
                    return new ItemStack(DEFeatures.chaosShard, block, 1);
                }
                else if (index == 4 && ingot > 0) {
                    return new ItemStack(DEFeatures.chaosShard, ingot, 2);
                }
                else if (index == 5 && nugget > 0) {
                    return new ItemStack(DEFeatures.chaosShard, nugget, 3);
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public void putStack(@Nonnull ItemStack stack) {
            //this.inventory.setInventorySlotContents(this.slotIndex, stack);
            this.onSlotChanged();
        }

        @Override
        public void onSlotChanged() {
            this.tile.markDirty();
        }

        @Override
        public int getSlotStackLimit() {
            return 64;//this.inventory.getInventoryStackLimit();
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            return ItemStack.EMPTY;//this.inventory.decrStackSize(this.getSlotIndex, amount);
        }

        @Override
        public boolean isHere(IInventory inv, int slotIn) {
            return false;//inv == this.inventory && slotIn == this.getSlotIndex();
        }
    }
}
