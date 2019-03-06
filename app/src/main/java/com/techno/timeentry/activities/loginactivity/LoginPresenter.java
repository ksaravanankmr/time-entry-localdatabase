package com.techno.timeentry.activities.loginactivity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = "LoginActivity";

    private LoginContract.View view;

    Handler handler;
    private String userName = "dummyuser";
    private String passWord = "dummypass";

    LoginPresenter(LoginContract.View view) {
        this.view = view;
        handler = new Handler();
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
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        if (authenticateUser(userName, passWord)) {
                            view.onLoginSuccess();
                        } else {
                            view.onLoginFailed();
                            view.hideProgressDialog(progressDialog);
                        }


                    }
                }, 1000);
    }

    @Override
    public void cancelLogin() {
        handler.removeCallbacksAndMessages(null);
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
        if (!this.userName.equals(userName)) {
            view.showUserNameNotRegistered();
        } else if (!this.passWord.equals(passWord)) {
            view.showPassWordIncorrect();
        } else {
            return true;
        }

        return false;
    }
}
