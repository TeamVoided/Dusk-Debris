package org.teamvoided.dusk_debris.screen.widget

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import org.teamvoided.dusk_debris.screen.StatueScreen
import java.util.*


@Environment(EnvType.CLIENT)
class EntityModelListWidget(client: Minecraft?, width: Int, var parent: StatueScreen) :
    ContainerObjectSelectionList<EntityModelListWidget.EntityModelEntry>(
        client, width, parent.layout.contentHeight, parent.layout.headerHeight, ROW_HEIGHT
    ) {
    companion object {
        private const val ROW_HEIGHT = 84
        private const val ROW_WIDTH = 310
    }

    init {
        this.centerListVertically = true
    }

    override fun getRowWidth(): Int = ROW_WIDTH

    fun addEntries(widgets: List<AbstractWidget>) {
        var i = 0
        while (i < widgets.size) {
            this.addEntry(widgets[i], if (i < widgets.size - 1) widgets[i + 1] else null)
            i += 2
        }
    }

    fun addEntry(first: AbstractWidget, second: AbstractWidget?) {
        this.addEntry(EntityModelEntry.Companion.create(first, second, this.parent))
    }

    fun addEntry(first: AbstractWidget) {
        this.addEntry(EntityModelEntry.Companion.create(listOf(first), this.parent))
    }

    fun getHoveredButton(mouseX: Double, mouseY: Double): Optional<GuiEventListener> {
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
    open class EntityModelEntry internal constructor(buttons: List<AbstractWidget>, val parent: Screen) :
        Entry<EntityModelEntry>() {
        val buttons: List<AbstractWidget> = buttons.toList()
        override fun children(): List<GuiEventListener> = buttons
        override fun narratables(): List<NarratableEntry> = buttons
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

            fun create(buttons: List<AbstractWidget>, parent: Screen): EntityModelEntry {
                return EntityModelEntry(buttons, parent)
            }

            fun create(first: AbstractWidget, second: AbstractWidget?, parent: Screen): EntityModelEntry {
                return EntityModelEntry(
                    if (second == null) listOf(first) else listOf(first, second),
                    parent
                )
            }
        }
    }
}
