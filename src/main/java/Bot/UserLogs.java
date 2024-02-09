package Bot;

public class UserLogs {

    public long userID;
    private String name;
    private double timeIn;
    private double timeOut;
    private double totalTime;
    private boolean loggedIn;
    private Save save;

    public UserLogs(long user, String name) {
        save = new Save();
        userID = user;
        this.name = name;
        timeIn = 0;
        timeOut = 0;
        totalTime = save.loadHours(save.toFileName(name));
        loggedIn = false;
    }

    public void setLoggedIn(boolean bool) {
        loggedIn = bool;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setTimeIn(long time) {
        timeIn = time;
    }

    public void setTimeOut(long time) {
        timeOut = time;
    }

    public void addTimes(String name) {
        double elapsedTime;
        elapsedTime = (timeOut - timeIn) / 3600;
        elapsedTime = Math.round(elapsedTime * 100.0) / 100.0;
        totalTime = totalTime + elapsedTime;
        save.saveHours(totalTime, save.toFileName(name));
    }

    public double getTotalTime() {
        return totalTime;
    }
}
