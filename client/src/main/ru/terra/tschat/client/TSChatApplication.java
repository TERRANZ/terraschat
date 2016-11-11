package ru.terra.tschat.client;

import android.app.Application;
import android.util.Log;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.core.Logger;

/**
 * Date: 24.06.14
 * Time: 18:08
 */
@ReportsCrashes(formKey = "",
        formUri = "http://terranz.ath.cx/jbrss/errors/do.error.report/tschat",
        httpMethod = HttpSender.Method.POST,
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.error_caught)
public class TSChatApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
        SharedContext.getInstance().setLogger(new Logger() {
            @Override
            public void error(String tag, String msg) {
                Log.e(tag, msg);
            }

            @Override
            public void error(String tag, String msg, Throwable t) {
                Log.e(tag, msg, t);
            }

            @Override
            public void info(String tag, String msg) {
                Log.i(tag,msg);
            }

            @Override
            public void debug(String tag, String msg) {
                Log.d(tag,msg);
            }
        });
    }
}