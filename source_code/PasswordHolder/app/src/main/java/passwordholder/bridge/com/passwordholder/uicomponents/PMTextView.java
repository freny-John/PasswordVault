package passwordholder.bridge.com.passwordholder.uicomponents;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PMTextView extends TextView {

    public PMTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PMTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PMTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/HelveticaNeueLTPro-Lt.otf");
        setTypeface(tf);
        
        
    }



}

