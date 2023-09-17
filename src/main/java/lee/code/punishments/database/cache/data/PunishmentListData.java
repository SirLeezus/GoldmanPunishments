package lee.code.punishments.database.cache.data;

import lee.code.punishments.database.tables.PlayerTable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PunishmentListData {
  private final ConcurrentHashMap<UUID, Long> punishmentCache = new ConcurrentHashMap<>();

  public void cachePunishments(PlayerTable playerTable) {
    if (playerTable.isBanned() || playerTable.isTempBanned() || playerTable.isMuted() || playerTable.isTempMuted()) {
      punishmentCache.put(playerTable.getUniqueId(), playerTable.getTimePunished());
    }
  }

  public void addPunishmentList(PlayerTable playerTable) {
    punishmentCache.put(playerTable.getUniqueId(), playerTable.getTimePunished());
  }

  public void removePunishmentList(UUID uuid) {
    punishmentCache.remove(uuid);
  }

  public ConcurrentHashMap<UUID, Long> getAllPunishments() {
    return punishmentCache;
  }
}
