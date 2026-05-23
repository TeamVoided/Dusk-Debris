package org.teamvoided.dusk_debris.entity.block.treasure_chest

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BrightnessCombiner
import net.minecraft.core.Direction
import net.minecraft.world.level.block.AbstractChestBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.LidBlockEntity
import net.minecraft.world.level.block.state.properties.ChestType
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel.Companion.BASE
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel.Companion.LATCH
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel.Companion.LID

@Environment(EnvType.CLIENT)
class TreasureChestBlockEntityRenderer<T>(ctx: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<T> where T : BlockEntity, T : LidBlockEntity {
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
        val modelPart = ctx.bakeLayer(DuskEntityModelLayers.TREASURE_CHEST)
        this.singleChestBase = modelPart.getChild(BASE)
        this.singleChestLid = modelPart.getChild(LID)
        this.singleChestLatch = modelPart.getChild(LATCH)
        val modelPart2 = ctx.bakeLayer(DuskEntityModelLayers.TREASURE_CHEST_LEFT)
        this.doubleChestRightBase = modelPart2.getChild(BASE)
        this.doubleChestRightLid = modelPart2.getChild(LID)
        this.doubleChestRightLatch = modelPart2.getChild(LATCH)
        val modelPart3 = ctx.bakeLayer(DuskEntityModelLayers.TREASURE_CHEST_RIGHT)
        this.doubleChestLeftBase = modelPart3.getChild(BASE)
        this.doubleChestLeftLid = modelPart3.getChild(LID)
        this.doubleChestLeftLatch = modelPart3.getChild(LATCH)
    }

    override fun render(
        entity: T,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        lightI: Int,
        overlay: Int
    ) {
        val world = entity.level
        val bl = world != null
        val blockState = if (bl) entity.blockState
        else Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH)
        val chestType =
            if (blockState.hasProperty(ChestBlock.TYPE)) blockState.getValue(ChestBlock.TYPE)
            else ChestType.SINGLE
        val block = blockState.block
        if (block is AbstractChestBlock<*>) {
            val bl2 = chestType != ChestType.SINGLE
            matrices.pushPose()
            val f = (blockState.getValue(ChestBlock.FACING) as Direction).toYRot()
            matrices.translate(0.5f, 0.5f, 0.5f)
            matrices.mulPose(Axis.YP.rotationDegrees(-f))
            matrices.translate(-0.5f, -0.5f, -0.5f)
            val propertySource = block.combine(blockState, world, entity.blockPos, true)

            var animationProgress =
                (propertySource.apply(ChestBlock.opennessCombiner(entity)))[tickDelta]
            animationProgress = 1.0f - animationProgress
            animationProgress = 1.0f - animationProgress * animationProgress * animationProgress
            val light = (propertySource.apply(BrightnessCombiner())).applyAsInt(lightI)
            val material = Sheets.chooseMaterial(entity, chestType, false)
            val vertexConsumer = material.buffer(vertexConsumers) { RenderType.entityCutout(it) }
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

            matrices.popPose()
        }
    }

    private fun render(
        matrices: PoseStack,
        vertices: VertexConsumer,
        lid: ModelPart,
        latch: ModelPart,
        base: ModelPart,
        openFactor: Float,
        light: Int,
        overlay: Int
    ) {
        lid.xRot = -(openFactor * 1.5708f)
        latch.xRot = lid.xRot
        lid.render(matrices, vertices, light, overlay)
        latch.render(matrices, vertices, light, overlay)
        base.render(matrices, vertices, light, overlay)
    }
}