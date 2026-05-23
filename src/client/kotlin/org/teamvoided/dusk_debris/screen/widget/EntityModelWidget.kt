package org.teamvoided.dusk_debris.screen.widget

import com.mojang.blaze3d.platform.Lighting
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ComponentPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.FocusNavigationEvent
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.CommonComponents
import net.minecraft.util.Mth
import net.minecraft.util.Mth.lerp
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import org.teamvoided.dusk_debris.util.text
import kotlin.math.min

class EntityModelWidget(
    width: Int, height: Int,
    var entity: Entity?,
    val fixed: Boolean = false,
) : AbstractWidget(0, 0, width, height, CommonComponents.EMPTY) {
    var name: Boolean = false
    var sound: Boolean = false
    var clickAction: (EntityModelWidget) -> Unit = {}
    var scaleModifier: Float = 1f

    constructor(width: Int, height: Int, type: EntityType<*>, fixed: Boolean = false)
            : this(width, height, type.create(Minecraft.getInstance().level), fixed)

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

    override fun renderWidget(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        if (entity == null) return
        val entityHeight = (entity!!.bbHeight * scaleModifier * lerp(delta, lastSizeScaler, sizeScaler))
        graphics.pose().pushPose()
        graphics.pose().translate(
            this.x.toFloat() + this.getWidth().toFloat() / 2.0f,
            (this.y + this.getHeight()).toFloat(),
            Z_OFFSET
        )
        val scale: Float = this.getHeight().toFloat() / min(entityHeight, -1f)
        graphics.pose().rotateAround(Axis.XP.rotationDegrees(this.pitch), 0f, scale, 0f)
        graphics.pose().mulPose(Axis.YP.rotationDegrees(this.yaw))
        graphics.pose().scale(scale, scale, scale)

        graphics.flush()
        Lighting.setupForEntityInInventory(Axis.XP.rotationDegrees(this.pitch))
        Minecraft.getInstance().entityRenderDispatcher.render(
            entity!!,
            0.0, 0.0, 0.0,
            0f, 1f,
            graphics.pose(), graphics.bufferSource(),
            255
        )
        graphics.flush()
        Lighting.setupFor3DItems()
        graphics.pose().popPose()
        if (name) {
            graphics.pose().pushPose()
            graphics.pose().translate(0f, 0f, Z_OFFSET * 10)
//            graphics.text("Height: $entityHeight", x, y)
            graphics.text(entity!!.type.description, x, y + height - (Minecraft.getInstance().font.lineHeight))
            graphics.pose().popPose()
        }
        lastSizeScaler = sizeScaler
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double) {
        if (fixed) return
        this.pitch = Mth.clamp(this.pitch - deltaY.toFloat() * ROTATION_SENSITIVITY, -PITCH_LIMIT, PITCH_LIMIT)
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

    override fun updateWidgetNarration(builder: NarrationElementOutput) = Unit
    override fun isActive(): Boolean = false
    override fun nextFocusPath(event: FocusNavigationEvent): ComponentPath? = null
}
