package com.android.budgetpanda.backend.evaluation;

import com.android.budgetpanda.backend.months.MonthsTracking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class StatusEvaluationRepository {

    public enum Status {
        INFERIOR, SUPERIOR, MODERATE
    }

    public interface StatusEvaluationCallback {
        void onStatusEvaluated(Status status, double amount);

        void onStatusEvaluatedFailed(String errmsg);
    }

    private static final String INCOME_NODE = "Income";
    private static final String EXPENSE_NODE = "Expense";
    private static final String TOTAL_AMOUNT = "totalAmount";
    private final DatabaseReference mDatabase;
    private final MonthsTracking monthsTracking;

    public StatusEvaluationRepository(MonthsTracking monthsTracking) {
        this.monthsTracking = monthsTracking;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("items").child(userId);
    }

    public void evaluateStatus(final StatusEvaluationCallback callback) {
        String monthId = monthsTracking.getSelectedMonth() + "_" + monthsTracking.getSelectedYear();
        mDatabase.child(monthId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(INCOME_NODE) && dataSnapshot.hasChild(EXPENSE_NODE)) {
                    try {
                        long income = dataSnapshot.child(INCOME_NODE).child(TOTAL_AMOUNT).getValue(Long.class);
                        long expense = dataSnapshot.child(EXPENSE_NODE).child(TOTAL_AMOUNT).getValue(Long.class);

                        Status status = getStatus(income, expense);
                        callback.onStatusEvaluated(status, income-expense);
                    } catch (Exception ex) {
                        callback.onStatusEvaluatedFailed(ex.getMessage());
                    }
                } else {
                    callback.onStatusEvaluatedFailed("You must enter data for income and expense to evaluate status");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onStatusEvaluatedFailed(databaseError.getMessage());
            }
        });
    }

    private Status getStatus(long income, long expense) {
        long diff = income - expense;
        long percent = (Math.abs(diff) * 100) / income;
        if (income > expense) {
            return Status.SUPERIOR;
        } else if (percent > 10) {
            return  Status.INFERIOR;
        } else {
            return Status.MODERATE;
        }
    }

}
