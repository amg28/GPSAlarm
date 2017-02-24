package a1stgroup.gpsalarm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import static a1stgroup.gpsalarm.ListActivity.selectedMarkerData;
import static a1stgroup.gpsalarm.MapsActivity.markerDataList;
import static a1stgroup.gpsalarm.MapsActivity.recentDataList;
import static android.R.id.list;

/**
 * Prilozhenie startuet s etoj stranici, esli estj soxranennie tochki, inache perexodit v MapsActivity.
 */

public class MyStartActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            markerDataList = (ArrayList<MarkerData>) InternalStorage.readObject(this, "myFile");
            recentDataList = (ArrayList<MarkerData>) InternalStorage.readObject(this, "myFile2");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (markerDataList.size() == 0) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

        else {

            setContentView(R.layout.activity_start);

            final ArrayAdapter myAdapter = new MyCustomizedAdapter(this, MapsActivity.markerDataList);
            final ArrayAdapter myAdapter2 = new MyCustomizedAdapter(this, MapsActivity.recentDataList);

            final ListView listView = (ListView) findViewById(R.id.listView);

            Collections.sort(markerDataList, new Comparator<MarkerData>() {

            /* This comparator will sort MarkerData objects alphabetically. */

                @Override
                public int compare(MarkerData a1, MarkerData a2) {

                    // String implements Comparable
                    return (a1.getName().toString()).compareTo(a2.getName().toString());
                }
            });

            listView.setAdapter(myAdapter);

            final ListView myListView2 = (ListView) findViewById(R.id.listView);
            myListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {        // Dobavlenij kod 21.02,2017
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedMarkerData = (MarkerData) myAdapter.getItem(i);
                    Toast.makeText(MyStartActivity.this, "Alarm Set: " + selectedMarkerData.getName(), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(MyStartActivity.this, MapsActivity.class);
                    startActivity(myIntent);
                }
            });

            final ListView listViewNew = (ListView) findViewById(R.id.listViewRecent);
           // listViewNew.setAdapter(null);
            listViewNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //myAdapter2.clear();
                     selectedMarkerData = (MarkerData) myAdapter.getItem(i);
                   // myAdapter2.insert(myAdapter.getItem(i),0);

                   // listViewNew.setAdapter(myAdapter2);
                    Toast.makeText(MyStartActivity.this, "Alarm Set: " + selectedMarkerData.getName(), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(MyStartActivity.this, MapsActivity.class);
                    startActivity(myIntent);
                }
            });



            myListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    recentDataList.clear();

                    recentDataList.add((MarkerData) myAdapter.getItem(i));
                    selectedMarkerData = (MarkerData) myAdapter.getItem(i);
                    saveRecentDataList();
//                    myAdapter2.insert(myAdapter.getItem(i),0);

                    listViewNew.setAdapter(myAdapter2);
                    Toast.makeText(MyStartActivity.this, "Alarm Set: " + selectedMarkerData.getName(), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(MyStartActivity.this, MapsActivity.class);
                    startActivity(myIntent);
                }
                });

            myListView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MyStartActivity.this);
                    alert.setMessage("Are you sure you want to delete this?");
                    alert.setCancelable(false);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MapsActivity.markerDataList.remove(i);
                            myAdapter.notifyDataSetChanged();
                            saveMarkerDataList();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    alert.show();
                    return true;
                }
            });
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private boolean saveMarkerDataList() {
        try {
            InternalStorage.writeObject(this, "myFile", MapsActivity.markerDataList);
            return true;
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save alarm", Toast.LENGTH_SHORT).show();
            Log.e("IOException", e.getMessage());
        }
        return false;
    }


    private boolean saveRecentDataList() {
        try {
            InternalStorage.writeObject(this, "myFile2", MapsActivity.recentDataList);
            return true;
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save alarm", Toast.LENGTH_SHORT).show();
            Log.e("IOException", e.getMessage());
        }
        return false;
    }


    public void toMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MyStart Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
