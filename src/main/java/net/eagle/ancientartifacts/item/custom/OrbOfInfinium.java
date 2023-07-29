package net.eagle.ancientartifacts.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;


import java.awt.*;

public class OrbOfInfinium extends Item {

    public OrbOfInfinium(Settings settings) { super(settings); }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.of("§5" + super.getName(stack).getString());
    }
}
