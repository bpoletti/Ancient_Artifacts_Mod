package net.eagle.ancientartifacts.item.custom;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class WardensHeart extends Item {
    public WardensHeart(Properties properties) {super(properties); }

    @Override
    public @NonNull Component getName(net.minecraft.world.item.ItemStack stack) {
        return super.getName(stack).copy().withStyle(ChatFormatting.YELLOW);
    }
}