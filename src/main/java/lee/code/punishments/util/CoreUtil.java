package lee.code.punishments.util;

import lee.code.punishments.lang.Lang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CoreUtil {
  private final static Pattern numberIntPattern = Pattern.compile("^[1-9]\\d*$");

  public static Component parseColorComponent(String text) {
    final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
    return (Component.empty().decoration(TextDecoration.ITALIC, false)).append(serializer.deserialize(text));
  }

  public static String buildStringFromArgs(String[] words, int startIndex) {
    final StringBuilder sb = new StringBuilder();
    for (int i = startIndex; i < words.length; i++) {
      sb.append(words[i]);
      if (i < words.length - 1) sb.append(" ");
    }
    return sb.toString();
  }

  public static List<String> getOnlinePlayers() {
    return Bukkit.getOnlinePlayers().stream()
      .filter(player -> !player.getGameMode().equals(GameMode.SPECTATOR))
      .map(Player::getName)
      .collect(Collectors.toList());
  }

  public static boolean isPositiveIntNumber(String numbers) {
    final String intMax = String.valueOf(Integer.MAX_VALUE);
    if (numbers.length() > intMax.length() || (numbers.length() == intMax.length() && numbers.compareTo(intMax) > 0)) return false;
    return numberIntPattern.matcher(numbers).matches();
  }

  public static long getMilliseconds(String time) {
    if (!isPositiveIntNumber(time.replaceAll("[[^0-9]+$]", ""))) return 0;
    final int value = Integer.parseInt(time.replaceAll("[[^0-9]+$]", ""));
    return switch (time.replaceAll("[0-9]", "")) {
      case "w" -> (long) value * 604800L * 1000L;
      case "d" -> (long) value * 86400L * 1000L;
      case "h" -> (long) value * 3600L * 1000L;
      case "m" -> (long) value * 60L * 1000L;
      case "s" -> (long) value * 1000L;
      default -> 0L;
    };
  }

  public static String parseTime(long time) {
    final long days = TimeUnit.MILLISECONDS.toDays(time);
    final long hours = TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(days);
    final long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
    final long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);
    if (days != 0L) return "&e" + days + "&6d&e, " + hours + "&6h&e, " + minutes + "&6m&e, " + seconds + "&6s";
    else if (hours != 0L) return "&e" + hours + "&6h&e, " + minutes + "&6m&e, " + seconds + "&6s";
    else return minutes != 0L ? "&e" + minutes + "&6m&e, " + seconds + "&6s" : "&e" + seconds + "&6s";
  }

  public static void kickPlayerIfOnline(UUID uuid, Component message) {
    final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(uuid);
    if (!offlineTarget.isOnline()) return;
    final Player onlineTarget = offlineTarget.getPlayer();
    if (onlineTarget == null) return;
    onlineTarget.kick(message);
  }
}
