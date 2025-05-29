package org.teamvoided.dusk_debris.screen.widget

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.list.ElementListWidget
import org.teamvoided.dusk_debris.screen.StatueScreen
import java.util.*


@Environment(EnvType.CLIENT)
class EntityModelListWidget(client: MinecraftClient?, width: Int, var parent: StatueScreen) :
    ElementListWidget<EntityModelListWidget.EntityModelEntry>(
        client, width, parent.layout.contentsHeight, parent.layout.headerHeight, ROW_HEIGHT
    ) {
    companion object {
        private const val ROW_HEIGHT = 84
        private const val ROW_WIDTH = 310
    }

    init {
        this.centerListVertically = true
    }

    override fun getRowWidth(): Int = ROW_WIDTH

    fun addEntries(widgets: List<ClickableWidget>) {
        var i = 0
        while (i < widgets.size) {
            this.addEntry(widgets[i], if (i < widgets.size - 1) widgets[i + 1] else null)
            i += 2
        }
    }

    fun addEntry(first: ClickableWidget, second: ClickableWidget?) {
        this.addEntry(EntityModelEntry.Companion.create(first, second, this.parent))
    }

    fun addEntry(first: ClickableWidget ) {
        this.addEntry(EntityModelEntry.Companion.create(listOf(first), this.parent))
    }

    fun getHoveredButton(mouseX: Double, mouseY: Double): Optional<Element> {
        for (buttonEntry in this.children()) {
            for (element in buttonEntry.children()) {
                if (element.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(element)
                }
            }
        }
        return Optional.empty()
    }

    @Environment(EnvType.CLIENT)
    open class EntityModelEntry internal constructor(buttons: List<ClickableWidget>, val parent: Screen) :
        Entry<EntityModelEntry>() {
        val buttons: List<ClickableWidget> = buttons.toList()
        override fun children(): List<Element> = buttons
        override fun selectableChildren(): List<Selectable> = buttons
        override fun render(
            graphics: GuiGraphics, index: Int, y: Int, x: Int,
            entryWidth: Int, entryHeight: Int,
            mouseX: Int, mouseY: Int,
            hovered: Boolean, tickDelta: Float,
        ) {
            var i = 0
            val j = this.parent.width / 2 - 155

            for (clickableWidget in this.buttons) {
                clickableWidget.setPosition(j + i, y)
                clickableWidget.render(graphics, mouseX, mouseY, tickDelta)
                i += clickableWidget.width + PADDING
            }
        }

        companion object {
            private const val WIDTH_PLUS_PADDING = 160
            const val PADDING = 16

            fun create(buttons: List<ClickableWidget>, parent: Screen): EntityModelEntry {
                return EntityModelEntry(buttons, parent)
            }

            fun create(first: ClickableWidget, second: ClickableWidget?, parent: Screen): EntityModelEntry {
                return EntityModelEntry(
                    if (second == null) listOf(first) else listOf(first, second),
                    parent
                )
            }
        }
    }
}
