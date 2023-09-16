package lee.code.punishments.lang;

import lee.code.punishments.util.CoreUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {
  PREFIX("&2&lPunishments &6➔ "),
  USAGE("&6&lUsage: &e{0}"),
  COMMAND_BAN_BROADCAST("&cThe player &6{0} &chas been permanently banned for&7: &e{1}"),
  COMMAND_UNBAN_BROADCAST("&aThe player &6{0} &ahas been unbanned."),
  COMMAND_BAN_KICK_MESSAGE("&cYou have been permanently banned for&7:\n\n&e{0}"),
  ERROR_ONE_COMMAND_AT_A_TIME("&cYou're currently processing another command, please wait for it to finish."),
  ERROR_NO_PLAYER_DATA("&cCould not find any player data for &6{0}&c."),
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
