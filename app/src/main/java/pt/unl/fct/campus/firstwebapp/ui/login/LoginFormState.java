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

    private boolean isDataValid,isCodeValid;

    @Nullable
    private Integer confirmPassError;

    @Nullable
    private Integer verification_code;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirmPassError,@Nullable Integer name,@Nullable Integer verification_code) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmPassError = confirmPassError;
        this.name = name;
        this.verification_code = verification_code;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.confirmPassError = null;
        this.name = null;
        this.isDataValid = isDataValid;
    }

    LoginFormState(boolean isCodeValid, String a) {

        this.verification_code = null;
        this.isCodeValid = isCodeValid;
    }

    LoginFormState(boolean isDataValid,int b) {
        this.passwordError = null;
        this.confirmPassError = null;
        this.isDataValid = isDataValid;
    }

     LoginFormState(boolean isDataValid, int i, boolean b1) {
         this.usernameError = null;
         this.passwordError = null;
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

    @Nullable
    Integer getVerification_code(){return verification_code;}

    boolean isDataValid() {
        return isDataValid;
    }

    boolean isCodeValid() {
        return isCodeValid;
    }
}