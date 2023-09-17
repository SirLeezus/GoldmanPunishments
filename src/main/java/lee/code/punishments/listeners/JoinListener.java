package lee.code.punishments.listeners;

import lee.code.punishments.Punishments;
import lee.code.punishments.database.cache.CachePlayers;
import lee.code.punishments.lang.Lang;
import lee.code.punishments.util.CoreUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class JoinListener  implements Listener {
  private final Punishments punishments;

  public JoinListener(Punishments punishments) {
    this.punishments = punishments;
  }

  @EventHandler
  public void onPlayerJoin(AsyncPlayerPreLoginEvent e) {
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    final UUID uuid = e.getUniqueId();
    if (!cachePlayers.hasPlayerData(uuid)) cachePlayers.createPlayerData(uuid);
    if (!cachePlayers.isBanned(uuid)) return;
    if (cachePlayers.isTempBanned(uuid)) {
      if (cachePlayers.isTempBanOver(uuid)) {
        cachePlayers.unbanPlayer(uuid);
        return;
      }
      e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
      e.kickMessage(Lang.COMMAND_TEMP_BAN_KICK_MESSAGE.getComponent(new String[]{CoreUtil.parseTime(cachePlayers.getTempBanTime(uuid)), cachePlayers.getBanReason(uuid)}));
      return;
    }
    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
    e.kickMessage(Lang.COMMAND_BAN_KICK_MESSAGE.getComponent(new String[]{cachePlayers.getBanReason(uuid)}));
  }
}
