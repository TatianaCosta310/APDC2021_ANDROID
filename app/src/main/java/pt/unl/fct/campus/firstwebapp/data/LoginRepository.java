package pt.unl.fct.campus.firstwebapp.data;
import pt.unl.fct.campus.firstwebapp.data.model.AdditionalAttributes;
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

    public Result<RegisterData> register(String name, String password,String email) {


        Result<RegisterData> result = dataSource.register(name, password,email);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoginData>) result).getData());
        }

        return result;
    }

    public Result<AdditionalAttributes> updateInfo(String userType, String fixNumber, String mobileNumber, String address, String cAddress,String locality) {


        Result<AdditionalAttributes> result = dataSource.updateInfo(userType, fixNumber,mobileNumber,address,cAddress,locality);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoginData>) result).getData());
        }

        return result;
    }

    public Result<LoginData> removeAccount(String token,String password) {


        Result<LoginData> result = dataSource.removeAccount(token,password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoginData>) result).getData());
        }

        return result;
    }
}