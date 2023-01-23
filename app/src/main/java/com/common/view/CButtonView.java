package com.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.finalAssignment.utils.Utils;


/**
 * @author VaViAn Labs.
 */
public class CButtonView extends AppCompatButton {

    public CButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CButtonView(Context context) {
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