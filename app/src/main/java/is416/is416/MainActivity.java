package is416.is416;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void startWalkActivity(View v) {
        Intent launchWalk = new Intent(this, WalkActivity.class);
        startActivity(launchWalk);
    }
}
