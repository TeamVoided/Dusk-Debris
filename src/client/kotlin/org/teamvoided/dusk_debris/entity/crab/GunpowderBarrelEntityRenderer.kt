package org.teamvoided.dusk_debris.entity.crab

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.TntMinecartEntityRenderer
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity

@Environment(EnvType.CLIENT)
class GunpowderBarrelEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<GunpowderBarrelEntity>(context) {
    private val blockRenderManager: BlockRenderManager

    init {
        this.shadowRadius = 0.5f
        this.blockRenderManager = context.blockRenderManager
    }

    override fun render(
        gunpowderBlockEntity: GunpowderBarrelEntity,
        f: Float,
        g: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int
    ) {
        matrices.push()
        matrices.translate(0.0f, 0.5f, 0.0f)
        val j = gunpowderBlockEntity.fuse
        if (j.toFloat() - g + 1.0f < 10.0f) {
            var h = 1.0f - (j.toFloat() - g + 1.0f) / 10.0f
            h = MathHelper.clamp(h, 0.0f, 1.0f)
            h *= h
            h *= h
            val k = 1.0f + h * 0.3f
            matrices.scale(k, k, k)
        }

        matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-90.0f))
        matrices.translate(-0.5f, -0.5f, 0.5f)
        matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(90.0f))
//        TntMinecartEntityRenderer.renderFlashingBlock(
//            this.blockRenderManager,
//            gunpowderBlockEntity.blockState,
//            matrices,
//            vertexConsumers,
//            i,
//            j / 5 % 2 == 0
//        )
        matrices.pop()
        super.render(gunpowderBlockEntity, f, g, matrices, vertexConsumers, i)
    }

    override fun getTexture(gunpowderBarrelEntity: GunpowderBarrelEntity): Identifier {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
    }
}