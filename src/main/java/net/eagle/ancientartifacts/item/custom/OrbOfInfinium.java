package net.eagle.ancientartifacts.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class OrbOfInfinium extends Item {

    public OrbOfInfinium(Settings settings) { super(settings); }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.of("§e" + super.getName(stack).getString());
    }
}