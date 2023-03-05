package com.brandon3055.draconicevolution.utils;

import com.brandon3055.brandonscore.lib.MultiBlockStorage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

// Same as MultiblockStorage, but uses IBlockState instead of String, and has fallback block
public class BlockStateMultiblockStorage extends MultiBlockStorage{
    private BlockStateMultiblockHelper helper;
    private IBlockState[][][][] blockStorage;
    private int xPos = 0;
    private int yPos = 0;

    public BlockStateMultiblockStorage(int size, BlockStateMultiblockHelper helper) {
        super(size, helper);
        blockStorage = new IBlockState[size][size][size][2];
        this.helper = helper;
    }
    public void addRow(IBlockState[]... zRow) {
        if (zRow.length > blockStorage.length || zRow.length < blockStorage.length) {
            throw new RuntimeException("[MultiBlockStorage] Attempt to add zRow larger or smaller then defined structure size");
        } else if (xPos >= blockStorage.length) {
            throw new RuntimeException("[MultiBlockStorage] Attempt to add too many zRow's to layer");
        }
        blockStorage[xPos][yPos] = zRow;
        xPos++;
    }

    @Override
    public void newLayer() {
        xPos = 0;
        yPos++;

        if (yPos >= blockStorage.length) {
            throw new RuntimeException("[MultiBlockStorage] Attempt to add too many layers to structure");
        }
    }

    @Override
    public boolean checkStructure(World world, BlockPos startPos) {
        for (int x = 0; x < blockStorage.length; x++) {
            for (int y = 0; y < blockStorage[0].length; y++) {
                for (int z = 0; z < blockStorage[0][0].length; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!helper.checkBlock(blockStorage[x][y][z][0], world, pos.add(startPos))) {
                        if (!helper.checkBlock(blockStorage[x][y][z][1], world, pos.add(startPos))) {
                            helper.invalidBlock = startPos.add(pos);
                            helper.expectedBlock = blockStorage[x][y][z][0].getBlock().getLocalizedName();
                            return false;
                        }
                    }
                }
            }
        }

        helper.invalidBlock = null;
        return true;
    }

    @Override
    public void placeStructure(World world, BlockPos startPos) {
        for (int x = 0; x < blockStorage.length; x++) {
            for (int y = 0; y < blockStorage[0].length; y++) {
                for (int z = 0; z < blockStorage[0][0].length; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    helper.setBlock(blockStorage[x][y][z][0], world, pos.add(startPos));
                }
            }
        }
    }

    @Override
    public void forEachInStructure(World world, BlockPos startPos, int flag) {
        for (int x = 0; x < blockStorage.length; x++) {
            for (int y = 0; y < blockStorage[0].length; y++) {
                for (int z = 0; z < blockStorage[0][0].length; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    helper.forBlock(blockStorage[x][y][z][0], world, pos.add(startPos), startPos, flag);
                }
            }
        }
    }

    public void forEachBlockState(BlockPos startPos, BiConsumer<BlockPos, IBlockState> consumer) {
        for (int x = 0; x < blockStorage.length; x++) {
            for (int y = 0; y < blockStorage[0].length; y++) {
                for (int z = 0; z < blockStorage[0][0].length; z++) {
                    if (!blockStorage[x][y][z][0].getBlock().equals(Blocks.AIR)){
                        consumer.accept(new BlockPos(x, y, z).add(startPos), blockStorage[x][y][z][0]);
                    }
                }
            }
        }
    }
}
