package utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cynoteck.petofy.R;

/**
 * Created by cynoteck on 1/4/2016.
 */
public class DialogBox extends AlertDialog {

    Context context;
    public DialogBox(Context context) {
        super(context);
        this.context = context;
    }

    public void showAlertDialog(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(context.getResources().getString(R.string.ok), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }
}
