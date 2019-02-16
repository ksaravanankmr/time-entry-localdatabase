package com.techno.timeentry.activities.loginactivity;

import android.app.ProgressDialog;

public interface LoginContract {

    interface Presenter {
        void login(String userName, String passWord);

        void cancelLogin();
    }

    interface View {
        void onLoginSuccess();

        void onLoginFailed();

        void disableLoginButton();

        void enableLoginButton();

        ProgressDialog showProgressDialog();

        void hideProgressDialog(ProgressDialog progressDialog);

        void showUserNameError();

        void showPassWordError();

        void showUserNameNotRegistered();

        void showPassWordIncorrect();

        void clearUserNameError();

        void clearPassWordError();
    }
}
