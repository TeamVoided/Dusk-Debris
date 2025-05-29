@file:Suppress("unused")

package org.teamvoided.dusk_debris.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.text.Text

fun GuiGraphics.drawAround(x: Number, y: Number, scale: Int = 5, color: Int = 0xff_ff_00_00.toInt()) {
    this.fill(x.toInt() - scale, y.toInt() - scale, x.toInt() + scale, y.toInt() + scale, color)
}

fun GuiGraphics.text(text: String, x: Number, y: Number, color: Number = 0xff_ff_ff_ff) {
    this.drawText(MinecraftClient.getInstance().textRenderer, text, x.toInt(), y.toInt(), color.toInt(), true)
}
fun GuiGraphics.text(text: Text, x: Number, y: Number, color: Number = 0xff_ff_ff_ff) {
    this.drawText(MinecraftClient.getInstance().textRenderer, text, x.toInt(), y.toInt(), color.toInt(), true)
}

fun GuiGraphics.centeredText(text: String, x: Number, y: Number, color: Number = 0xff_ff_ff_ff) {
    this.drawCenteredShadowedText(MinecraftClient.getInstance().textRenderer, text, x.toInt(), y.toInt(), color.toInt())
}