package org.teamvoided.dusk_debris.screen.widget

import com.mojang.blaze3d.lighting.DiffuseLighting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.ElementPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.navigation.GuiNavigationEvent
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.sound.SoundManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.text.CommonTexts
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.MathHelper.lerp
import org.teamvoided.dusk_debris.util.text
import kotlin.math.min

class EntityModelWidget(
    width: Int, height: Int,
    var entity: Entity?,
    val fixed: Boolean = false,
) : ClickableWidget(0, 0, width, height, CommonTexts.EMPTY) {
    var name: Boolean = false
    var sound: Boolean = false
    var clickAction: (EntityModelWidget) -> Unit = {}
    var scaleModifier: Float = 1f

    constructor(width: Int, height: Int, type: EntityType<*>, fixed: Boolean = false)
            : this(width, height, type.create(MinecraftClient.getInstance().world), fixed)

    companion object {
        const val Z_OFFSET = 100.0f
        const val ROTATION_SENSITIVITY = 2.5f
        const val DEFAULT_PITCH = -15.0f
        const val DEFAULT_YAW = 210.0f
        const val PITCH_LIMIT = 50.0f
        const val DEFAULT_SALER: Float = -1f
        const val ZOOM_RATE = 0.1f
    }

    var pitch: Float = DEFAULT_PITCH
    var yaw: Float = DEFAULT_YAW
    var sizeScaler: Float = DEFAULT_SALER
    var lastSizeScaler: Float = DEFAULT_SALER

    override fun drawWidget(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        if (entity == null) return
        val entityHeight = (entity!!.height * scaleModifier * lerp(delta, lastSizeScaler, sizeScaler))
        graphics.matrices.push()
        graphics.matrices.translate(
            this.x.toFloat() + this.getWidth().toFloat() / 2.0f,
            (this.y + this.getHeight()).toFloat(),
            Z_OFFSET
        )
        val scale: Float = this.getHeight().toFloat() / min(entityHeight, -1f)
        graphics.matrices.rotateAround(Axis.X_POSITIVE.rotationDegrees(this.pitch), 0f, scale, 0f)
        graphics.matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(this.yaw))
        graphics.matrices.scale(scale, scale, scale)

        graphics.draw()
        DiffuseLighting.setupInventoryShaderLighting(Axis.X_POSITIVE.rotationDegrees(this.pitch))
        MinecraftClient.getInstance().entityRenderDispatcher.render(
            entity!!,
            0.0, 0.0, 0.0,
            0f, 1f,
            graphics.matrices, graphics.vertexConsumers,
            255
        )
        graphics.draw()
        DiffuseLighting.setup3DGuiLighting()
        graphics.matrices.pop()
        if (name) {
            graphics.matrices.push()
            graphics.matrices.translate(0f, 0f, Z_OFFSET * 10)
//            graphics.text("Height: $entityHeight", x, y)
            graphics.text(entity!!.type.name, x, y + height - (MinecraftClient.getInstance().textRenderer.fontHeight))
            graphics.matrices.pop()
        }
        lastSizeScaler = sizeScaler
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double) {
        if (fixed) return
        this.pitch = MathHelper.clamp(this.pitch - deltaY.toFloat() * ROTATION_SENSITIVITY, -PITCH_LIMIT, PITCH_LIMIT)
        this.yaw += deltaX.toFloat() * ROTATION_SENSITIVITY
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amountX: Double, amountY: Double): Boolean {
        if (!this.visible || fixed) {
            return false
        } else {
            lastSizeScaler = sizeScaler
            sizeScaler = min(sizeScaler + (amountY.toFloat() * ZOOM_RATE), -0.01f)
            return true
        }
    }

    fun isDisplay(action: (EntityModelWidget) -> Unit) {
        sound = true
        name = true
        clickAction = action
    }

    override fun playDownSound(soundManager: SoundManager) = if (sound) super.playDownSound(soundManager) else Unit
    override fun onClick(mouseX: Double, mouseY: Double) {
        super.onClick(mouseX, mouseY)
        clickAction(this)
    }

    override fun updateNarration(builder: NarrationMessageBuilder) = Unit
    override fun isNarratable(): Boolean = false
    override fun nextFocusPath(event: GuiNavigationEvent): ElementPath? = null
}
