package is416.is416.Schedule;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import is416.is416.Database.Appointment;
import is416.is416.Database.ApptDatabase;

/**
 * Created by Cheryl on 24/3/2018.
 */

public class Mic implements AIListener {
    private AIService aiService;
    private static String CLIENT_ACCESS_TOKEN = "4ae9328c789d41af97d6f14d558dccab";
    private static String TAG = "Permission";
    private ApptDatabase myDb;
    private Context context;
    private TextView questionView;
    private TextView answerView;
    private View speechBubbleView;
    private View questionBubbleView;

    public Mic(Context context, TextView questionView, TextView answerView, View speechBubbleView, View questionBubbleView){
        this.questionView = questionView;
        this.answerView = answerView;
        this.context = context;
        this.speechBubbleView = speechBubbleView;
        this.questionBubbleView = questionBubbleView;
    }

    public void micStart(){
        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(context, config);
        aiService.setListener(this);
        myDb = ApptDatabase.getInstance(context);
    }

    public void micStartListening(){
        aiService.startListening();
    }

    public String GetText(String query) throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/query?v=20150910");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "Bearer "+ CLIENT_ACCESS_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            JSONArray queryArray = new JSONArray();
            queryArray.put(query);
            jsonParam.put("query", queryArray);
            jsonParam.put("lang", "en");
            jsonParam.put("sessionId", "1234567890");

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonParam.toString());
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

            JSONObject object1 = new JSONObject(text);
            JSONObject result = object1.getJSONObject("result");
            JSONObject fulfillment = null;
            String speech = null;
            fulfillment = result.getJSONObject("fulfillment");
            speech = fulfillment.optString("speech");

            JSONObject params = result.getJSONObject("parameters");
            String time = params.has("time")? params.getString("time"):null;
            String name = params.has("name")? params.getString("name"):null;
            String date = params.has("date")? params.getString("date"):null;

            if (time!= null && !time.isEmpty() && name != null && !name.isEmpty() && date != null && !date.isEmpty()){
                myDb.addAppointment(new Appointment(date, time, name));
            }
            return speech;

        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }
        return null;
    }

    @Override
    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }
        String userQuery = result.getResolvedQuery();
        // Show results in TextView.
        questionBubbleView.setVisibility(TextView.VISIBLE);
        questionView.setText(userQuery);
        questionBubbleView.postDelayed(new Runnable() {
            public void run() {
                questionBubbleView.setVisibility(TextView.INVISIBLE);
            }
        }, 3500);
//        questionView.setText("Query:" + userQuery + "\n"
//                + "Parameter: " + parameterString);
        RetrieveFeedTask task=new RetrieveFeedTask();
        task.execute(userQuery);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            String s = null;
            try {

                s = GetText(voids[0]);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            speechBubbleView.setVisibility(TextView.VISIBLE);
            answerView.setText(s);
            speechBubbleView.postDelayed(new Runnable() {
                public void run() {
                    speechBubbleView.setVisibility(TextView.INVISIBLE);
                }
            }, 4000);

        }
    }


    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
