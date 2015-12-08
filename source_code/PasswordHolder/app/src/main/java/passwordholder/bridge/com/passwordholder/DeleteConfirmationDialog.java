package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Bus;


public class DeleteConfirmationDialog extends DialogFragment {

    public enum DialogType{
        DIALOG_DELETE_CONFIRMATION,
        DIALOG_APP_EXIT
    }
    private OnDialogInteractionListener mListener;
   static  private OnDialogInteractionListener mfragmentListener;
    static  DeleteConfirmationDialog mDialogInstance;
    static String dialogMessage,dialogContent;
    TextView title,content;
    Button positive,negative;


    public static DeleteConfirmationDialog newInstance(String param1, String param2) {
        DeleteConfirmationDialog fragment = new DeleteConfirmationDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    void showDialog(DeleteConfirmationDialog mDeleteConfirmationDialog) {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        this.show(ft, "dialog");
    }


    private DeleteConfirmationDialog() {
        // Required empty public constructor
    }
    public static DeleteConfirmationDialog getDialogInstance(DialogType mType,String message,String content,
                                                             DetailsFragment mDetailsFragment){
        if(mDialogInstance==null){
            mDialogInstance=new DeleteConfirmationDialog();
        }
        dialogMessage=message;
        dialogContent=content;
        mfragmentListener=mDetailsFragment;
        return mDialogInstance;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_delete_confirmation_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        initUi(v);
        return v;
    }

    private void initUi(View v) {

        title=(TextView) v.findViewById(R.id.title);
        content=(TextView) v.findViewById(R.id.message);
        positive=(Button) v.findViewById(R.id.btn_p);
        negative=(Button) v.findViewById(R.id.btn_n);

        title.setText(dialogMessage);
        content.setText(dialogContent);
        setListeners();
    }

    private void setListeners() {
        positive.setOnClickListener(view -> {
            if(mListener!=null){
                mfragmentListener.onDialogDone();
                getDialog().dismiss();
            }
        });
        negative.setOnClickListener(view -> {
            if(mListener!=null){
                mListener.onDialogCancel();
                getDialog().dismiss();

            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDialogInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogInteractionListener {
        void onDialogCancel();
        void onDialogDone();

    }

}
