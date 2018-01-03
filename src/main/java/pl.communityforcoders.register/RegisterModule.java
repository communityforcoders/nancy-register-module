package pl.communityforcoders.register;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.module.annotation.Listener;
import pl.communityforcoders.nancy.module.annotation.Manifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;

@Manifest(name = "RegisterModule", author = "kacperduras", version = "1.0.0.0")
public class RegisterModule {

  private Nancy nancy;

  @OnEnable
  public void onEnable(Nancy nancy) {
    this.nancy = nancy;
  }

  @OnDisable
  public void onDisable() {
    this.nancy = null;
  }

  @Listener
  public void onJoin(GuildMemberJoinEvent event) {

  }

  @Listener
  public void onSend(PrivateMessageReceivedEvent event) {

  }

}
