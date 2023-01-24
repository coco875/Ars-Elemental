package alexthw.ars_elemental.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class TooltipUtils {

    public static Component getShiftInfoTooltip(String type) {
        Component shift = new TextComponent("SHIFT").withStyle(ChatFormatting.AQUA);
        return new TranslatableComponent("ars_elemental." + type + ".shift_info", shift).withStyle(ChatFormatting.GRAY);
    }

    public static void addOnShift(List<Component> tooltip, Runnable lambda, String type) {
        if (Screen.hasShiftDown()) {
            lambda.run();
        } else {
            tooltip.add(getShiftInfoTooltip(type));
        }
    }

}
