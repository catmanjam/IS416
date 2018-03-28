package is416.is416;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


/**
 * Created by aispa on 3/27/2018.
 */

public class RewardsWindowDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        RewardsDatabase myDb = RewardsDatabase.getInstance(this.getContext());
        SQLiteDatabase db = myDb.getWritableDatabase();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.rewardinfo_dialogbox, null);

        final TextView dialog_id = dialogView.findViewById(R.id.dialog_rewardInfo);
        dialog_id.setText("Cotton Candy");
        dialog_id.setText("+ 10 HP");

        YoYo.with(Techniques.Tada)
                .duration(1000)
                .repeat(1)
                .playOn(dialogView.findViewById(R.id.reward_image));


//        Log.d("ID of view",appt.getId().toString() );
//
//        dialog_date.setText(appt.getDate());
//        dialog_time.setText(appt.getTime());
//        dialog_title.setText(appt.getDetails());
//        final Integer appointmentId =appt.getId();

        builder.setView(dialogView)
                // Add action buttons
//                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })

                .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RewardsWindowDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }
}
