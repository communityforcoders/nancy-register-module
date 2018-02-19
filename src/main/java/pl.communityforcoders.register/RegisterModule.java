package pl.communityforcoders.register;

import java.util.List;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.command.annotation.CommandManifest;
import pl.communityforcoders.nancy.command.context.CommandContext;
import pl.communityforcoders.nancy.module.annotation.Inject;
import pl.communityforcoders.nancy.module.annotation.Listener;
import pl.communityforcoders.nancy.module.annotation.ModuleManifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;
import pl.communityforcoders.nancy.util.EmbedUtils;

@ModuleManifest(name = "RegisterModule", author = "kacperduras", version = "1.0.1.0")
public class RegisterModule {

  private Guild guild;

  private RegisterThread thread;

  @OnEnable
  public void onEnable(Nancy nancy) {
    guild = nancy.getJDA().getGuildById("396018831434186762");

    thread = new RegisterThread(nancy, guild);
    thread.start();
  }

  @OnDisable
  public void onDisable() {
    thread.interrupt();
  }

  @Listener
  public void onJoin(GuildMemberJoinEvent event) {
    event.getUser().openPrivateChannel().queue(channel -> {
      channel.sendMessage(":anger: Witaj, na jedynej tego typu społeczności w Polsce! :anger:\n")
          .append("*Przedstawiamy* ***CommunityForCoders***\n")
          .append("Przeczytaj te krótkie informacje, **pomogą Ci one odnaleźć** się na serwerze.\n")
          .append(":thought_balloon:           Masz jakieś pytania lub problem? Śmiało zgłoś się do kogoś z **ekipy**\n")
          .append(":thought_balloon:           Masz problem dotyczący programowania? Nasza społeczność to idealne miejsce żeby poprosić o pomoc. Zapytaj tylko na odpowiednim kanale, odpowiedź przyjdzie sama.\n")
          .append(":thought_balloon:           Propozycje? To nie problem. Jesteśmy jak najbardziej **otwarci**, również drogą prywatną.\n")
          .append(":thought_balloon:           Jedna z ważniejszych rzeczy - regulamin, znajduję się na kanale **#rules**.\n")
          .append(":thought_balloon:           Wszystkie ważne ogłoszenia znajdziesz na **#announcements**.\n")
          .append("\n")
          .append("Nasza strona, bot - wszystko jest *open source*. I to **TY** możesz przyczynić się do jego ewolucji, a nawet nie tylko.\n").queue();
      channel.sendMessage("Skoro się tutaj znalazłeś, została Ci tylko część weryfikacyjna. Zaakceptuj regulamin, pisząc komendę `!akceptuj`.").queue();
    });
  }

  @CommandManifest(name = "!akceptuj", type = ChannelType.PRIVATE)
  public void registerCommand(User user, PrivateChannel channel, CommandContext context) {
    Member member = guild.getMember(user);

    List<Role> roles = guild.getRolesByName("user", true);
    if (roles.size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd wewnętrzny", "Skontaktuj się z administracją serwera.", true))).queue();
      return;
    }

    List<Role> memberRoles = member.getRoles();
    if (memberRoles.stream().map(Role::getName).anyMatch(role -> role.equals("user"))) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd", "Twoje konto jest już zarejestrowane.", true))).queue();
      return;
    }

    guild.getController().addRolesToMember(member, roles.get(0)).queue();
    channel.sendMessage(EmbedUtils.agree(new Field("Gratulacje :)", "Twoje konto zostało zarejestrowane pomyślnie! Życzymy miłego korzystania ze serwera.", true))).queue();
  }

}
