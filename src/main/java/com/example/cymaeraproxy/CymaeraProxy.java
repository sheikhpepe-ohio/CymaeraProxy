package com.example.cymaeraproxy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CymaeraProxy extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("CymaeraProxy enabled!");

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity e : Bukkit.getWorlds().get(0).getEntities()) {
                    // TEMP: Detect Cymaera spiders by tag until ECS hook is added
                    if (e.getScoreboardTags().contains("CymaeraSpider")) {
                        ArmorStand proxy = e.getWorld().getNearbyEntities(e.getLocation(), 0.5, 0.5, 0.5,
                                ent -> ent instanceof ArmorStand && ent.getScoreboardTags().contains("SpiderMarker"))
                                .stream().findFirst().orElse(null);

                        if (proxy == null) {
                            proxy = e.getWorld().spawn(e.getLocation(), ArmorStand.class, stand -> {
                                stand.setInvisible(true);
                                stand.setMarker(true);
                                stand.setGravity(false);
                                stand.addScoreboardTag("SpiderMarker");
                            });
                        }

                        proxy.teleport(e.getLocation());
                    }
                }
            }
        }.runTaskTimer(this, 0L, 1L); // every tick
    }
}
