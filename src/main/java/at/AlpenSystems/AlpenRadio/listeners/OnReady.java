/**
 * Class Listener
 * @author David-H-Dev
 *
 * last changes:
 * @date 16.08.2020
 */

package at.AlpenSystems.AlpenRadio.listeners;

import at.AlpenSystems.AlpenRadio.lavaplayer.PlayerManager;
import at.AlpenSystems.AlpenRadio.utils.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OnReady extends ListenerAdapter {

        @Override
        public void onReady(@Nonnull ReadyEvent event) {
            TextChannel logchannel = Objects.requireNonNull(event.getJDA().getGuildById(Long.parseLong(Config.get("GUILD_ID")))).getTextChannelById(Long.parseLong(Config.get("LOGCHANNEL")));

            String guild_str = Config.get("GUILD_ID");
            String voice_str = Config.get("CHANNEL_ID");

            Guild guild = event.getJDA().getGuildById(Long.parseLong(guild_str));
            VoiceChannel voiceChannel = event.getJDA().getVoiceChannelById(Long.parseLong(voice_str));

            PlayerManager manager = PlayerManager.getInstance();
            assert guild != null;
            AudioManager audioManager = guild.getAudioManager();

            if (audioManager.isConnected()) {
                System.out.println("Already connected!");
                return;
            }
            Timer timer = new Timer ();
            TimerTask hourlyTask = new TimerTask () {
                @Override
                public void run () {
                    System.out.println("Leaving channel...");
                    audioManager.closeAudioConnection();
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    audioManager.openAudioConnection(voiceChannel);
                    System.out.println("Joining channel...");
                    manager.loadAndPlay(guild, Config.get("STREAM_URL"));
                    assert logchannel != null;
                    logchannel.sendMessage(":white_check_mark: Restart successful!").queue();
                }
            };
            timer.schedule (hourlyTask, 0L, TimeUnit.HOURS.toMillis(Integer.parseInt(Config.get("RESTART_HOURS"))));
        }
}