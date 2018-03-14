package com.bridge.passwordholder.uicomponents;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class PMButton extends android.support.v7.widget.AppCompatButton {

    public PMButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PMButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PMButton(Context context) {
        super(context);
        init();
    }

    private void init() {		 
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                                               "fonts/HelveticaNeueLTPro-Lt.otf");
        setTypeface(tf);
        
        
    }



}

