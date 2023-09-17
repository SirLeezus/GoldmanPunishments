package lee.code.punishments.commands;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lee.code.punishments.Punishments;
import lee.code.punishments.commands.cmds.*;
import lee.code.punishments.lang.Lang;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
  private final Punishments punishments;
  @Getter private final Set<CustomCommand> commands = ConcurrentHashMap.newKeySet();
  private final ConcurrentHashMap<UUID, ScheduledTask> asyncTasks = new ConcurrentHashMap<>();
  private final Object synchronizedThreadLock = new Object();

  public CommandManager(Punishments punishments) {
    this.punishments = punishments;
    storeCommands();
  }

  private void storeCommands() {
    commands.add(new TempBanCMD(punishments));
    commands.add(new BanCMD(punishments));
    commands.add(new UnbanCMD(punishments));
    commands.add(new KickCMD(punishments));
    commands.add(new MuteCMD(punishments));
  }

  public void perform(CommandSender sender, String[] args, CustomCommand customCommand, Command command) {
    if (sender instanceof Player player) {
      final UUID uuid = player.getUniqueId();
      if (asyncTasks.containsKey(uuid)) {
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_ONE_COMMAND_AT_A_TIME.getComponent(null)));
        return;
      }
      if (customCommand.performAsync()) {
        if (customCommand.performAsyncSynchronized()) {
          synchronized (synchronizedThreadLock) {
            performPlayerCommandAsync(player, uuid, args, customCommand, command);
          }
        } else {
          performPlayerCommandAsync(player, uuid, args, customCommand, command);
        }
      } else {
        customCommand.perform(player, args, command);
      }
    } else if (sender instanceof ConsoleCommandSender console) {
      if (customCommand.performAsync()) {
        if (customCommand.performAsyncSynchronized()) {
          synchronized (synchronizedThreadLock) {
            performConsoleCommandAsync(sender, args, customCommand, command);
          }
        } else {
          performConsoleCommandAsync(sender, args, customCommand, command);
        }
      } else {
        customCommand.performConsole(console, args, command);
      }
    }
  }

  private void performPlayerCommandAsync(Player player, UUID uuid, String[] args, CustomCommand customCommand, Command command) {
    asyncTasks.put(uuid, Bukkit.getAsyncScheduler().runNow(punishments, scheduledTask -> {
      try {
        customCommand.perform(player, args, command);
      } finally {
        asyncTasks.remove(uuid);
      }
    }));
  }

  private void performConsoleCommandAsync(CommandSender sender, String[] args, CustomCommand customCommand, Command command) {
    Bukkit.getAsyncScheduler().runNow(punishments, scheduledTask -> customCommand.performConsole(sender, args, command));
  }
}
