package pt.unl.fct.campus.firstwebapp.ui.login;

import android.app.Application;

import pt.unl.fct.campus.firstwebapp.data.model.API;
import pt.unl.fct.campus.firstwebapp.data.model.WebAPI;

public class Model  {

    private  static Model sInstance = null;
    private API mApi = null;

    public static Model getInstance(Application application){

    if(sInstance == null){
        sInstance = new Model(application);
    }
    return sInstance;
}

    private final Application mApplication;

    private Model(Application application){
    mApplication = application;
    mApi = new WebAPI(mApplication);

    }

public Application getApplication(){
        return mApplication;
}

public void login(String username, String password){
        mApi.login(username, password);
}

public int getResponse(){
        return mApi.getResponse();
}
}
