package com.example.chatapp.Login;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener {

    private LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;
    private FirebaseAuth mAuth;

    LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
        mLoginInteractor = new LoginInteractor(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void facebookLogin(AccessToken accessToken) {
        mLoginInteractor.handleFacebookLogin(accessToken);
    }

    @Override
    public void googleLogin(GoogleSignInAccount googleSignInAccount) {
        mLoginInteractor.handleGoogleLogin(googleSignInAccount);
    }

    @Override
    public void checkLogin() {
        if (mAuth.getCurrentUser() == null)
            mLoginView.isLoggedIn(false);
        else
            mLoginView.isLoggedIn(true);
    }

    @Override
    public void onSuccess() {
        mLoginView.onLoginSuccess();
    }

    @Override
    public void onFailure() {
        mLoginView.onLoginFailure();
    }

}
