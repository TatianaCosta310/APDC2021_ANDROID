package pt.unl.fct.campus.firstwebapp.data.model;

import java.io.IOException;

import okhttp3.Interceptor;
import pt.unl.fct.campus.firstwebapp.data.Constantes;

public class ReceivedCookiesInterceptor implements Interceptor,Constantes {
    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException, IOException {
        okhttp3.Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
        }
        return originalResponse;
    }
}