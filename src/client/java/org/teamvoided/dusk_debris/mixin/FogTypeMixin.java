package org.teamvoided.dusk_debris.mixin;

import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(FogType.class)
public class FogTypeMixin {
//    @Mutable
//    @Shadow
//    @Final
//    private static CameraSubmersionType[] field_27889;
//
//    @Invoker("<init>")
//    private static CameraSubmersionType invokeInit(String name) {
//        throw new AssertionError();
//    }
//
//    @Inject(method = "<clinit>", at = @At("RETURN"))
//    private static void clInit(CallbackInfo ci) {
//        register("CUSTOM");
//    }
//
//    @SuppressWarnings({"UnusedReturnValue"})
//    @Unique
//    private static CameraSubmersionType register(String name) {
//        ArrayList<CameraSubmersionType> values = new ArrayList<>(Arrays.asList(field_27889));
//        CameraSubmersionType type = invokeInit(name);
//        values.add(type);
//        field_27889 = values.toArray(new CameraSubmersionType[]{});
//        return type;
//    }
}