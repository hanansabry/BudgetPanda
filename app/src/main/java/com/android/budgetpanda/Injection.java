package com.android.budgetpanda;


import android.content.SharedPreferences;

import com.android.budgetpanda.backend.authentication.AuthenticationRepository;
import com.android.budgetpanda.backend.authentication.AuthenticationRepositoryImpl;
import com.android.budgetpanda.backend.evaluation.StatusEvaluationRepository;
import com.android.budgetpanda.backend.items.ItemsRepository;
import com.android.budgetpanda.backend.items.ItemsRepositoryImpl;
import com.android.budgetpanda.backend.months.MonthsTracking;
import com.android.budgetpanda.backend.todo.TodoListRepository;
import com.android.budgetpanda.backend.todo.TodoListRepositoryImpl;
import com.android.budgetpanda.backend.types.TypesRepository;
import com.android.budgetpanda.backend.types.TypesRepositoryImpl;

public class Injection {


    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }

    public static MonthsTracking provideMonthsTracking(SharedPreferences sharedPreferences) {
        return new MonthsTracking(sharedPreferences);
    }

    public static TypesRepository provideTypesRepository() {
        return new TypesRepositoryImpl();
    }


    public static ItemsRepository provideItemsRepository(SharedPreferences sharedPreferences) {
        return new ItemsRepositoryImpl(provideMonthsTracking(sharedPreferences));
    }

    public static StatusEvaluationRepository provideStatusEvaluationRepository(SharedPreferences sharedPreferences) {
        return new StatusEvaluationRepository(provideMonthsTracking(sharedPreferences));
    }

    public static TodoListRepository provideTodoListRepository() {
        return new TodoListRepositoryImpl();
    }
}
