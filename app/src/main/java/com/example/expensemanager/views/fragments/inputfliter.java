package com.example.expensemanager.views.fragments;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

public class inputfliter implements InputFilter {

    private static final String RUPEE_SIGN = "₹";

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // If deleting, return the input as is
        if (end == 0) {
            return source;
        }

        // If adding text, ensure the rupee sign is at the start
        if (!dest.toString().startsWith(RUPEE_SIGN)) {
            return RUPEE_SIGN + source;
        }

        return null;
    }

    public static void apply(EditText editText) {
        editText.setFilters(new InputFilter[]{new inputfliter()});
        editText.addTextChangedListener(new TextWatcher() {
            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;

                isEditing = true;

                if (!s.toString().startsWith(RUPEE_SIGN)) {
                    s.insert(0, RUPEE_SIGN);
                }

                isEditing = false;
            }
        });
    }
}
