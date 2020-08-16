/**
 * Class BotManager
 * @author David-H-Dev
 *
 * last changes:
 * @date 16.08.2020
 */

package at.AlpenSystems.AlpenRadio.utils;

import at.AlpenSystems.AlpenRadio.listeners.OnReady;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BotManager {

    public static void startBot() {
        try {
            JDABuilder.createDefault(
                    Config.get("TOKEN")

            ).addEventListeners(new OnReady()

            ).setActivity(Activity.playing(Config.get("STATUS"))

            ).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}