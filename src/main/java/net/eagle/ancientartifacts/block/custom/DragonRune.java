package net.eagle.ancientartifacts.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;

public class DragonRune extends Block {

    public final static BooleanProperty FOSSIL_HEAD = BooleanProperty.of("fossil_head");
    public final static BooleanProperty HEART_SEA = BooleanProperty.of("heart_of_the_sea");
    public final static BooleanProperty ORB_INFINIUM = BooleanProperty.of("orb_of_infinium");




    public DragonRune(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FOSSIL_HEAD, HEART_SEA, ORB_INFINIUM);
    }

}
