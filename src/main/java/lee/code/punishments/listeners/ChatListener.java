package lee.code.punishments.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import lee.code.punishments.Punishments;
import lee.code.punishments.database.cache.CachePlayers;
import lee.code.punishments.lang.Lang;
import lee.code.punishments.util.CoreUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class ChatListener implements Listener {
  private final Punishments punishments;

  public ChatListener(Punishments punishments) {
    this.punishments = punishments;
  }

  @EventHandler
  public void onMutedChat(AsyncChatEvent e) {
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    final Player player = e.getPlayer();
    final UUID uuid = player.getUniqueId();
    if (!cachePlayers.isMuted(uuid)) return;
    if (cachePlayers.isTempMuted(uuid)) {
      if (cachePlayers.isTempMuteOver(uuid)) {
        cachePlayers.unmutePlayer(uuid);
        return;
      }
      e.setCancelled(true);
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_TEMP_MUTE_TARGET_MESSAGE.getComponent(new String[]{CoreUtil.parseTime(cachePlayers.getTempMuteTime(uuid)), cachePlayers.getMuteReason(uuid)})));
      return;
    }
    e.setCancelled(true);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_MUTE_TARGET_MESSAGE.getComponent(new String[]{cachePlayers.getMuteReason(uuid)})));
  }

  @EventHandler
  public void onMutedCommand(PlayerCommandPreprocessEvent e) {
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    final Player player = e.getPlayer();
    final UUID uuid = player.getUniqueId();
    if (cachePlayers.isCuffed(uuid)) {
      e.setCancelled(true);
      return;
    }
    final String cmd = e.getMessage().contains(" ") ? e.getMessage().split(" ")[0] : e.getMessage();
    if (!CoreUtil.getMuteCommands().contains(cmd)) return;
    if (!cachePlayers.isMuted(uuid)) return;
    if (cachePlayers.isTempMuted(uuid)) {
      if (cachePlayers.isTempMuteOver(uuid)) {
        cachePlayers.unmutePlayer(uuid);
        return;
      }
      e.setCancelled(true);
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_TEMP_MUTE_TARGET_MESSAGE.getComponent(new String[]{CoreUtil.parseTime(cachePlayers.getTempMuteTime(uuid)), cachePlayers.getMuteReason(uuid)})));
      return;
    }
    e.setCancelled(true);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_MUTE_TARGET_MESSAGE.getComponent(new String[]{cachePlayers.getMuteReason(uuid)})));
  }
}
