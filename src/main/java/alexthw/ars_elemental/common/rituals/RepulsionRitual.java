package alexthw.ars_elemental.common.rituals;

import alexthw.ars_elemental.registry.ModPotions;
import alexthw.ars_elemental.util.PosCarryMEI;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RepulsionRitual extends AbstractRitual {
    @Override
    protected void tick() {
        boolean modifier = didConsumeItem(Items.BONE);
        if (getWorld() instanceof ServerLevel level && level.getGameTime() % getBackoff() == 0 && this.tile != null) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(tile.getBlockPos()).inflate(15), modifier ? living -> living.getMobType() == MobType.UNDEAD : living -> !(living instanceof Player));
            boolean flag = false;
            for (LivingEntity entity : entities) {
                if (entity != null) {
                    flag = entity.addEffect(new PosCarryMEI(ModPotions.REPEL.get(), 200, 0, true, true, getPos()));
                    if (flag) setNeedsMana(true);
                }
            }
            if (entities.isEmpty() && !flag) {
                setBackoff(60);
            } else {
                setBackoff(0);
            }
        }
    }

    @Override
    public String getLangName() {
        return "Repulsion";
    }

    int backoff = 0;

    private int getBackoff() {
        return 20 + backoff;
    }

    private void setBackoff(int i) {
        this.backoff = i;
    }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putInt("backoff", this.backoff);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        backoff = tag.getInt("backoff");
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public ParticleColor getCenterColor() {
        return new ParticleColor(
                rand.nextInt(125),
                rand.nextInt(225),
                rand.nextInt(125));
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        return stack.getItem() == Items.BONE;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    public static String ID = "ritual_repulsion";
}
