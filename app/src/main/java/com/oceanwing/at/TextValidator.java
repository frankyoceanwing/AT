package com.oceanwing.at;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by franky on 16/7/28.
 */
public abstract class TextValidator implements TextWatcher {

    private final TextView mTextView;

    public TextValidator(TextView textView) {
        this.mTextView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    public void afterTextChanged(Editable editable) {
        String text = mTextView.getText().toString();
        validate(mTextView, text);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        /* Don't care */
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        /* Don't care */
    }

}
