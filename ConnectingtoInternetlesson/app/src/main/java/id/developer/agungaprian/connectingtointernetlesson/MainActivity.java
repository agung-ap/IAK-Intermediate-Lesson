package id.developer.agungaprian.connectingtointernetlesson;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private ListView listView;

    // URL to get contacts JSON
    private static String url = "https://api.androidhive.info/contacts/";

    ArrayList<HashMap<String , String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.list_item);

        //panggil class asynctask disini
        new GetContact().execute();
    }

    private class GetContact extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... params) {

            NetworkHandler networkHandler = new NetworkHandler();
            // buat request ke url dan get response
            String jsonString = networkHandler.makeServiceCall(url);

            if (jsonString != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    //get json array node
                    JSONArray contacts = jsonObject.getJSONArray("contacts");

                    //looping throug all contact
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject object = contacts.getJSONObject(i);

                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String address = object.getString("address");
                        String gender = object.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = object.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);
                        // adding contact to contact list
                        contactList.add(contact);
                    }

                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                    e.printStackTrace();
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"name", "email",
                    "mobile"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            listView.setAdapter(adapter);
        }

    }


}
