package org.teamvoided.dusk_debris.entity.gunpowder_barrel

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.TntMinecartRenderer
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity

@Environment(EnvType.CLIENT)
class GunpowderBarrelEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<GunpowderBarrelEntity>(context) {
    private val blockRenderManager: BlockRenderDispatcher

    init {
        this.shadowRadius = 0.5f
        this.blockRenderManager = context.blockRenderDispatcher
    }

    override fun render(
        gunpowderBlockEntity: GunpowderBarrelEntity,
        f: Float,
        g: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        i: Int
    ) {
        matrices.pushPose()
        matrices.translate(0.0f, 0.5f, 0.0f)
        val fuse = gunpowderBlockEntity.fuse
        if (fuse.toFloat() - g + 1.0f < 10.0f) {
            var h = 1.0f - (fuse.toFloat() - g + 1.0f) / 10.0f
            h = Mth.clamp(h, 0.0f, 1.0f)
            h *= h
            h *= h
            val k = 1.0f + h * 0.3f
            matrices.scale(k, k, k)
        }

        matrices.mulPose(Axis.YP.rotationDegrees(-90.0f))
        matrices.translate(-0.5f, -0.5f, 0.5f)
        matrices.mulPose(Axis.YP.rotationDegrees(90.0f))
        TntMinecartRenderer.renderWhiteSolidBlock(
            this.blockRenderManager,
            gunpowderBlockEntity.blockState,
            matrices,
            vertexConsumers,
            i,
            fuse / 5 % 2 == 0
        )
        matrices.popPose()
        super.render(gunpowderBlockEntity, f, g, matrices, vertexConsumers, i)
    }

    override fun getTextureLocation(gunpowderBarrelEntity: GunpowderBarrelEntity): ResourceLocation {
        return TextureAtlas.LOCATION_BLOCKS
    }
}