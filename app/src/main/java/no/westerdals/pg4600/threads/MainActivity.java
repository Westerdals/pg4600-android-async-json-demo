package no.westerdals.pg4600.threads;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends ListActivity {
    private static final String TAG = "JSON LOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadContacts();
    }

    private ListAdapter makeListAdapter(final List<String> contacts) {
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
    }

    private void displayContacts(final String s) {
        Log.d(TAG, s);

        List<String> contacts = Arrays.asList(s.split("\\},"));
        setListAdapter(makeListAdapter(contacts));
    }

    private void downloadContacts() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("http://contacts.theneva.com/contacts").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    String json = "";

                    while (scanner.hasNextLine()) {
                        json += scanner.nextLine();
                    }

                    return json;
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while downloading contacts", e);
                }
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                displayContacts(s);
            }
        }.execute();
    }
}
