package com.wirsztj.jsonparser.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.wirsztj.jsonparser.Adapter.GetInformationsAdapter;
import com.wirsztj.jsonparser.Model.Person;
import com.wirsztj.jsonparser.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wirszt_j on 22/10/14.
 */
public class GetInformations extends Activity {

    ListView listView;

    GetInformationsAdapter adapter;

    List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_informations);

        listView = (ListView) findViewById(R.id.list);
        adapter = new GetInformationsAdapter();
        listView.setAdapter(adapter);

        persons = new LinkedList<Person>();

        adapter.setList(persons);

        downloadInformations();
    }

    private void downloadInformations() {
        String url = "";

        new JSONDownloader().execute(url);
    }

    private class JSONDownloader extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(GetInformations.this);

            dialog.setTitle("Chargement");
            dialog.setMessage("Le chargement peut prendre un petit temps.\nMerci de patienter");
            dialog.setIndeterminate(true);

            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream is = null;
            JSONObject jObj = null;
            String json = "";

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("Return", json + "");
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject == null) {
                    dialog.dismiss();
                    Toast.makeText(GetInformations.this, "Something went wrong, do you have internet ?", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONArray persons = jsonObject.getJSONArray("persons");

                for (int i = 0; i < persons.length(); ++i) {
                    Person person = new Person();
                    JSONObject jo = persons.getJSONObject(i);

                    person.setName(jo.getString("first_name") + " " + jo.getString("last_name"));
                    person.setBirthDate(jo.getString("birth_date"));
                    person.setProfilePictureUrl(jo.getString("profile_picture"));
                    person.setYear(jo.getString("epitech_year"));

                    GetInformations.this.persons.add(person);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.setList(persons);

            dialog.dismiss();
        }
    }
}
