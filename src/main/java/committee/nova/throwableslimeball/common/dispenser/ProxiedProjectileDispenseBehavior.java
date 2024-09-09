package committee.nova.throwableslimeball.common.dispenser;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ProxiedProjectileDispenseBehavior extends DefaultDispenseItemBehavior {
    private final ProjectileItem projectileItem;
    private final ProjectileItem.DispenseConfig dispenseConfig;

    public ProxiedProjectileDispenseBehavior(ProjectileItem projectileitem) {
        this.projectileItem = projectileitem;
        this.dispenseConfig = projectileitem.createDispenseConfig();
    }

    @Override
    public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
        Level level = blockSource.level();
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        Position position = this.dispenseConfig.positionFunction().getDispensePosition(blockSource, direction);
        Projectile projectile = this.projectileItem.asProjectile(level, position, itemStack, direction);
        this.projectileItem
                .shoot(
                        projectile,
                        direction.getStepX(),
                        direction.getStepY(),
                        direction.getStepZ(),
                        this.dispenseConfig.power(),
                        this.dispenseConfig.uncertainty()
                );
        level.addFreshEntity(projectile);
        itemStack.shrink(1);
        return itemStack;
    }

    @Override
    protected void playSound(BlockSource blockSource) {
        blockSource.level().levelEvent(this.dispenseConfig.overrideDispenseEvent().orElse(1002), blockSource.pos(), 0);
    }
}
