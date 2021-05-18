package pt.unl.fct.campus.firstwebapp;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginApp extends Application {

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public ExecutorService getExecutorService(){return executorService; }
}
