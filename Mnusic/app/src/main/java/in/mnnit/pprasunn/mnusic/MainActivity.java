package in.mnnit.pprasunn.mnusic;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    FloatingActionButton fb;
    String[] items;
    public ArrayList<File>mySongs;
    MediaMetadataRetriever mmr;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lvPlaylist);

        String sdpath = null;
        if(new File("/storage/extSdCard/").exists())
            sdpath="/storage/extSdCard/";
        else if(new File("/storage/sdcard1/").exists())
            sdpath="/storage/sdcard1/";
        else if(new File("/storage/usbcard1/").exists())
            sdpath="/storage/usbcard1/";
        else if(new File("/storage/extSdCard0/").exists())
            sdpath="/storage/extSdCard0/";
        else if(new File("/storage/sdcard0/").exists())
            sdpath="/storage/sdcard0/";



        if(sdpath!=null)
            mySongs=findSongs(new File(sdpath));
        else
            mySongs = findSongs(Environment.getExternalStorageDirectory());//Environment.getExternalStorageDirectory()
        items = new String[mySongs.size()];


        for (int i = 0; i < mySongs.size(); i++) {
            //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "");//replace wav also
        }

        fb=(FloatingActionButton)findViewById(R.id.fab);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));
            }
        });

         final  int position=1;





        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ArrayList<File>arrayList=(ArrayList<File>) Collections.shuffle(mySongs);
                Collections.shuffle(mySongs);
                //Random rn=new Random();
                //int max=mySongs.size();
                //int random=rn.nextInt()%max;
               // position=random;
                startActivity(new Intent(MainActivity.this,Player.class).putExtra("pos",position).putExtra("songlist",mySongs));
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
            } else {
                String single = singleFile.getName();
                if (single.endsWith(".mp3") || single.endsWith(".wav") || single.endsWith(".3gp") || single.endsWith(".webm") || single.endsWith(".wma") || single.endsWith(".au")) {
                    al.add(singleFile);

                    //img[]=mmr.getEmbeddedPicture();


                }
            }
        }
        return al;
    }




    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }


    //for notification





}
