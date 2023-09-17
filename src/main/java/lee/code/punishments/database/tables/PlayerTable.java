package lee.code.punishments.database.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "players")
public class PlayerTable {
  @DatabaseField(id = true, canBeNull = false)
  private UUID uniqueId;

  @DatabaseField(columnName = "banned")
  private boolean banned;

  @DatabaseField(columnName = "ban_reason")
  private String banReason;

  @DatabaseField(columnName = "temp_banned")
  private boolean tempBanned;

  @DatabaseField(columnName = "temp_ban_time")
  private long tempBanTime;

  @DatabaseField(columnName = "temp_ban_reason")
  private String tempBanReason;

  @DatabaseField(columnName = "muted")
  private boolean muted;

  @DatabaseField(columnName = "muted_reason")
  private String mutedReason;

  @DatabaseField(columnName = "temp_muted")
  private boolean tempMuted;

  @DatabaseField(columnName = "temp_muted_time")
  private long tempMutedTime;

  @DatabaseField(columnName = "temp_muted_reason")
  private String tempMutedReason;

  public PlayerTable(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }
}
