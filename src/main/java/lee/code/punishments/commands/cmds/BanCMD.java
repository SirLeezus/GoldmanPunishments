package lee.code.punishments.commands.cmds;

import lee.code.colors.ColorAPI;
import lee.code.playerdata.PlayerDataAPI;
import lee.code.punishments.Punishments;
import lee.code.punishments.commands.CustomCommand;
import lee.code.punishments.database.CacheManager;
import lee.code.punishments.lang.Lang;
import lee.code.punishments.util.CoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanCMD extends CustomCommand {
  private final Punishments punishments;

  public BanCMD(Punishments punishments) {
    this.punishments = punishments;
  }

  @Override
  public String getName() {
    return "ban";
  }

  @Override
  public boolean performAsync() {
    return false;
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
    performSender(player, args, command);
  }

  @Override
  public void performConsole(CommandSender console, String[] args, Command command) {
    performSender(console, args, command);
  }

  @Override
  public void performSender(CommandSender sender, String[] args, Command command) {
    if (args.length < 2) {
      sender.sendMessage(Lang.USAGE.getComponent(new String[]{command.getUsage()}));
      return;
    }
    final String targetString = args[0];
    final UUID targetID = PlayerDataAPI.getUniqueId(targetString);
    if (targetID == null) {
      sender.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_PLAYER_DATA.getComponent(new String[]{targetString})));
      return;
    }
    final CacheManager cacheManager = punishments.getCacheManager();
    final String reason = CoreUtil.buildStringFromArgs(args, 1);
    cacheManager.getCachePlayers().banPlayer(targetID, reason);
    final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetID);
    if (!offlineTarget.isOnline()) return;
    final Player onlineTarget = offlineTarget.getPlayer();
    if (onlineTarget == null) return;
    onlineTarget.kick(Lang.COMMAND_BAN_KICK_MESSAGE.getComponent(new String[]{reason}));
    Bukkit.getServer().sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_BAN_BROADCAST.getComponent(new String[]{ColorAPI.getNameColor(targetID, targetString), reason})));
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) return StringUtil.copyPartialMatches(args[0], CoreUtil.getOnlinePlayers(), new ArrayList<>());
    return new ArrayList<>();
  }
}
