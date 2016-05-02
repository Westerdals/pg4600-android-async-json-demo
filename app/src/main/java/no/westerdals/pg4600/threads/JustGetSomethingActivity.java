package no.westerdals.pg4600.threads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class JustGetSomethingActivity extends Activity {
    private TextView resultTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_get_something);

        this.resultTextView = (TextView) findViewById(R.id.resultTextView);

        downloadAndDisplayData();
    }

    private void downloadAndDisplayData() {
        new AsyncTask<Void, Void, List<Contact>>() {

            private ProgressDialog progressDialog = new ProgressDialog(JustGetSomethingActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Loading HTML");
                progressDialog.show();
            }

            @Override
            protected List<Contact> doInBackground(final Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("http://contacts.theneva.com/contacts").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    final StringBuilder builder = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }

                    String json = builder.toString();

                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<List<Contact>>(){}.getType();

                    return gson.fromJson(json, collectionType);
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while fetching website", e);
                }
            }

            @Override
            protected void onPostExecute(final List<Contact> contacts) {
                super.onPostExecute(contacts);
                resultTextView.setText(contacts.toString());

                progressDialog.cancel();
            }
        }.execute();
    }
}
