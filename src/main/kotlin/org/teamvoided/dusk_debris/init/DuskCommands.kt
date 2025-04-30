package org.teamvoided.dusk_debris.init

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.RegistryEntryArgumentType
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.registry.Holder
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.util.spellController
import org.teamvoided.dusk_debris.util.toBlockPos
import org.teamvoided.dusk_debris.util.variant

object DuskCommands {
    fun init() = CommandRegistrationCallback.EVENT.register { dispatcher, ctx, _ ->
        val sniffers = literal("sniffers").executes(this::sniffer).build()
        dispatcher.root.addChild(sniffers)

        val worldEvent = literal("worldEvent").build()
        dispatcher.root.addChild(worldEvent)
        val eventID = argument("eventID", IntegerArgumentType.integer()).executes { cx ->
            val world = cx.source.world
            val pos = cx.source.position
            val eventID = IntegerArgumentType.getInteger(cx, "eventID")
            world.syncWorldEvent(eventID, pos.toBlockPos(), 0)

            0
        }.build()
        worldEvent.addChild(eventID)

        val spell = literal("spell").build()
        dispatcher.root.addChild(spell)
        val spellType = argument("spell_id", RegistryEntryArgumentType.registryEntry(ctx, DuskRegistryKeys.SPELL))
            .executes { spell(it, RegistryEntryArgumentType.getRegistryEntry(it, "spell_id", DuskRegistryKeys.SPELL)) }
            .build()
        spell.addChild(spellType)
    }

    fun spell(cx: CommandContext<ServerCommandSource>, registryEntry: Holder.Reference<Spell<*, *>>): Int {
        val player = cx.source.player ?: return 0
        player.spellController.setSpell(player, registryEntry)
        player.sendMessage(Text.literal("applied spell " + registryEntry.key.toString()), false)
        return 1
    }

    fun sniffer(cx: CommandContext<ServerCommandSource>): Int {
        val world = cx.source.world
        val player = cx.source.player ?: return 0
        var offset = 0.0
        world.registryManager.get(DuskRegistryKeys.SNIFFER_VARIANT).holders().forEach {
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
            name.text = Text.literal(it.key.get().value.toString())
            name.addScoreboardTag("summoned_with_command")
            world.spawnEntity(name)

            offset += EntityType.SNIFFER.width * 2
        }
        return 1
    }
}