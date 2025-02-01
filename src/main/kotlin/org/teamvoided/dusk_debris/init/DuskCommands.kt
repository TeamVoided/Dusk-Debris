package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.util.variant

object DuskCommands {
    fun init() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            val root = literal("sniffers").executes { cx ->
                val world = cx.source.world
                val player = cx.source.player ?: return@executes 0
                var offset = 0.0
                world.registryManager.get(DuskRegistries.SNIFFER_VARIANT).holders().forEach {
                    val pos = player.pos.add(offset, 0.0, 0.0)

                    val sniffer = SnifferEntity(EntityType.SNIFFER, world)
                    sniffer.setPosition(pos)
                    sniffer.isInvulnerable = true
                    sniffer.variant = it
                    sniffer.isAiDisabled = true
                    sniffer.isSilent = true
                    sniffer.yaw = 0f
                    sniffer.addScoreboardTag("summoned_with_command")
                    world.spawnEntity(sniffer)
                    sniffer.isBaby
                    sniffer.setPosition(pos.add(0.0, sniffer.height.toDouble(), 0.0))
                    world.spawnEntity(sniffer)

                    val name = TextDisplayEntity(EntityType.TEXT_DISPLAY, world)
                    name.setPosition(pos.add(0.0, 3.0, 0.0))
                    name.setText(Text.literal(it.key.get().value.toString()))
                    name.addScoreboardTag("summoned_with_command")
                    world.spawnEntity(name)

                    offset += EntityType.SNIFFER.width * 2
                }
                0
            }.build()
            dispatcher.root.addChild(root)
        }
    }

}