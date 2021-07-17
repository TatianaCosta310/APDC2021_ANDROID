package pt.unl.fct.campus.firstwebapp.data;
import java.util.Map;

import okhttp3.RequestBody;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
import pt.unl.fct.campus.firstwebapp.data.model.EventData2;
import pt.unl.fct.campus.firstwebapp.data.model.LoginData;
import pt.unl.fct.campus.firstwebapp.data.model.RegisterData;
import pt.unl.fct.campus.firstwebapp.data.model.UserAuthenticated;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance = null;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoginData user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }



    private void setLoggedInUser(LoginData user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    private void setLoggedOutUser() {
        this.user = null;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoginData> login(String username, String password){
        // handle login
        Result<LoginData> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoginData>) result).getData());
        }
        return result;
    }


    public Result<Void> logout() {
        user = null;

        Result<Void> result = dataSource.logout();
        if (result instanceof Result.Success) {
            setLoggedOutUser();
        }
        return result;
    }

    public Result<RegisterData> register(RegisterData data, String verification_code) {


        Result<RegisterData> result = dataSource.register(data,verification_code);

        return result;
    }

    public Result<RegisterData> sendVerificationCode(String email) {

        Result<RegisterData> result = dataSource.sendVerificationCode(email);

        return result;
    }

    public Result<AdditionalAttributes> updateInfo( String cookie,AdditionalAttributes atribs) {


        Result<AdditionalAttributes> result = dataSource.updateInfo(cookie,atribs);

        return result;
    }

    public Result<String> updateProfilePicture(String token, Map<String, RequestBody> map) {

        Result<String> result = dataSource.updateProfilePicture(token,map);

        return result;
    }

    public Result<AdditionalAttributes> getInfos(String token,String userid) {

        Result<AdditionalAttributes> result = dataSource.getInfos(token,userid);

        return result;
    }

    public Result<String> removeAccount(String token,String password) {


        Result<String> result = dataSource.removeAccount(token,password);
      /*  if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoginData>) result).getData());
        }*/

        return result;
    }



}