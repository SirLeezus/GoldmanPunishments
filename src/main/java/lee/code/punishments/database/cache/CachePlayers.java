package lee.code.punishments.database.cache;

import lee.code.punishments.database.DatabaseManager;
import lee.code.punishments.database.handlers.DatabaseHandler;
import lee.code.punishments.database.tables.PlayerTable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CachePlayers extends DatabaseHandler {
  private final ConcurrentHashMap<UUID, PlayerTable> playersCache = new ConcurrentHashMap<>();

  public CachePlayers(DatabaseManager databaseManager) {
    super(databaseManager);
  }

  public void setPlayerTable(PlayerTable playerTable) {
    playersCache.put(playerTable.getUniqueId(), playerTable);
  }

  public boolean hasPlayerData(UUID uuid) {
    return playersCache.containsKey(uuid);
  }

  public PlayerTable getPlayerTable(UUID uuid) {
    return playersCache.get(uuid);
  }

  public void createPlayerData(UUID uuid) {
    final PlayerTable playerTable = new PlayerTable(uuid);
    setPlayerTable(playerTable);
    createPlayerDatabase(playerTable);
  }

  public void banPlayer(UUID uuid, String reason) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setBanned(true);
    playerTable.setBanReason(reason);
    updatePlayerDatabase(playerTable);
  }

  public void tempBanPlayer(UUID uuid, String reason, long time) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setTempBanned(true);
    playerTable.setTempBanReason(reason);
    playerTable.setTempBanTime(System.currentTimeMillis() + time);
    updatePlayerDatabase(playerTable);
  }

  public void unbanPlayer(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setBanned(false);
    playerTable.setTempBanned(false);
    playerTable.setBanReason(null);
    playerTable.setTempBanReason(null);
    updatePlayerDatabase(playerTable);
  }

  public boolean isBanned(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    return playerTable.isBanned() || playerTable.isTempBanned();
  }

  public boolean isTempBanned(UUID uuid) {
    return getPlayerTable(uuid).isTempBanned();
  }

  public long getTempBanTime(UUID uuid) {
    return Math.max(getPlayerTable(uuid).getTempBanTime() - System.currentTimeMillis(), 0);
  }

  public boolean isTempBanOver(UUID uuid) {
    return getPlayerTable(uuid).getTempBanTime() < System.currentTimeMillis();
  }

  public String getBanReason(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    if (playerTable.getBanReason() != null) return playerTable.getBanReason();
    else return playerTable.getTempBanReason();
  }
}
