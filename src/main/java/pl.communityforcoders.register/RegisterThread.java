package pl.communityforcoders.register;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.lang3.Validate;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.NancyException;
import pl.communityforcoders.nancy.util.EmbedUtils;

public class RegisterThread extends Thread {

  private final Nancy nancy;
  private final Guild guild;

  public RegisterThread(Nancy nancy, Guild guild) {
    super("RegisterModule - RegisterThread");
    Validate.notNull(nancy);

    this.nancy = nancy;
    this.guild = guild;
  }

  @Override
  public void run() {
    try {
      List<Role> user = guild.getRolesByName("user", true);
      if (user.size() == 0) {
        throw new NancyException("User role can not be null!");
      }

      while (nancy.isRunning() && !isInterrupted()) {
        for (Member member : guild.getMembers()) {
          if (member.getUser().isBot()) {
            continue;
          }

          if (member.getOnlineStatus() != OnlineStatus.ONLINE
              && member.getOnlineStatus() != OnlineStatus.DO_NOT_DISTURB) {
            continue;
          }

          if (member.getRoles().contains(user.get(0))) {
            continue;
          }

          OffsetDateTime now = OffsetDateTime.now();
          OffsetDateTime joinDate = member.getJoinDate();
          long minutes = joinDate.until(now, ChronoUnit.MINUTES);

          if (minutes <= 30) {
            return;
          }

          member.getUser().openPrivateChannel().queue(channel ->
              channel.sendMessage(EmbedUtils.warning("Hej!", new Field(
                    "Wygląda na to, że nie zarejestrowałeś się na serwerze Community for Coders!",
                    "Dokonaj tego teraz, akceptując regulamin komendą `!akceptuj`.",
                    true
              ))).queue());
        }
        Thread.sleep(TimeUnit.MINUTES.toMillis(30));
      }
    } catch (InterruptedException ignored) {
    }
  }

}
