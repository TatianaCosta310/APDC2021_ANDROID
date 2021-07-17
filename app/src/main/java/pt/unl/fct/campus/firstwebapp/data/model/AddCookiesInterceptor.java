package pt.unl.fct.campus.firstwebapp.data.model;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import pt.unl.fct.campus.firstwebapp.data.Constantes;

public class AddCookiesInterceptor implements Interceptor,Constantes {
    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        for (String cookie : cookies) {
            builder.addHeader("Cookie", cookie);
            Log.v("OkHttp", "Adding Header: " + cookie);//This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}
