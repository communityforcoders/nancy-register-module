package pl.communityforcoders.register;

import java.io.File;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.module.annotation.Listener;
import pl.communityforcoders.nancy.module.annotation.Manifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;
import pl.communityforcoders.nancy.util.ConfigUtils;

@Manifest(name = "RegisterModule", author = "kacperduras", version = "1.0.0.0")
public class RegisterModule {

  private Nancy nancy;
  private RegisterModuleConfiguration config;

  @OnEnable
  public void onEnable(Nancy nancy) {
    this.nancy = nancy;
    config = ConfigUtils.loadConfig(new File("config.json"), RegisterModuleConfiguration.class);
  }

  @OnDisable
  public void onDisable() {
    config = null;
    nancy = null;
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
