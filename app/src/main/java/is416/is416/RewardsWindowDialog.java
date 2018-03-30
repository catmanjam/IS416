package is416.is416;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.lang.reflect.Field;


/**
 * Created by aispa on 3/27/2018.
 */

public class RewardsWindowDialog extends DialogFragment {

    private String polyId;
    private double OBTAIN_RANGE = 50.0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final RewardsDatabase myDb = RewardsDatabase.getInstance(this.getContext());
        SQLiteDatabase db = myDb.getWritableDatabase();
        Bundle args = getArguments();
        final String polyId = args.getString("PolyId");
        String latlng = args.getString("LatLng");



        Cursor c = db.rawQuery("SELECT * FROM "+RewardsDatabase.TABLE_REWARDS,null);
        if (c == null){
            System.out.println("OH NOESSSS");
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.rewardinfo_dialogbox, null);

        TextView dialog_title = dialogView.findViewById(R.id.titleName);
//        dialog_title.setText(myDb.getLocationNameByPolyId(polyId));
        dialog_title.setText(polyId);

        final Rewards reward = myDb.getRewardByPolyId(polyId);

        if (latlng!=null){
            if (Double.parseDouble(latlng) < OBTAIN_RANGE){
                final Button claimButt = dialogView.findViewById(R.id.claimButton);
                claimButt.setVisibility(View.VISIBLE);

                if (myDb.checkClaimedByName(reward.getName(),polyId)){
                    claimButt.setClickable(false);
                    claimButt.setBackgroundColor(Color.DKGRAY);
                    claimButt.setText("CLAIMED!");
                }else {
                    claimButt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDb.claimRewardByName(reward.getName(),polyId);
                            claimButt.setClickable(false);
                            claimButt.setBackgroundColor(Color.DKGRAY);
                            claimButt.setText("CLAIMED!");
                            MapsActivity.healthToAdd += reward.getRewardInt();
                        }
                    });
                }
            }
        }

        TextView dialog_name = dialogView.findViewById(R.id.dialog_id);
        dialog_name.setText(reward.getName());
        final TextView dialog_id = dialogView.findViewById(R.id.dialog_rewardInfo);
        dialog_id.setText("+ "+reward.getRewardAmt());

        ImageView dialog_image = dialogView.findViewById(R.id.reward_image);
        try {
            Class res = R.drawable.class;
            Field field = res.getField(reward.getName());
            int drawableId = field.getInt(null);
            dialog_image.setImageResource(drawableId);
        }
        catch (Exception e) {
            Log.e("MyTag", "Failure to get drawable id.", e);
        }
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
//                .setPositiveButton("Claim", new DialogInterface.OnClickListener() {
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
