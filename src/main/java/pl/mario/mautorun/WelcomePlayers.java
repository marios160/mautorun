package pl.mario.mautorun;

public class WelcomePlayers extends Thread {

    Player player;

    public WelcomePlayers(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            if (Main.conf.isWelcomeCheck()) {
                Main.srv.welcomePlayer(this.player);
            }
        } catch (InterruptedException ex) {
            Loggs.loguj("WelcomePlayers-Run", ex);
        }
    }

}
