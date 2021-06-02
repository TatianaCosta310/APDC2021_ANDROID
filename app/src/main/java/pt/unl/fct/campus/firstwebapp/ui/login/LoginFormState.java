package pt.unl.fct.campus.firstwebapp.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError; // corresponde ao email

    @Nullable
    private Integer name;

    @Nullable
    private Integer passwordError;

    private boolean isDataValid;

    @Nullable
    private Integer confirmPassError;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirmPassError,@Nullable Integer name) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmPassError = confirmPassError;
        this.name = name;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.confirmPassError = null;
        this.name = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getConfirmPassError(){return confirmPassError;}

    @Nullable
    Integer getname(){return name;}

    boolean isDataValid() {
        return isDataValid;
    }
}