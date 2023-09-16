package lee.code.punishments.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LogBackendType;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lee.code.punishments.Punishments;
import lee.code.punishments.database.tables.PlayerTable;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.SQLException;

public class DatabaseManager {
  private final Punishments punishments;
  private Dao<PlayerTable, String> playerDao;
  private ConnectionSource connectionSource;

  public DatabaseManager(Punishments punishments) {
    this.punishments = punishments;
  }

  private String getDatabaseURL() {
    if (!punishments.getDataFolder().exists()) punishments.getDataFolder().mkdir();
    return "jdbc:sqlite:" + new File(punishments.getDataFolder(), "database.db");
  }

  public void initialize(boolean debug) {
    if (!debug) LoggerFactory.setLogBackendFactory(LogBackendType.NULL);
    try {
      final String databaseURL = getDatabaseURL();
      connectionSource = new JdbcConnectionSource(
        databaseURL,
        "test",
        "test",
        DatabaseTypeUtils.createDatabaseType(databaseURL));
      createOrCacheTables();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void closeConnection() {
    try {
      connectionSource.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createOrCacheTables() throws SQLException {
    final CacheManager cacheManager = punishments.getCacheManager();

    //Player data
    TableUtils.createTableIfNotExists(connectionSource, PlayerTable.class);
    playerDao = DaoManager.createDao(connectionSource, PlayerTable.class);

    for (PlayerTable playerTable : playerDao.queryForAll()) {
      cacheManager.getCachePlayers().setPlayerTable(playerTable);
    }
  }

  public synchronized void createPlayerTable(PlayerTable playerTable) {
    Bukkit.getAsyncScheduler().runNow(punishments, scheduledTask -> {
      try {
        playerDao.createIfNotExists(playerTable);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public synchronized void updatePlayerTable(PlayerTable playerTable) {
    Bukkit.getAsyncScheduler().runNow(punishments, scheduledTask -> {
      try {
        playerDao.update(playerTable);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public synchronized void deletePlayerTable(PlayerTable playerTable) {
    Bukkit.getAsyncScheduler().runNow(punishments, scheduledTask -> {
      try {
        playerDao.delete(playerTable);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }
}