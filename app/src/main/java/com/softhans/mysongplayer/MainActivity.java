package com.softhans.mysongplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private String[] itemsAll;

    private ListView mSongsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appExternalStoragePermission();

        mSongsList = findViewById(R.id.songsList);

    }

    public void appExternalStoragePermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        displayAudioFileName();
                    }

                    @Override public void onPermissionDenied(PermissionDeniedResponse response)
                    {

                    }

                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    public ArrayList<File> readOnlyAudioFiles(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] allFiles=file.listFiles();

        for(File individualFile : allFiles)
        {
            if(individualFile.isDirectory() && !individualFile.isHidden())
            {
                arrayList.addAll(readOnlyAudioFiles(individualFile));
            }
            else
            {
                if(individualFile.getName(). endsWith(".mp3") || individualFile.getName(). endsWith(".aac") || individualFile.getName(). endsWith(".wav") || individualFile.getName(). endsWith(".wma"))
                {
                    arrayList.add(individualFile);
                }
            }
        }
        return arrayList;
    }

    public  void displayAudioFileName()
    {
        final ArrayList<File> audioFiles = readOnlyAudioFiles(Environment.getExternalStorageDirectory());

        itemsAll =new String[audioFiles.size()];

        for (int songCounter=0; songCounter<audioFiles.size(); songCounter++)
        {
            itemsAll[songCounter] = audioFiles.get(songCounter).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, itemsAll);
        mSongsList.setAdapter(arrayAdapter);
     }
}
