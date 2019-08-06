package com.extracraftx.minecraft.extradoors.block;

import com.extracraftx.minecraft.extradoors.sound.Sounds;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GoldTrapdoorBlock extends TrapdoorBlock {

    public GoldTrapdoorBlock() {
        super(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).strength(4, 4).sounds(BlockSoundGroup.METAL)
                .build());
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hitResult) {
        if (!state.get(POWERED)) {
            state = state.cycle(OPEN);
            world.setBlockState(pos, state, 2);
            if (state.get(WATERLOGGED))
                world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            playToggleSound(player, world, pos, state.get(OPEN));
            return true;
        }
        world.playSound(player, pos, Sounds.LOCKED, SoundCategory.BLOCKS, 1.1f, world.random.nextFloat() * 0.1f + 1f);
        return true;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos updatedPos,
            boolean b) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        if(block != this && isPowered != state.get(POWERED)){
            world.setBlockState(pos, state.with(POWERED, isPowered), 2);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext placementContext) {
        BlockState state = super.getPlacementState(placementContext);
        return state.with(OPEN, false);
    }

}