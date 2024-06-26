package lee.code.punishments.listeners;

import lee.code.punishments.Punishments;
import lee.code.punishments.lang.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CuffListener implements Listener {
  private final Punishments punishments;

  public CuffListener(Punishments punishments) {
    this.punishments = punishments;
  }

  @EventHandler
  public void onCuffMove(PlayerMoveEvent e) {
    if (punishments.getCacheManager().getCachePlayers().isCuffed(e.getPlayer().getUniqueId())) {
      e.getPlayer().sendActionBar(Lang.ERROR_CUFFED_MOVED.getComponent(null));
      e.setCancelled(true);
    }
  }
}
