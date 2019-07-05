package com.example.chatapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.ChatRoom.ChatRoomActivity;
import com.example.chatapp.R;
import com.example.chatapp.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "Login";
    private static final String EMAIL = "email";

    private LoginPresenter mLoginPresenter;

    //Facebook Login
    private CallbackManager mCallbackManager;
    Button mFbLoginButton;

    //Google Login
    Button mGoogleLoginButton;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_login);
        mFbLoginButton = findViewById(R.id.buttonFacebookLogin);
        mGoogleLoginButton = findViewById(R.id.buttonGoogleLogin);
        mLoginPresenter = new LoginPresenter(this);

        //Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        facebookLoginButtonClick();
        googleLoginButtonClick();

        mLoginPresenter.checkLogin();

    }

    private void facebookLoginButtonClick() {
        mFbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(EMAIL));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        initFacebookLogin(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);

                    }
                });
            }
        });
    }

    private void googleLoginButtonClick() {
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void initFacebookLogin(AccessToken accessToken) {
        mLoginPresenter.facebookLogin(accessToken);
    }

    private void initGoogleLogin(GoogleSignInAccount googleSignInAccount) {
        mLoginPresenter.googleLogin(googleSignInAccount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Google Sign in
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                initGoogleLogin(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                System.out.println(task.getException().toString());
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLoginSuccess() {
        Utils.showMessage(this, getString(R.string.login_success));
        Utils.setIntent(this, ChatRoomActivity.class);
    }

    @Override
    public void onLoginFailure() {
        Utils.showMessage(this, getString(R.string.login_failed));
    }

    @Override
    public void isLoggedIn(boolean isLoggedIn) {
        if (isLoggedIn) {
            Utils.setIntent(this, ChatRoomActivity.class);
        }
    }
}