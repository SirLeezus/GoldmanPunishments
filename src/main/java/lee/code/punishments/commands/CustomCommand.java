package lee.code.punishments.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CustomCommand implements CommandExecutor {
  public abstract String getName();
  public abstract boolean performAsync();
  public abstract boolean performAsyncSynchronized();
  public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
  public abstract void perform(Player player, String[] args, Command command);
  public abstract void performConsole(CommandSender console, String[] args, Command command);
  public abstract void performSender(CommandSender sender, String[] args, Command command);
  public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
