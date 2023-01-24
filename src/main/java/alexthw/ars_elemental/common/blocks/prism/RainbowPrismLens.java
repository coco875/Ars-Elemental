package alexthw.ars_elemental.common.blocks.prism;

import java.util.Random;

import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class RainbowPrismLens extends AbstractPrismLens {

    static final Random randomSource = new Random();

    public RainbowPrismLens(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        spell.setColor(ParticleColor.makeRandomColor(255, 255, 255, randomSource).toWrapper());
        super.shoot(world, pos, spell, angle);
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell) {
        return true;
    }

    @Override
    protected String getDescriptionKey() {
        return super.getDescriptionKey() + "rgb";
    }

}
