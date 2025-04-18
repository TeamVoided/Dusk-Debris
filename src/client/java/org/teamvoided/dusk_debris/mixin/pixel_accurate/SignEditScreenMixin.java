package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.teamvoided.dusk_debris.util.SignFunctions;

@Mixin(SignEditScreen.class)
public abstract class SignEditScreenMixin extends AbstractSignEditScreen {
    public SignEditScreenMixin(SignBlockEntity sign, boolean fromFront, boolean shouldFilterText) {
        super(sign, fromFront, shouldFilterText);
    }

    @Override
    public void renderSignBackground(GuiGraphics graphics, BlockState state) {
        SignFunctions.renderSignModelBackground(graphics, state);
    }

    @Override
    public void offsetSign(GuiGraphics graphics, BlockState state) {
        SignFunctions.offsetSign(graphics, state, this.width);
    }
}
