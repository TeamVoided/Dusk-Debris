package org.teamvoided.dusk_debris.mixin.directional_sculk.blockstates;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Mixin(SculkShriekerBlockEntity.class)
public class SculkShriekerBlockEntityMixin extends BlockEntity {
    @Shadow
    private int warningLevel;

    public SculkShriekerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "trySummonWarden", at = @At("HEAD"), cancellable = true)
    public void trySpawnWardenBelow(ServerLevel world, CallbackInfoReturnable<Boolean> cir) {
        if (SculkDirectionalStuff.isNotUp(this.getBlockState())) {
            if (this.warningLevel >= 4) {
                for (int down = 0; down < 30; down++) {
                    var posDown = this.getBlockPos().below(down);
                    var stateDown = world.getBlockState(posDown.below());
                    if (!stateDown.is(BlockTags.REPLACEABLE)) {
                        cir.setReturnValue(SpawnUtil.trySpawnMob(EntityType.WARDEN, MobSpawnType.TRIGGERED, world, posDown, 20, 5, 6, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER).isPresent());
                        break;
                    }
                }
            }
            cir.cancel();
        }
    }
}