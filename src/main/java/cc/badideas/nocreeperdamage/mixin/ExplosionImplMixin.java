package cc.badideas.nocreeperdamage.mixin;

import cc.badideas.nocreeperdamage.NoCreeperDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExplosionImpl.class)
public class ExplosionImplMixin {
    @Shadow @Final
    @Mutable
    private Explosion.DestructionType destructionType;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterConstructor(
            ServerWorld world,
            Entity entity,
            DamageSource damageSource,
            ExplosionBehavior behavior,
            Vec3d pos,
            float power,
            boolean createFire,
            Explosion.DestructionType destructionType,
            CallbackInfo ci
    ) {
        if (entity instanceof CreeperEntity) {
            boolean allowCreeperDamage = world.getGameRules().getBoolean(NoCreeperDamage.CREEPER_BLOCK_DAMAGE);
            if (!allowCreeperDamage) {
                this.destructionType = Explosion.DestructionType.KEEP;
            }
        }
    }
}
