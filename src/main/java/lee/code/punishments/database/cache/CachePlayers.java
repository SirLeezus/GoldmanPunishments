package lee.code.punishments.database.cache;

import lee.code.punishments.database.DatabaseManager;
import lee.code.punishments.database.cache.data.PunishmentListData;
import lee.code.punishments.database.handlers.DatabaseHandler;
import lee.code.punishments.database.tables.PlayerTable;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CachePlayers extends DatabaseHandler {
  @Getter private final PunishmentListData punishmentListData;
  private final ConcurrentHashMap<UUID, PlayerTable> playersCache = new ConcurrentHashMap<>();

  public CachePlayers(DatabaseManager databaseManager) {
    super(databaseManager);
    this.punishmentListData = new PunishmentListData();
  }

  public void setPlayerTable(PlayerTable playerTable) {
    playersCache.put(playerTable.getUniqueId(), playerTable);
    punishmentListData.cachePunishments(playerTable);
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

  public void mutePlayer(UUID uuid, String reason, String muter) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setMuted(true);
    playerTable.setMutedReason(reason);
    playerTable.setWhoMutedPlayer(muter);
    playerTable.setTimePunished(System.currentTimeMillis());
    updatePlayerDatabase(playerTable);
    punishmentListData.addPunishmentList(playerTable);
  }

  public void tempMutePlayer(UUID uuid, String reason, long time, String muter) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setTempMuted(true);
    playerTable.setTempMutedReason(reason);
    playerTable.setTempMutedTime(System.currentTimeMillis() + time);
    playerTable.setTimePunished(System.currentTimeMillis());
    playerTable.setWhoMutedPlayer(muter);
    updatePlayerDatabase(playerTable);
    punishmentListData.addPunishmentList(playerTable);
  }

  public void unmutePlayer(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setMuted(false);
    playerTable.setTempMuted(false);
    playerTable.setMutedReason(null);
    playerTable.setTempMutedReason(null);
    playerTable.setWhoMutedPlayer(null);
    playerTable.setTimePunished(0);
    updatePlayerDatabase(playerTable);
    if (!isBanned(uuid)) punishmentListData.removePunishmentList(uuid);
  }

  public boolean isTempMuted(UUID uuid) {
    return getPlayerTable(uuid).isTempMuted();
  }

  public boolean isTempMuteOver(UUID uuid) {
    return getPlayerTable(uuid).getTempMutedTime() < System.currentTimeMillis();
  }

  public long getTempMuteTime(UUID uuid) {
    return Math.max(getPlayerTable(uuid).getTempMutedTime() - System.currentTimeMillis(), 0);
  }

  public String getMuteReason(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    if (playerTable.getMutedReason() != null) return playerTable.getMutedReason();
    else return playerTable.getTempMutedReason();
  }

  public boolean isMuted(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    return playerTable.isMuted() || playerTable.isTempMuted();
  }

  public String getWhoMuted(UUID uuid) {
    return getPlayerTable(uuid).getWhoMutedPlayer();
  }

  public void banPlayer(UUID uuid, String reason, String banner) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setBanned(true);
    playerTable.setBanReason(reason);
    playerTable.setWhoBannedPlayer(banner);
    playerTable.setTimePunished(System.currentTimeMillis());
    updatePlayerDatabase(playerTable);
    punishmentListData.addPunishmentList(playerTable);
  }

  public void tempBanPlayer(UUID uuid, String reason, long time, String banner) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setTempBanned(true);
    playerTable.setTempBanReason(reason);
    playerTable.setTempBanTime(System.currentTimeMillis() + time);
    playerTable.setWhoBannedPlayer(banner);
    playerTable.setTimePunished(System.currentTimeMillis());
    updatePlayerDatabase(playerTable);
    punishmentListData.addPunishmentList(playerTable);
  }

  public void unbanPlayer(UUID uuid) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setBanned(false);
    playerTable.setTempBanned(false);
    playerTable.setBanReason(null);
    playerTable.setTempBanReason(null);
    playerTable.setWhoBannedPlayer(null);
    playerTable.setTimePunished(0);
    updatePlayerDatabase(playerTable);
    if (!isMuted(uuid)) punishmentListData.removePunishmentList(uuid);
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

  public long getTimePunished(UUID uuid) {
    return getPlayerTable(uuid).getTimePunished();
  }

  public String getWhoBanned(UUID uuid) {
    return getPlayerTable(uuid).getWhoBannedPlayer();
  }

  public boolean isCuffed(UUID uuid) {
    return getPlayerTable(uuid).isCuffed();
  }

  public void setCuffed(UUID uuid, boolean result) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setCuffed(result);
    updatePlayerDatabase(playerTable);
  }
}
