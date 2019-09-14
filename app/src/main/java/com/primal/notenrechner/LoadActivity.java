package com.primal.notenrechner;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load2);

        final List<String> fileList = new ArrayList<>();
        final List<String> displayList = new ArrayList<>();

        fileList.clear();
        displayList.clear();

        ListView lv = (ListView)findViewById(R.id.listfiles);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, displayList);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);

        String path = getApplicationContext().getFilesDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int a = 0; a < files.length; a++)
        {
            displayList.add(files[a].getName().split("\\.")[0]);
            fileList.add(files[a].getName());
        }

        arrayAdapter.notifyDataSetChanged();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // add a list
                //Get the text file
                File file = new File(getApplicationContext().getFilesDir(), fileList.get(i));


                //Read text from file
                MainActivity.note_list.clear();
                arrayAdapter.notifyDataSetChanged();


                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        MainActivity.note_list.add(line);
                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }
            }

        });
    }
}
