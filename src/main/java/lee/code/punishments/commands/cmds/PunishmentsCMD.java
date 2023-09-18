package lee.code.punishments.commands.cmds;

import lee.code.colors.ColorAPI;
import lee.code.playerdata.PlayerDataAPI;
import lee.code.punishments.Punishments;
import lee.code.punishments.commands.CustomCommand;
import lee.code.punishments.database.cache.CachePlayers;
import lee.code.punishments.lang.Lang;
import lee.code.punishments.util.CoreUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PunishmentsCMD extends CustomCommand {
  private final Punishments punishments;

  public PunishmentsCMD(Punishments punishments) {
    this.punishments = punishments;
  }

  @Override
  public String getName() {
    return "punishments";
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
    final CachePlayers cachePlayers = punishments.getCacheManager().getCachePlayers();
    final Map<UUID, Long> sortedPunishments = CoreUtil.sortByValue(cachePlayers.getPunishmentListData().getAllPunishments(), Comparator.reverseOrder());
    final ArrayList<UUID> players = new ArrayList<>(sortedPunishments.keySet());
    if (players.isEmpty()){
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_PUNISHED_PLAYERS.getComponent(null)));
      return;
    }
    int index;
    int page = 0;
    final int maxDisplayed = 10;
    if (args.length > 0) {
      if (CoreUtil.isPositiveIntNumber(args[0])) page = Integer.parseInt(args[0]);
    }
    int position = page * maxDisplayed + 1;
    final ArrayList<Component> lines = new ArrayList<>();
    lines.add(Lang.COMMAND_PUNISHMENTS_TITLE.getComponent(null));
    lines.add(Component.text(" "));

    for (int i = 0; i < maxDisplayed; i++) {
      index = maxDisplayed * page + i;
      if (index >= players.size()) break;
      final UUID targetID = players.get(index);

      lines.add(Lang.COMMAND_PUNISHMENTS_LINE.getComponent(new String[]{String.valueOf(position), ColorAPI.getNameColor(targetID, PlayerDataAPI.getName(targetID))})
        .hoverEvent(punishments.getPunishmentMessageManager().buildPunishmentHover(targetID))
        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment " + PlayerDataAPI.getName(targetID)))
      );
      position++;
    }
    if (lines.size() == 2) return;
    lines.add(Component.text(" "));
    lines.add(CoreUtil.createPageSelectionComponent("/punishments", page));
    for (Component line : lines) player.sendMessage(line);
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
    return new ArrayList<>();
  }
}
