package com.example.chatapp.Login;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginContract {

    interface View {
        void onLoginSuccess();

        void onLoginFailure();

        void isLoggedIn(boolean isLoggedIn);
    }

    interface Presenter {
        void facebookLogin(AccessToken accessToken);

        void googleLogin(GoogleSignInAccount googleSignInAccount);

        void checkLogin();
    }

    interface Intractor {
        void handleFacebookLogin(AccessToken accessToken);

        void handleGoogleLogin(GoogleSignInAccount googleSignInAccount);

        void addUserToFirebase();
    }

    interface onLoginListener {
        void onSuccess();

        void onFailure();
    }
}
