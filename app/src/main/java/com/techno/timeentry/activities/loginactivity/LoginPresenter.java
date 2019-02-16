package com.techno.timeentry.activities.loginactivity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = "LoginActivity";

    private LoginContract.View view;

    LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(final String userName, final String passWord) {
        Log.d(TAG, "Login");

        if (!validate(userName, passWord)) {
            view.onLoginFailed();
            return;
        }

        view.disableLoginButton();
        final ProgressDialog progressDialog = view.showProgressDialog();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (authenticateUser(userName, passWord)) {
                            view.onLoginSuccess();
                        } else {
                            view.onLoginFailed();
                            view.hideProgressDialog(progressDialog);
                        }


                    }
                }, 2000);
    }

    private boolean validate(String userName, String passWord) {
        boolean valid = true;
        if (userName.isEmpty() || userName.length() < 4) {
            view.showUserNameError();
            valid = false;
        } else {
            view.clearUserNameError();
        }

        if (passWord.isEmpty() || passWord.length() < 4 || passWord.length() > 10) {
            view.showPassWordError();
            valid = false;
        } else {
            view.clearPassWordError();
        }
        return valid;
    }

    private boolean authenticateUser(String userName, String passWord) {
        if (!userName.equals("dummyuser")) {
            view.showUserNameNotRegistered();
        } else if (!passWord.equals("dummypass")) {
            view.showPassWordIncorrect();
        } else {
            return true;
        }

        return false;
    }
}
