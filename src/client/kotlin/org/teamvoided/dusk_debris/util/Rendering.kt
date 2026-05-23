@file:Suppress("unused")

package org.teamvoided.dusk_debris.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

fun GuiGraphics.drawAround(x: Number, y: Number, scale: Int = 5, color: Int = 0xff_ff_00_00.toInt()) {
    this.fill(x.toInt() - scale, y.toInt() - scale, x.toInt() + scale, y.toInt() + scale, color)
}

fun GuiGraphics.text(text: String, x: Number, y: Number, color: Number = 0xff_ff_ff_ff) {
    this.drawString(Minecraft.getInstance().font, text, x.toInt(), y.toInt(), color.toInt(), true)
}
fun GuiGraphics.text(text: Component, x: Number, y: Number, color: Number = 0xff_ff_ff_ff) {
    this.drawString(Minecraft.getInstance().font, text, x.toInt(), y.toInt(), color.toInt(), true)
}

fun GuiGraphics.centeredText(text: String, x: Number, y: Number, color: Number = 0xff_ff_ff_ff) {
    this.drawCenteredString(Minecraft.getInstance().font, text, x.toInt(), y.toInt(), color.toInt())
}