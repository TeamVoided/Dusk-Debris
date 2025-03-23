package org.teamvoided.dusk_debris.entity.block.treasure_chest

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.AbstractChestBlock
import net.minecraft.block.Blocks
import net.minecraft.block.ChestBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.client.block.ChestAnimationProgress
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.TexturedRenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel.Companion.BASE
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel.Companion.LATCH
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel.Companion.LID

@Environment(EnvType.CLIENT)
class TreasureChestBlockEntityRenderer<T>(ctx: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<T> where T : BlockEntity, T : ChestAnimationProgress {
    private val singleChestLid: ModelPart
    private val singleChestBase: ModelPart
    private val singleChestLatch: ModelPart
    private val doubleChestRightLid: ModelPart
    private val doubleChestRightBase: ModelPart
    private val doubleChestRightLatch: ModelPart
    private val doubleChestLeftLid: ModelPart
    private val doubleChestLeftBase: ModelPart
    private val doubleChestLeftLatch: ModelPart

    init {
        val modelPart = ctx.getLayerModelPart(DuskEntityModelLayers.TREASURE_CHEST)
        this.singleChestBase = modelPart.getChild(BASE)
        this.singleChestLid = modelPart.getChild(LID)
        this.singleChestLatch = modelPart.getChild(LATCH)
        val modelPart2 = ctx.getLayerModelPart(DuskEntityModelLayers.TREASURE_CHEST_LEFT)
        this.doubleChestRightBase = modelPart2.getChild(BASE)
        this.doubleChestRightLid = modelPart2.getChild(LID)
        this.doubleChestRightLatch = modelPart2.getChild(LATCH)
        val modelPart3 = ctx.getLayerModelPart(DuskEntityModelLayers.TREASURE_CHEST_RIGHT)
        this.doubleChestLeftBase = modelPart3.getChild(BASE)
        this.doubleChestLeftLid = modelPart3.getChild(LID)
        this.doubleChestLeftLatch = modelPart3.getChild(LATCH)
    }

    override fun render(
        entity: T,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        lightI: Int,
        overlay: Int
    ) {
        val world = entity.world
        val bl = world != null
        val blockState = if (bl) entity.cachedState
        else Blocks.CHEST.defaultState.with(ChestBlock.FACING, Direction.SOUTH)
        val chestType =
            if (blockState.contains(ChestBlock.CHEST_TYPE)) blockState.get(ChestBlock.CHEST_TYPE)
            else ChestType.SINGLE
        val block = blockState.block
        if (block is AbstractChestBlock<*>) {
            val bl2 = chestType != ChestType.SINGLE
            matrices.push()
            val f = (blockState.get(ChestBlock.FACING) as Direction).asRotation()
            matrices.translate(0.5f, 0.5f, 0.5f)
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-f))
            matrices.translate(-0.5f, -0.5f, -0.5f)
            val propertySource = block.getBlockEntitySource(blockState, world, entity.pos, true)

            var animationProgress =
                (propertySource.apply(ChestBlock.getAnimationProgressRetriever(entity)))[tickDelta]
            animationProgress = 1.0f - animationProgress
            animationProgress = 1.0f - animationProgress * animationProgress * animationProgress
            val light = (propertySource.apply(LightmapCoordinatesRetriever())).applyAsInt(lightI)
            val material = TexturedRenderLayers.getChestTexture(entity, chestType, false)
            val vertexConsumer = material.getVertexConsumer(vertexConsumers) { RenderLayer.getEntityCutout(it) }
            if (bl2) {
                if (chestType == ChestType.LEFT) {
                    this.render(
                        matrices,
                        vertexConsumer,
                        this.doubleChestRightLid,
                        this.doubleChestRightLatch,
                        this.doubleChestRightBase,
                        animationProgress,
                        light,
                        overlay
                    )
                } else {
                    this.render(
                        matrices,
                        vertexConsumer,
                        this.doubleChestLeftLid,
                        this.doubleChestLeftLatch,
                        this.doubleChestLeftBase,
                        animationProgress,
                        light,
                        overlay
                    )
                }
            } else {
                this.render(
                    matrices,
                    vertexConsumer,
                    this.singleChestLid,
                    this.singleChestLatch,
                    this.singleChestBase,
                    animationProgress,
                    light,
                    overlay
                )
            }

            matrices.pop()
        }
    }

    private fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        lid: ModelPart,
        latch: ModelPart,
        base: ModelPart,
        openFactor: Float,
        light: Int,
        overlay: Int
    ) {
        lid.pitch = -(openFactor * 1.5708f)
        latch.pitch = lid.pitch
        lid.render(matrices, vertices, light, overlay)
        latch.render(matrices, vertices, light, overlay)
        base.render(matrices, vertices, light, overlay)
    }
}