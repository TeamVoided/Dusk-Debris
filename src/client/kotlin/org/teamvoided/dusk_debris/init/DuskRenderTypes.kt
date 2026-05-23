package org.teamvoided.dusk_debris.init

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat.Mode
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.CompositeState
import net.minecraft.resources.ResourceLocation
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.init.DuskShaders.STATUE_SHADER
import java.util.function.BiFunction

object DuskRenderTypes {
    @JvmField
    val STATUE: BiFunction<ResourceLocation, Boolean, RenderType.CompositeRenderType> = Util.memoize { texture, affectsOutline ->
        val multiPhaseParameters = CompositeState.builder()
            .setShaderState(STATUE_SHADER)
            .setTextureState(RenderStateShard.TextureStateShard(texture, false, false))
            .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(affectsOutline)
        RenderType.create(
            id("statue").toString(),
            DefaultVertexFormat.NEW_ENTITY,
            Mode.QUADS, 1536, true, false, multiPhaseParameters
        )
    }
}