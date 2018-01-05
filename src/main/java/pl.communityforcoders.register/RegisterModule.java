package pl.communityforcoders.register;

import java.io.File;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.module.annotation.Inject;
import pl.communityforcoders.nancy.module.annotation.Listener;
import pl.communityforcoders.nancy.module.annotation.ModuleManifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;
import pl.communityforcoders.nancy.util.ConfigUtils;

@ModuleManifest(name = "RegisterModule", author = "kacperduras", version = "1.0.0.0")
public class RegisterModule {

  @Inject
  private Nancy nancy;

  @Inject
  private File directory;

  private RegisterModuleConfiguration config;

  @OnEnable
  public void onEnable() {
    config = ConfigUtils.loadConfig(new File(directory, "config.json"),
        RegisterModuleConfiguration.class);
  }

  @OnDisable
  public void onDisable() {
    config = null;
  }

  @Listener
  public void onJoin(GuildMemberJoinEvent event) {
    event.getUser().openPrivateChannel()
        .queue(channel -> config.getWelcomeMessage().forEach(channel::sendMessage));
  }

  @Listener
  public void onSend(PrivateMessageReceivedEvent event) {

  }

}
