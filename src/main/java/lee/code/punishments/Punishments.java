package lee.code.punishments;

import com.mojang.brigadier.tree.LiteralCommandNode;
import lee.code.punishments.commands.CommandManager;
import lee.code.punishments.commands.CustomCommand;
import lee.code.punishments.commands.TabCompletion;
import lee.code.punishments.database.CacheManager;
import lee.code.punishments.database.DatabaseManager;
import lee.code.punishments.listeners.ChatListener;
import lee.code.punishments.listeners.CuffListener;
import lee.code.punishments.listeners.JoinListener;
import lee.code.punishments.managers.PunishmentMessageManager;
import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Punishments extends JavaPlugin {
  @Getter private CacheManager cacheManager;
  @Getter private CommandManager commandManager;
  @Getter private PunishmentMessageManager punishmentMessageManager;
  private DatabaseManager databaseManager;

  @Override
  public void onEnable() {
    this.databaseManager = new DatabaseManager(this);
    this.cacheManager = new CacheManager(databaseManager);
    this.commandManager = new CommandManager(this);
    this.punishmentMessageManager = new PunishmentMessageManager(this);
    databaseManager.initialize(false);

    registerListeners();
    registerCommands();
  }

  @Override
  public void onDisable() {
    databaseManager.closeConnection();
  }

  private void registerListeners() {
    getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    getServer().getPluginManager().registerEvents(new CuffListener(this), this);
  }

  private void registerCommands() {
    for (CustomCommand command : commandManager.getCommands()) {
      getCommand(command.getName()).setExecutor(command);
      getCommand(command.getName()).setTabCompleter(new TabCompletion(command));
      loadCommodoreData(getCommand(command.getName()));
    }
  }

  private void loadCommodoreData(Command command) {
    try {
      final LiteralCommandNode<?> targetCommand = CommodoreFileReader.INSTANCE.parse(getResource("commodore/" + command.getName() + ".commodore"));
      CommodoreProvider.getCommodore(this).register(command, targetCommand);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
