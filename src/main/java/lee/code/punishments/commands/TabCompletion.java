package lee.code.punishments.commands;

import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TabCompletion implements TabCompleter {
  private final CustomCommand customCommand;

  public TabCompletion(CustomCommand customCommand) {
    this.customCommand = customCommand;
  }

  @Override
  public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {
    return customCommand.onTabComplete(sender, args);
  }
}
