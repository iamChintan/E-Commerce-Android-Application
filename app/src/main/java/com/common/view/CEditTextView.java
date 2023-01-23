package com.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.finalAssignment.utils.Utils;


/**
 * @author VaViAn Labs.
 */
public class CEditTextView extends AppCompatEditText {

    public CEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CEditTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            try {
                setTypeface(Utils.getNormal(getContext()));
            } catch (Exception e) {

            }
        }
    }

};