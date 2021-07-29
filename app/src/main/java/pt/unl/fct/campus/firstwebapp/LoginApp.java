package pt.unl.fct.campus.firstwebapp;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginApp extends Application {

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public ExecutorService getExecutorService(){return executorService; }


    private static Context context;

    public void onCreate() {
        super.onCreate();
        LoginApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return LoginApp.context;
    }
}
