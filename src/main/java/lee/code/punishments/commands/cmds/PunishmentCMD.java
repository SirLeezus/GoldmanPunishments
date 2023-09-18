package lee.code.punishments.commands.cmds;

import lee.code.colors.ColorAPI;
import lee.code.playerdata.PlayerDataAPI;
import lee.code.punishments.Punishments;
import lee.code.punishments.commands.CustomCommand;
import lee.code.punishments.database.cache.CachePlayers;
import lee.code.punishments.lang.Lang;
import lee.code.punishments.util.CoreUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PunishmentCMD extends CustomCommand {
  private final Punishments punishments;

  public PunishmentCMD(Punishments punishments) {
    this.punishments = punishments;
  }

  @Override
  public String getName() {
    return "punishment";
  }

  @Override
  public boolean performAsync() {
    return true;
  }

  @Override
  public boolean performAsyncSynchronized() {
    return false;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    punishments.getCommandManager().perform(sender, args, this, command);
    return true;
  }

  @Override
  public void perform(Player player, String[] args, Command command) {
    if (args.length < 1) {
      player.sendMessage(Lang.USAGE.getComponent(new String[]{command.getUsage()}));
      return;
    }
    final String targetString = args[0];
    final UUID targetID = PlayerDataAPI.getUniqueId(targetString);
    if (targetID == null) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_PLAYER_DATA.getComponent(new String[]{targetString})));
      return;
    }
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    if (!cachePlayers.isMuted(targetID) && !cachePlayers.isBanned(targetID)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_PUNISHMENT_NOT_PUNISHED.getComponent(new String[]{ColorAPI.getNameColor(targetID, targetString)})));
      return;
    }
    punishments.getPunishmentMessageManager().sendPunishmentMessageList(player, targetID);
  }

  @Override
  public void performConsole(CommandSender console, String[] args, Command command) {
    console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
  }

  @Override
  public void performSender(CommandSender sender, String[] args, Command command) {

  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) return StringUtil.copyPartialMatches(args[0], CoreUtil.getOnlinePlayers(), new ArrayList<>());
    return new ArrayList<>();
  }
}
