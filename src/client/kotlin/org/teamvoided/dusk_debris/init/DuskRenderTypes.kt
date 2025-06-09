package org.teamvoided.dusk_debris.init

import com.mojang.blaze3d.vertex.VertexFormat.DrawMode
import com.mojang.blaze3d.vertex.VertexFormats
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters
import net.minecraft.client.render.RenderPhase
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.init.DuskShaders.STATUE_SHADER
import java.util.function.BiFunction

object DuskRenderTypes {
    @JvmField
    val STATUE: BiFunction<Identifier, Boolean, RenderLayer.MultiPhase> = Util.memoize { texture, affectsOutline ->
        val multiPhaseParameters = MultiPhaseParameters.builder()
            .shader(STATUE_SHADER)
            .texture(RenderPhase.Texture(texture, false, false))
            .transparency(RenderPhase.NO_TRANSPARENCY)
            .cull(RenderPhase.DISABLE_CULLING)
            .lightmap(RenderPhase.ENABLE_LIGHTMAP)
            .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(affectsOutline)
        RenderLayer.of(
            id("statue").toString(),
            VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
            DrawMode.QUADS, 1536, true, false, multiPhaseParameters
        )
    }
}