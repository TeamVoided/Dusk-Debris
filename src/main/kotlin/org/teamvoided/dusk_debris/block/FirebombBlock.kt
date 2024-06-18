//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.BatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.*
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.BlunderbombEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior

class FirebombBlock(settings: Settings) : BlunderbombBlock(settings) {
    override val explosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )

    override fun explode(world: World, pos: BlockPos, explosionBehavior: ExplosionBehavior) {
        val firebombRadius = 3.0
        val entitiesNearby = world.getOtherEntities(
            null, Box(
                pos.x - firebombRadius,
                pos.y - firebombRadius,
                pos.z - firebombRadius,
                pos.x + firebombRadius,
                pos.y + firebombRadius,
                pos.z + firebombRadius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.isIn(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }
        entitiesNearby.forEach {
            it.damage(it.damageSources.onFire(), 4f)
            it.fireTicks += 60
        }
        super.explode(world, pos, explosionBehavior)
    }
}