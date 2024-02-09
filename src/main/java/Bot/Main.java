package Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDA jda;
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault("NzQwMDgzNzA1NTE0Njg4NTUz.Xyj2qQ.QWR4Y67S3twddix-CVByMTLWas0").build();
        jda.getPresence().setActivity(Activity.playing("!help for more info!"));
        jda.addEventListener(new Commands());
    }
}