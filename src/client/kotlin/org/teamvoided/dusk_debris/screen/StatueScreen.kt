package org.teamvoided.dusk_debris.screen

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.layout.HeaderFooterLayoutWidget
import net.minecraft.entity.Entity
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.net.StatueUpdatePayload
import org.teamvoided.dusk_debris.screen.widget.EntityModelListWidget
import org.teamvoided.dusk_debris.screen.widget.EntityModelWidget

class StatueScreen(val statue: StatueBlockEntity) : Screen(TITLE) {
    var entity: Entity? = null
    var entityWidget: EntityModelWidget? = null
//    var test: EntityModelWidget? = null
    val layout: HeaderFooterLayoutWidget = HeaderFooterLayoutWidget(this)
    var modelList: EntityModelListWidget? = null


    override fun init() {
        super.init()
        entity = statue.entityType.create(client!!.world)
        entityWidget = EntityModelWidget(48, 48, entity, true)
        entityWidget?.let { entityWidget ->
            entityWidget.x = 16
            entityWidget.y = 16
            addDrawableSelectableElement(entityWidget)
        }
        /*test = EntityModelWidget(48, 48, EntityType.SKELETON, true)
        test?.let { test ->
            test.isDisplay {
                ClientPlayNetworking.send(StatueUpdatePayload(statue.pos, it.entity?.type))
                entity = it.entity
                entityWidget?.let { ew -> ew.entity = it.entity }
            }
            test.x = (width / 2) - 24
            test.y = (height / 2) - 24
            addDrawableSelectableElement(test)
        }*/
        modelList = this.layout.addToContents(EntityModelListWidget(client, this.width, this))
        modelList?.let { list ->
            list.addEntries(Registries.ENTITY_TYPE.map { type ->
                EntityModelWidget(128, 48, type, true).let { widget ->
                    widget.isDisplay { button->
                        ClientPlayNetworking.send(StatueUpdatePayload(statue.pos, type))
                        entity = button.entity
                        entityWidget?.let { ew -> ew.entity = button.entity }
                    }
//                    widget.scaleModifier = 1.1f
                    widget
                }
            })
        }
        layout.visitWidgets(::addDrawableSelectableElement)
        repositionElements()
    }

    override fun repositionElements() {
        this.layout.arrangeElements()
        this.modelList?.setDimensionsWithLayout(this.width, this.layout)
    }

    companion object {
        val TITLE = Text.literal("Gay Screen")
    }
}