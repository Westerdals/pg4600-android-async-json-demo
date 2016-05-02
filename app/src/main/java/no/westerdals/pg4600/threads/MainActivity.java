package no.westerdals.pg4600.threads;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

    private void displayContacts(final List<Contact> contacts) {
        Log.d(TAG, contacts.toString());
        final List<String> contactsAsString = contactsToString(contacts);
        setListAdapter(makeListAdapter(contactsAsString));
    }

    private List<String> contactsToString(final List<Contact> contacts) {
        final List<String> strings = new ArrayList<>();

        for (final Contact contact : contacts) {
            strings.add(contact.toString());
        }

        return strings;
    }

    private List<Contact> contactsFromJson(final String json) {
        final Gson gson = new Gson();
        final Type collectionType = new TypeToken<List<Contact>>(){}.getType();
        return gson.fromJson(json, collectionType);
    }

    private void downloadContacts() {
        new AsyncTask<Void, Void, String>() {
            private final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Loading contacts");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(final Void... params) {
                try {
                    // Sleep for a couple of seconds to demo the progress dialog on fast connections.
                    // Can be safely removed!
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Log.e("MainActivity", "Something went wrong while sleeping…", e);
                }

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
            protected void onPostExecute(final String json) {
                super.onPostExecute(json);

                progressDialog.cancel();

                final List<Contact> contacts = contactsFromJson(json);
                displayContacts(contacts);
            }
        }.execute();
    }
}
