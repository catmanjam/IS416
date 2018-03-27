package is416.is416.Schedule;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import is416.is416.Database.Appointment;
import is416.is416.Database.Database;
import is416.is416.R;

/**
 * Created by Cheryl on 26/3/2018.
 */

public class UpdateApptDialog extends DialogFragment {
    private Appointment appt;


    public void setArguments(Bundle args){
        this.appt = (Appointment) new Appointment(args.getInt("id"), args.getString("date"), args.getString("time"),args.getString("details"));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.appointment_dialogbox, null);

        final TextView dialog_date = dialogView.findViewById(R.id.dialog_date);
        final TextView dialog_time = dialogView.findViewById(R.id.dialog_time);
        final TextView dialog_title = dialogView.findViewById(R.id.dialog_title);

        Log.d("ID of view",appt.getId().toString() );

        dialog_date.setText(appt.getDate());
        dialog_time.setText(appt.getTime());
        dialog_title.setText(appt.getDetails());
        final Integer appointmentId =appt.getId();

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Database myDb = Database.getInstance(null);
                        myDb.updateAppointment(new Appointment(appointmentId,dialog_date.getText().toString(),dialog_time.getText().toString(), dialog_title.getText().toString()));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateApptDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }
}
