package lee.code.punishments.lang;

import lee.code.punishments.util.CoreUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {
  PREFIX("&2&lPunishments &6➔ "),
  USAGE("&6&lUsage: &e{0}"),
  CONSOLE("Console"),
  NEXT_PAGE_TEXT("&2&lNext &a&l>>--------"),
  PREVIOUS_PAGE_TEXT("&a&l--------<< &2&lPrev"),
  PAGE_SPACER_TEXT(" &e| "),
  NEXT_PAGE_HOVER("&6&lNext Page"),
  PREVIOUS_PAGE_HOVER("&6&lPrevious Page"),
  COMMAND_PUNISHMENTS_TITLE("&a----- &e[ &2&lPunishments &e] &a-----"),
  COMMAND_PUNISHMENTS_LINE("&3{0}&7. {1}"),
  COMMAND_PUNISHMENTS_LINE_HOVER_TITLE("&c&lPunishment\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_MUTED("&3Permanently Muted&7: &atrue\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_TEMP_MUTED("&3Temp Muted&7: &atrue\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_BANNED("&3Permanently Banned&7: &atrue\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_TEMP_BANNED("&3Temp Banned&7: &atrue\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_REMAINING_TIME("&3Remaining Time&7: {0}\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_DATE_PUNISHED("&3Date Punished&7: &e{0}\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_WHO_PUNISHED("&3Who Punished&7: &e{0}\n"),
  COMMAND_PUNISHMENTS_LINE_HOVER_REASON("&3Reason&7: {0}"),
  COMMAND_BAN_BROADCAST("&cThe player &6{0} &chas been permanently banned for&7: &e{1}"),
  COMMAND_UNBAN_BROADCAST("&aThe player &6{0} &ahas been unbanned."),
  COMMAND_KICK_BROADCAST("&cThe player &6{0} &chas been kicked for&7: &e{1}"),
  COMMAND_KICK_MESSAGE("&cYou have been kicked for&7:\n\n&e{0}"),
  COMMAND_BAN_KICK_MESSAGE("&cYou have been permanently banned for&7:\n\n&e{0}"),
  COMMAND_MUTE_TARGET_MESSAGE("&cYou have been permanently muted for&7: &e{0}"),
  COMMAND_TEMP_MUTE_TARGET_MESSAGE("&cYou have been muted for {0} &cfor&7: &e{1}"),
  COMMAND_TEMP_MUTE_BROADCAST("&cThe player &6{0} &chas been muted for {1} &cfor&7: &e{2}"),
  COMMAND_MUTE_BROADCAST("&cThe player &6{0} &chas been permanently muted for&7: &e{1}"),
  COMMAND_UNMUTE_BROADCAST("&cThe player &6{0} &chas been unmuted."),
  COMMAND_TEMP_BAN_KICK_MESSAGE("&cYou are banned for {0} &cfor&7:\n\n&e{1}"),
  COMMAND_TEMP_BAN_BROADCAST("&cThe player &6{0} &chas been banned for {1} &cfor&7: &e{2}"),
  ERROR_ONE_COMMAND_AT_A_TIME("&cYou're currently processing another command, please wait for it to finish."),
  ERROR_NO_PLAYER_DATA("&cCould not find any player data for &6{0}&c."),
  ERROR_PLAYER_NOT_ONLINE("&cThe player &6{0} &cis currently not online."),
  ERROR_TEMP_BAN_ZERO("&cYou can't temp ban a player for &e0&6s&c."),
  ERROR_NOT_BANNED("&cThe player &6{0} &cis not banned."),
  ERROR_ALREADY_BANNED("&cThe player &6{0} &cis already banned."),
  ERROR_ALREADY_MUTED("&cThe player &6{0} &cis already muted."),
  ERROR_NOT_MUTED("&cThe player &6{0} &cis not muted."),
  ERROR_NOT_CONSOLE_COMMAND("&cThis command does not work in console."),
  ;
  @Getter private final String string;

  public String getString(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return value;
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return value;
  }

  public Component getComponent(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return CoreUtil.parseColorComponent(value);
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return CoreUtil.parseColorComponent(value);
  }
}
