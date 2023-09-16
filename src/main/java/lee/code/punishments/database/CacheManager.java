package lee.code.punishments.database;

import lee.code.punishments.database.cache.CachePlayers;
import lombok.Getter;

public class CacheManager {
  @Getter private final CachePlayers cachePlayers;

  public CacheManager(DatabaseManager databaseManager) {
    this.cachePlayers = new CachePlayers(databaseManager);
  }
}
