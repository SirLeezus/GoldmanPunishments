package lee.code.punishments.managers;

import lee.code.colors.ColorAPI;
import lee.code.playerdata.PlayerDataAPI;
import lee.code.punishments.Punishments;
import lee.code.punishments.database.cache.CachePlayers;
import lee.code.punishments.lang.Lang;
import lee.code.punishments.util.CoreUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentMessageManager {
  private final Punishments punishments;

  public PunishmentMessageManager(Punishments punishments) {
    this.punishments = punishments;
  }

  public Component buildPunishmentHover(UUID uuid) {
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    final String nextLine = "\n";
    final StringBuilder hover = new StringBuilder(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_TITLE.getString() + nextLine);
    if (cachePlayers.isTempBanned(uuid)) {
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_TEMP_BANNED.getString()).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REMAINING_TIME.getString(new String[]{CoreUtil.parseTime(cachePlayers.getTempBanTime(uuid))})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getString(new String[]{cachePlayers.getWhoBanned(uuid)})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getString(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(uuid))})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getString(new String[]{cachePlayers.getBanReason(uuid)}));
    } else if (cachePlayers.isBanned(uuid)) {
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_BANNED.getString()).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getString(new String[]{cachePlayers.getWhoBanned(uuid)})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getString(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(uuid))})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getString(new String[]{cachePlayers.getBanReason(uuid)}));
    } else if (cachePlayers.isTempMuted(uuid)) {
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_TEMP_MUTED.getString()).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REMAINING_TIME.getString(new String[]{CoreUtil.parseTime(cachePlayers.getTempMuteTime(uuid))})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getString(new String[]{cachePlayers.getWhoMuted(uuid)})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getString(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(uuid))})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getString(new String[]{cachePlayers.getMuteReason(uuid)}));
    } else if (cachePlayers.isMuted(uuid)) {
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_MUTED.getString()).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getString(new String[]{cachePlayers.getWhoMuted(uuid)})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getString(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(uuid))})).append(nextLine);
      hover.append(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getString(new String[]{cachePlayers.getMuteReason(uuid)}));
    }
    return CoreUtil.parseColorComponent(hover.toString());
  }

  public void sendPunishmentMessageList(Player player, UUID targetID) {
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    final List<Component> lines = new ArrayList<>();
    lines.add(Lang.COMMAND_PUNISHMENT_LINE_TITLE.getComponent(null));
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_PUNISHMENT_LINE_TARGET_PLAYER.getComponent(new String[]{ColorAPI.getNameColor(targetID, PlayerDataAPI.getName(targetID))}));
    if (cachePlayers.isTempBanned(targetID)) {
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_TEMP_BANNED.getComponent(null));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REMAINING_TIME.getComponent(new String[]{CoreUtil.parseTime(cachePlayers.getTempBanTime(targetID))}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getComponent(new String[]{cachePlayers.getWhoBanned(targetID)}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getComponent(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(targetID))}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getComponent(new String[]{cachePlayers.getBanReason(targetID)}));
    } else if (cachePlayers.isBanned(targetID)) {
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_BANNED.getComponent(null));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getComponent(new String[]{cachePlayers.getWhoBanned(targetID)}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getComponent(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(targetID))}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getComponent(new String[]{cachePlayers.getBanReason(targetID)}));
    } else if (cachePlayers.isTempMuted(targetID)) {
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_TEMP_MUTED.getComponent(null));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REMAINING_TIME.getComponent(new String[]{CoreUtil.parseTime(cachePlayers.getTempMuteTime(targetID))}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getComponent(new String[]{cachePlayers.getWhoMuted(targetID)}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getComponent(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(targetID))}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getComponent(new String[]{cachePlayers.getMuteReason(targetID)}));
    } else if (cachePlayers.isMuted(targetID)) {
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_MUTED.getComponent(null));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED.getComponent(new String[]{cachePlayers.getWhoMuted(targetID)}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED.getComponent(new String[]{CoreUtil.parseDate(cachePlayers.getTimePunished(targetID))}));
      lines.add(Lang.COMMAND_PUNISHMENTS_LINE_HOVER_REASON.getComponent(new String[]{cachePlayers.getMuteReason(targetID)}));
    }
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_PUNISHMENT_LINE_SPLITTER.getComponent(null));
    for (Component line : lines) player.sendMessage(line);
  }
}
