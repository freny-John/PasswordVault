package com.bridge.passwordholder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bridge.passwordholder.R;
public class DeleteConfirmationDialog extends DialogFragment {

    public enum DialogType{
        DIALOG_DELETE_CONFIRMATION,
        DIALOG_APP_EXIT,
        DIALOG_SECURITY_QN
    }
    //  private OnDialogInteractionListener mListener;
    static  private OnDialogInteractionListener mFragmentListener;
    private static  DeleteConfirmationDialog mDialogInstance;
    private static String dialogMessage;
    private static String dialogContent;
    private TextView title;
    private TextView content;
    private Button positive;
    private Button negative;


  /*  public static DeleteConfirmationDialog newInstance(String param1, String param2) {
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
    }*/



    public static DeleteConfirmationDialog getDialogInstance(DialogType mType,String message,String content,
                                                             DetailsFragment mDetailsFragment){
        if(mDialogInstance==null){
            mDialogInstance=new DeleteConfirmationDialog();
        }
        dialogMessage=message;
        dialogContent=content;
        mFragmentListener =mDetailsFragment;
        return mDialogInstance;

    }  public static DeleteConfirmationDialog getDialogInstance(DialogType mType,String message,String content,
                                                                SecurityQnFragment mDetailsFragment){
        if(mDialogInstance==null){
            mDialogInstance=new DeleteConfirmationDialog();
        }
        dialogMessage=message;
        dialogContent=content;
        mFragmentListener =mDetailsFragment;
        return mDialogInstance;

    }
    public static DeleteConfirmationDialog getDialogInstance(DialogType mType,String message,String content,
                                                             MainActivity mActivity){
        if(mDialogInstance==null){
            mDialogInstance=new DeleteConfirmationDialog();
        }
        dialogMessage=message;
        dialogContent=content;
        mFragmentListener =mActivity;
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
        getDialog().setCancelable(true);//otherwise it will show keyboard
        initUi(v);
        return v;
    }

    private void initUi(View v) {

        title=(TextView) v.findViewById(R.id.title);
        content=(TextView) v.findViewById(R.id.message);
        positive=(Button) v.findViewById(R.id.btn_p);
        negative=(Button) v.findViewById(R.id.btn_n);
        if(TextUtils.isEmpty(dialogMessage))
        {
            title.setVisibility(View.GONE);
        }
        else{
            title.setVisibility(View.VISIBLE);
            title.setText(dialogMessage);
        }
        content.setText(dialogContent);
        setListeners();
    }

    private void setListeners() {
        positive.setOnClickListener(view -> {
            //  if(mListener!=null){
            mFragmentListener.onDialogDone();
            getDialog().dismiss();
            // }
        });
        negative.setOnClickListener(view -> {
            // if(mListener!=null){
            mFragmentListener.onDialogCancel();
            getDialog().dismiss();

            // }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* try {
            mListener = (OnDialogInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    public interface OnDialogInteractionListener {
        void onDialogDone();
        void onDialogCancel();

    }

}
