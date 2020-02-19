package com.android.budgetpanda;


import com.android.budgetpanda.backend.authentication.AuthenticationRepository;
import com.android.budgetpanda.backend.authentication.AuthenticationRepositoryImpl;

public class Injection {


    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }
}
