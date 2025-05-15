package org.teamvoided.dusk_debris.init

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.block.Blocks
import net.minecraft.command.argument.RegistryEntryArgumentType
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.registry.Holder
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Spline
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.spellController
import org.teamvoided.dusk_debris.util.toBlockPos
import org.teamvoided.dusk_debris.util.variant
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator

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

        val spline = literal("spline").executes { spline(it) }.build()
        dispatcher.root.addChild(spline)
    }

    fun spline(cx: CommandContext<ServerCommandSource>): Int {
        val world = cx.source.world
        //if (!DuskDebris.isDev()) {
        //    world.players.forEach { it.sendMessage(Text.literal("do not run the spline command"), false) }
        //    return 1
        //}
        val xSize = 100
        val zSize = 100
        val xRange = xSize * (3 / 4f)
        val zRange = zSize * (3 / 4f)
        val height = world.dimension.minY..(world.dimension.height - world.dimension.minY)
        for (x in -xSize..xSize) {
            val xSample = x / xRange
            val xAlt = x > xRange || -x > xRange
            for (z in -zSize..zSize) {
                val zSample = z / zRange //-3 * (((z / zRange).absoluteValue - (2f / 3f)).absoluteValue - (1f / 3f))
                val zAlt = z > zRange || -z > zRange

                val cont = xSample
                val eros = 0f
                val ridg = 0f
                val riFl = zSample
                val data = OverworldTerrainCreator.TerrainParametersData(
                    ToFloatFunction.createUnlimited { cont },
                    ToFloatFunction.createUnlimited { eros },
                    ToFloatFunction.createUnlimited { ridg },
                    ToFloatFunction.createUnlimited { riFl }
                )
                val spline: Spline<Float, ToFloatFunction<Float>> = OverworldTerrainCreator.offsetSpline(data, false)
                if (spline !is Spline.Multipoint<Float, ToFloatFunction<Float>>) return 1
                val the = spline.apply(0f)

                for (y in height) {
                    val ySample = (y - 128f) / 128f
                    val sampled = (the - ySample) - 0.5

                    val block2 =
                        if (sampled <= 0)
                            if (!(xAlt || zAlt) && y < 63) Blocks.BLUE_STAINED_GLASS.defaultState
                            else Blocks.AIR.defaultState
                        else if (xAlt || zAlt) Utils.getStateGlass((sampled * 16 + 1).toInt())
                        else Utils.getStateConcrete((sampled * 16 + 1).toInt())
                    world.setBlockState(BlockPos(x, y, z), block2)
                }
            }
        }
        world.players.forEach { it.sendMessage(Text.literal("spline placed"), false) }

        return 1
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