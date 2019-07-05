package com.example.chatapp.Login;

import androidx.annotation.NonNull;
import com.example.chatapp.Model.User;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginInteractor implements LoginContract.Intractor {

    private FirebaseAuth mAuth;
    private LoginContract.onLoginListener mOnLoginListener;
    private DatabaseReference mDatabaseReference;
    private User mUser;

    LoginInteractor(LoginContract.onLoginListener onLoginListener) {
        this.mOnLoginListener = onLoginListener;
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        mUser = new User();
    }

    @Override
    public void handleFacebookLogin(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserToFirebase();
                            mOnLoginListener.onSuccess();
                        } else {
                            mOnLoginListener.onFailure();
                        }
                    }
                });
    }

    @Override
    public void handleGoogleLogin(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            addUserToFirebase();
                            mOnLoginListener.onSuccess();

                        } else {
                            // If sign in fails, display a message to the user.
                            mOnLoginListener.onFailure();
                        }

                    }
                });
    }

    @Override
    public void addUserToFirebase() {
        mUser.setUserId(mAuth.getCurrentUser().getUid());
        mUser.setName(mAuth.getCurrentUser().getDisplayName());
        mUser.setImageUri(mAuth.getCurrentUser().getPhotoUrl().toString());
        mDatabaseReference.child(mAuth.getCurrentUser().getUid()).setValue(mUser);
    }

}
