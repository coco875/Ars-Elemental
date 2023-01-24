package alexthw.ars_elemental.datagen.advancement;

import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class ANCriteriaTriggers {
    public static final PlayerInteractTrigger POOF_MOB = register(new PlayerInteractTrigger(new ResourceLocation(ArsNouveau.MODID, "poof_mob")));
    public static final PlayerInteractTrigger FAMILIAR = register(new PlayerInteractTrigger(new ResourceLocation(ArsNouveau.MODID, "familiar")));
    public static final PlayerInteractTrigger CHIMERA_EXPLOSION = register(new PlayerInteractTrigger(new ResourceLocation(ArsNouveau.MODID, "chimera_explosion")));
    public static final PlayerInteractTrigger CREATE_PORTAL = register(new PlayerInteractTrigger(new ResourceLocation(ArsNouveau.MODID, "portals")));
    public static final PlayerInteractTrigger PRISMATIC = register(new PlayerInteractTrigger(new ResourceLocation(ArsNouveau.MODID, "prismatic")));
    public static final PlayerInteractTrigger SHRUNK_STARBY = register(new PlayerInteractTrigger(new ResourceLocation(ArsNouveau.MODID, "shrunk_starby")));

    public static void rewardNearbyPlayers(PlayerInteractTrigger criteria, ServerLevel level, BlockPos pos, int radius){
        AABB aabb = new AABB(pos).inflate(radius);
        for (ServerPlayer player : level.players()) {
            if (aabb.contains(player.getX(), player.getY(), player.getZ())) {
                criteria.trigger(player);
            }
        }
    }

    public static <T extends CriterionTrigger<?>> T register(T trigger) {
        return CriteriaTriggers.register(trigger);
    }

    public static void init() {}
}
