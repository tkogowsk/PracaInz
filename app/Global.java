/*
import akka.actor.Cancellable;
import scala.concurrent.duration.Duration;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {

    private Cancellable scheduler;

    @Override
    public void onStart(Application application) {
        int timeDelayFromAppStartToLogFirstLogInMs = 0;
        int timeGapBetweenMemoryLogsInMinutes = 10;
        scheduler = Akka.system().scheduler().schedule(Duration.create(timeDelayFromAppStartToLogFirstLogInMs, TimeUnit.MILLISECONDS),
                Duration.create(timeGapBetweenMemoryLogsInMinutes, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Cron Job");
                    }
                },
                Akka.system().dispatcher());
        super.onStart(application);
    }

    @Override
    public void onStop(Application app) {
        scheduler.cancel();
        super.onStop(app);
    }

}*/
