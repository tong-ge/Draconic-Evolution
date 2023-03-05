package com.brandon3055.draconicevolution.utils;

import com.brandon3055.brandonscore.utils.MultiBlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStateMultiblockHelper extends MultiBlockHelper {
    public BlockPos invalidBlock = null;
    public String expectedBlock = null;

    public boolean checkBlock(IBlockState expectedState, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (expectedState == null) {
            return true;
        } else if (expectedState.getBlock().equals(Block.getBlockFromName("air"))) {
            return state.getBlock().isAir(state, world, pos);
        } else {
            return state.getBlock().equals(expectedState.getBlock());
        }
    }

    public void setBlock(IBlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        if (!block.equals(Blocks.AIR)) {
            world.setBlockState(pos, state);
        } else {
            world.setBlockToAir(pos);
        }

    }

    /**
     * This is called for every block in the structure when MultiBlockStorage#forEachInStructure is called.
     *
     * @param pos      is the position of the block in the world.
     * @param startPos is position 0, 0, 0 in the storage array
     * @param flag     is passed through from forEachInStructure and can be used for whatever you want.
     */
    public void forBlock(IBlockState state, World world, BlockPos pos, BlockPos startPos, int flag) {
        //You can override this method and do some custom stuff with this block
    }
}
