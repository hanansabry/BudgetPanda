package com.android.budgetpanda.backend.users;


import com.android.budgetpanda.model.User;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    void insertNewUser(User user, UserInsertionCallback callback);
}
