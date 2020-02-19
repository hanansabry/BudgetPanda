package com.android.budgetpanda.backend.authentication;

import com.android.budgetpanda.model.User;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationRepository {

    interface RegistrationCallback {
        void onSuccessfulRegistration(FirebaseUser firebaseUser);

        void onFailedRegistration(String errmsg);
    }

    interface LoginCallback {
        void onSuccessLogin(FirebaseUser firebaseUser);

        void onFailedLogin(String errmsg);
    }

    void registerNewUser(User user, RegistrationCallback callback);

    void login(String email, String password, LoginCallback callback);

}
