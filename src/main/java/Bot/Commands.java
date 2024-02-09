package Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.User;  
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static java.awt.Color.*;

public class Commands extends ListenerAdapter {

    File folder = new File("./data/");
    List<UserLogs> users = new ArrayList<>();
    Set<Long> ids = new HashSet<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");


        if (args[0].equalsIgnoreCase(Main.prefix + "help")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Information");
            info.setDescription("Commands: \n\n**!start** - to begin logging hours \n" +
                    "**!end** - to finish logging hours\n" +
                    "**!hours** - to privately view your hours");
            info.setColor(GREEN);
            event.getChannel().sendMessage(info.build()).queue();
            event.getChannel().sendMessage("<@" + event.getMember().getIdLong() + ">").queue();
            info.clear();
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "start")) {
            if (!this.ids.contains(event.getMember().getIdLong())) {
                users.add(new UserLogs(event.getMember().getIdLong(), event.getMember().getEffectiveName()));
                ids.add(event.getMember().getIdLong());
            }
            if (!getUserLog(event.getMember().getIdLong()).getLoggedIn()) {
                EmbedBuilder info = new EmbedBuilder();
                info.setTitle("Logged in");
                info.setDescription("Started logging your hours!");
                info.setColor(GREEN);
                event.getChannel().sendMessage(info.build()).queue();
                event.getChannel().sendMessage("<@" + event.getMember().getIdLong() + ">").queue();
                getUserLog(event.getMember().getIdLong()).setLoggedIn(true);
                getUserLog(event.getMember().getIdLong()).setTimeIn(java.time.Instant.now().getEpochSecond());
                info.clear();
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setTitle("Error");
                error.setDescription("You have already logged in. Please log out before logging in again.");
                error.setColor(0xf45642);
                event.getChannel().sendMessage(error.build()).queue();
                event.getChannel().sendMessage("<@" + event.getMember().getIdLong() + ">").queue();
                error.clear();
            }
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "end")) {
            if (!this.ids.contains(event.getMember().getIdLong())) {
                users.add(new UserLogs(event.getMember().getIdLong(), event.getMember().getEffectiveName()));
                ids.add(event.getMember().getIdLong());
            }
            if (getUserLog(event.getMember().getIdLong()).getLoggedIn()) {
                EmbedBuilder info = new EmbedBuilder();
                info.setTitle("Logged out");
                info.setDescription("Finished logging your hours! Thanks for all your help!\n" +
                        "Please remember to keep your own personal records of your volunteer hours.\n" +
                        "Type !hours to privately view your accumulated volunteer hours.");
                info.setColor(GREEN);
                event.getChannel().sendMessage(info.build()).queue();
                event.getChannel().sendMessage("<@" + event.getMember().getIdLong() + ">").queue();
                getUserLog(event.getMember().getIdLong()).setLoggedIn(false);
                getUserLog(event.getMember().getIdLong()).setTimeOut(java.time.Instant.now().getEpochSecond());
                getUserLog(event.getMember().getIdLong()).addTimes(event.getMember().getEffectiveName());
                info.clear();
                for (Member member : event.getGuild().getMembers()) {                  
                    User user = member.getUser();
                    if (user.getIdLong() == 376255586293252096L){
                        user.openPrivateChannel().queue(channel ->
                                channel.sendMessage(event.getMember().getEffectiveName() + " " +
                                        getUserLog(event.getMember().getIdLong()).getTotalTime()).queue());
                    }
                }
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setTitle("Error");
                error.setDescription("You are not logged in. Please log in before logging out.");
                error.setColor(0xf45642);
                event.getChannel().sendMessage(error.build()).queue();
                event.getChannel().sendMessage("<@" + event.getMember().getIdLong() + ">").queue();
                error.clear();
            }
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "hours")) {
            if (!this.ids.contains(event.getMember().getIdLong())) {
                users.add(new UserLogs(event.getMember().getIdLong(), event.getMember().getEffectiveName()));
                ids.add(event.getMember().getIdLong());
            }
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("You have volunteered for:");
            info.setDescription(getUserLog(event.getMember().getIdLong()).getTotalTime() + " hours");
            info.setFooter("If you believe there is a problem with your hours, please contact one of the admins.");
            info.setColor(GREEN);
            MessageEmbed privatemsg = info.build();
            event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage(privatemsg).queue());
            info.clear();
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "masterlist")) {
            Role role = event.getGuild().getRoleById(753496699413987472L);
            Role role2 = event.getGuild().getRoleById(753498059601608764L);
            if (event.getMember().getRoles().contains(role) || event.getMember().getRoles().contains(role2)) {
                EmbedBuilder info = new EmbedBuilder();
                info.setTitle("Master List");
                info.setDescription(masterListMaker(folder));
                info.setColor(GREEN);
                MessageEmbed privatemsg = info.build();
                event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage(privatemsg).queue());
                info.clear();
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setTitle("Error");
                error.setDescription("You do not have permission to use this command.");
                error.setColor(0xf45642);
                MessageEmbed privatemsg = error.build();
                event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage(privatemsg).queue());
                error.clear();
            }
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "announcement")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("BOT INFORMATION");
            info.setDescription("Jit Bot is responsible for keeping track of volunteer hours! We expect everybody " +
                    "to use this bot as means to record their personal progress.\n\n When beginning your " +
                    "volunteer shift, please enter **'!start'**. During this time, we trust you are engaging " +
                    "in your volunteering responsibilities. When you end your shift, simply type **'!end'**.\n\n To " +
                    "privately view your volunteer hours, type **'!hours'** to have the bot directly message you " +
                    "with your accumulated hours.\n\n There is also a **'!help'** command" +
                    " where you can view all usable " +
                    "commands in brief.\n\n **Please use the dedicated text channel, 'hour-logging', to type in bot " +
                    "commands!**");
            info.setFooter("If you believe there is an issue with your recorded hours, please do not hesitate to " +
                    "contact one of the admins!");
            info.setColor(PINK);
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }
    }


    public UserLogs getUserLog(long id) {

        UserLogs myUser = null;

        if (!users.isEmpty()) {
            for (UserLogs user : users) {
                if (user.userID == id) {
                    myUser = user;
                }
            }
            return myUser;
        } else {
            return null;
        }
    }


    public String masterListMaker(File folder) {
        String phrase = "Hours: \n\n";
        for (File fileEntry : folder.listFiles()) {
            try {
                double next;
                FileReader fr = new FileReader(fileEntry);
                Scanner scan = new Scanner(fr);
                next = scan.nextDouble();
                scan.close();
                phrase = phrase + (fileEntry.getName().replace(".txt", "") + " --- " + next + "\n");
            } catch (FileNotFoundException e) {
                System.out.println("error");
            }
        }
        return phrase;
    }
}
