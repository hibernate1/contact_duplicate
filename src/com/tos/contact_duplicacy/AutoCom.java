package com.tos.contact_duplicacy;
import android.content.Context;  
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class AutoCom extends AutoCompleteTextView {

    public AutoCom(Context context) {
        super(context);
    }

    public AutoCom(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public AutoCom(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
        }
    }

}