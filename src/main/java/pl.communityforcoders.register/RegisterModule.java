package pl.communityforcoders.register;

import java.util.List;
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

@ModuleManifest(name = "RegisterModule", author = "kacperduras", version = "1.0.0.0-SNAPSHOT")
public class RegisterModule {

  @Inject
  private Nancy nancy;

  private Guild guild;

  @OnEnable
  public void onEnable() {
    guild = nancy.getJDA().getGuildById("396018831434186762");

    nancy.getCommandManager().register(this);
  }

  @OnDisable
  public void onDisable() {
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
          .append(":thought_balloon:           Jedna z wazniejszych rzeczy - regulamin, znajduję się na kanale **#rules**.\n")
          .append(":thought_balloon:           Wszystkie wazne ogłoszenia znajdziesz na **#advertisements**.\n")
          .append("\n")
          .append("Nasza strona, bot - wszystko jest *open source*. I to **TY** możesz przyczynić się do jego ewolucji, a nawet nie tylko.\n").queue();
      channel.sendMessage("Skoro się tutaj znalazłeś, została Ci tylko część weryfikacyjna. Tutaj możesz podać swoją płeć, poprzez wpisanie komendy `!reg <male/female/unspecified>`.").queue();
    });
  }

  @CommandManifest(name = "!reg")
  public void registerCommand(User user, PrivateChannel channel, CommandContext context) {
    if (context.getParams().size() != 1) {
      channel.sendMessage(EmbedUtils.error(new Field("Poprawne użycie", "!reg <male/female/unspecified>", true))).queue();
      return;
    }

    Member member = guild.getMember(user);

    if (context.hasParam("male")) {
      register(channel, member, "male");
    } else if (context.hasParam("female")) {
      register(channel, member, "female");
    } else if (context.hasParam("unspecified")) {
      register(channel, member, "unspecified");
    } else {
      channel.sendMessage(EmbedUtils.error(new Field("Poprawne użycie", "!reg <male/female/unspecified>", true))).queue();
    }
  }

  private void register(PrivateChannel channel, Member member, String role) {
    List<Role> user = guild.getRolesByName("user", true);
    if (user.size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd wewnętrzny", "Skontaktuj się z administracją serwera.", true))).queue();
      return;
    }

    if (member.getRoles().contains(user.get(0))) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd", "Twoje konto jest już zarejestrowane.", true))).queue();
      return;
    }

    List<Role> sex = guild.getRolesByName(role, true);
    if (sex.size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd wewnętrzny", "Skontaktuj się z administracją serwera.", true))).queue();
      return;
    }

    guild.getController().addRolesToMember(member, user.get(0), sex.get(0)).queue();

    channel.sendMessage(EmbedUtils.agree(new Field("Gratulacje :)", "Twoje konto zostało zarejestrowane pomyślnie! Życzymy miłego korzystania ze serwera.", true))).queue();
  }

}
