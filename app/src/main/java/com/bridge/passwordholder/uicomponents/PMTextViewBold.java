package com.bridge.passwordholder.uicomponents;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PMTextViewBold extends android.support.v7.widget.AppCompatTextView {

    public PMTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PMTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PMTextViewBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/HelveticaNeueLTPro-Md.otf");
        setTypeface(tf);
        
        
    }



}

