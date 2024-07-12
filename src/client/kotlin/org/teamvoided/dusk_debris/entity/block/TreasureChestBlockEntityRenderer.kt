package org.teamvoided.dusk_debris.entity.block

import com.mojang.blaze3d.vertex.VertexConsumer
import it.unimi.dsi.fastutil.floats.Float2FloatFunction
import it.unimi.dsi.fastutil.ints.Int2IntFunction
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.client.block.ChestAnimationProgress
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.TexturedRenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Direction
import java.util.*

@Environment(EnvType.CLIENT)
class ChestBlockEntityRenderer<T>(ctx: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<T> where T : BlockEntity?, T : ChestAnimationProgress? {
    private val singleChestLid: ModelPart
    private val singleChestBase: ModelPart
    private val singleChestLatch: ModelPart
    private val doubleChestRightLid: ModelPart
    private val doubleChestRightBase: ModelPart
    private val doubleChestRightLatch: ModelPart
    private val doubleChestLeftLid: ModelPart
    private val doubleChestLeftBase: ModelPart
    private val doubleChestLeftLatch: ModelPart
    private var christmas = false

    init {
        val calendar = Calendar.getInstance()
        if (calendar[2] + 1 == 12 && calendar[5] >= 24 && calendar[5] <= 26) {
            this.christmas = true
        }

        val modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST)
        this.singleChestBase = modelPart.getChild(BASE)
        this.singleChestLid = modelPart.getChild(LID)
        this.singleChestLatch = modelPart.getChild(LATCH)
        val modelPart2 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_LEFT)
        this.doubleChestRightBase = modelPart2.getChild(BASE)
        this.doubleChestRightLid = modelPart2.getChild(LID)
        this.doubleChestRightLatch = modelPart2.getChild(LATCH)
        val modelPart3 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_RIGHT)
        this.doubleChestLeftBase = modelPart3.getChild(BASE)
        this.doubleChestLeftLid = modelPart3.getChild(LID)
        this.doubleChestLeftLatch = modelPart3.getChild(LATCH)
    }

    override fun render(
        entity: T,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val world = entity!!.world
        val bl = world != null
        val blockState = if (bl) entity.cachedState else (Blocks.CHEST.defaultState.with(
            ChestBlock.FACING,
            Direction.SOUTH
        ) as BlockState)
        val chestType =
            if (blockState.contains(ChestBlock.CHEST_TYPE)) blockState.get(ChestBlock.CHEST_TYPE) as ChestType else ChestType.SINGLE
        val block = blockState.block
        if (block is AbstractChestBlock<*>) {
            val bl2 = chestType != ChestType.SINGLE
            matrices.push()
            val f = (blockState.get(ChestBlock.FACING) as Direction).asRotation()
            matrices.translate(0.5f, 0.5f, 0.5f)
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-f))
            matrices.translate(-0.5f, -0.5f, -0.5f)
            val propertySource = block.getBlockEntitySource(blockState, world, entity.pos, true)

            var g =
                (propertySource.apply(ChestBlock.getAnimationProgressRetriever(entity)))[tickDelta]
            g = 1.0f - g
            g = 1.0f - g * g * g
            val i = (propertySource.apply(LightmapCoordinatesRetriever())).applyAsInt(light)
            val material = TexturedRenderLayers.getChestTexture(entity, chestType, this.christmas)
            val vertexConsumer = material.getVertexConsumer(
                vertexConsumers
            ) { texture: Identifier ->
                RenderLayer.getEntityCutout(
                    texture
                )
            }
            if (bl2) {
                if (chestType == ChestType.LEFT) {
                    this.render(
                        matrices, vertexConsumer,
                        this.doubleChestRightLid,
                        this.doubleChestRightLatch,
                        this.doubleChestRightBase, g, i, overlay
                    )
                } else {
                    this.render(
                        matrices, vertexConsumer,
                        this.doubleChestLeftLid,
                        this.doubleChestLeftLatch,
                        this.doubleChestLeftBase, g, i, overlay
                    )
                }
            } else {
                this.render(
                    matrices, vertexConsumer,
                    this.singleChestLid,
                    this.singleChestLatch,
                    this.singleChestBase, g, i, overlay
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

    companion object {
        private const val BASE = "bottom"
        private const val LID = "lid"
        private const val LATCH = "lock"
        val singleTexturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "bottom",
                    ModelPartBuilder.create().uv(0, 19).cuboid(1.0f, 0.0f, 1.0f, 14.0f, 10.0f, 14.0f),
                    ModelTransform.NONE
                )
                modelPartData.addChild(
                    "lid",
                    ModelPartBuilder.create().uv(0, 0).cuboid(1.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addChild(
                    "lock",
                    ModelPartBuilder.create().uv(0, 0).cuboid(7.0f, -2.0f, 14.0f, 2.0f, 4.0f, 1.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }

        val rightDoubleTexturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "bottom",
                    ModelPartBuilder.create().uv(0, 19).cuboid(1.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f),
                    ModelTransform.NONE
                )
                modelPartData.addChild(
                    "lid",
                    ModelPartBuilder.create().uv(0, 0).cuboid(1.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addChild(
                    "lock",
                    ModelPartBuilder.create().uv(0, 0).cuboid(15.0f, -2.0f, 14.0f, 1.0f, 4.0f, 1.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }

        val leftDoubleTexturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "bottom",
                    ModelPartBuilder.create().uv(0, 19).cuboid(0.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f),
                    ModelTransform.NONE
                )
                modelPartData.addChild(
                    "lid",
                    ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                modelPartData.addChild(
                    "lock",
                    ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, -2.0f, 14.0f, 1.0f, 4.0f, 1.0f),
                    ModelTransform.pivot(0.0f, 9.0f, 1.0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}