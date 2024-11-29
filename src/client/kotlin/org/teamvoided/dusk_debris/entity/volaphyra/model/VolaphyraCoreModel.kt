package org.teamvoided.dusk_debris.entity.volaphyra.model;


import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.util.Utils.rotate360
import org.teamvoided.dusk_debris.util.Utils.rotate90

class VolaphyraCoreModel(val root: ModelPart) : SinglePartEntityModel<AbstractVolaphyraEntity>() {
    val core = root.getChild("core")
    val tendrils_north = core.getChild("tendrils_north")
    val tendrils_south = core.getChild("tendrils_south")
    val tendrils_east = core.getChild("tendrils_east")
    val tendrils_west = core.getChild("tendrils_west")

    override fun getPart(): ModelPart {
        return this.root
    }

    override fun setAngles(
        entity: AbstractVolaphyraEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        animationProgress: Float, //h
        headYaw: Float, //i
        headPitch: Float //j
    ) {
        this.part.traverse().forEach(ModelPart::resetTransform)
//        val velocity = entity.velocity.normalize().multiply(rotate360.toDouble()).toVector3f()
//        core.pitch = velocity.x
//        core.roll = velocity.y
//        core.yaw = velocity.z
//        definitelySomethingElse.visible =
//            tuffGolemEntity.hasCustomName() && "hon hon hon" == tuffGolemEntity.name.string.lowercase()
    }

    companion object {
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val core = modelPartData.addChild(
                    "core",
                    ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                    ModelTransform.pivot(0.0F, 24.0F, 0.0F)
                )
                core.addChild(
                    "tendrils_north",
                    ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(-3.0F, 0.0F, -2.0F, 6.0F, 3.0F, 0.0F),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )
                core.addChild(
                    "tendrils_south",
                    ModelPartBuilder.create()
                        .uv(17, 16)
                        .cuboid(-3.0F, 0.0F, 2.0F, 6.0F, 3.0F, 0.0F),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )
                core.addChild(
                    "tendrils_east",
                    ModelPartBuilder.create()
                        .uv(17, 10)
                        .cuboid(-2.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )
                core.addChild(
                    "tendrils_west",
                    ModelPartBuilder.create()
                        .uv(0, 10)
                        .cuboid(2.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F),
                    ModelTransform.pivot(0.0F, 0.0F, 0.0F)
                )
                return TexturedModelData.of(modelData, 32, 32)
            }
    }
}