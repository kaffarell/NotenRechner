package com.example.notenrechner;

import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.lv);
        final Button btn = (Button) findViewById(R.id.btn);
        final EditText in_Note = (EditText)findViewById(R.id.editText2);
        final EditText in_Percentage = (EditText)findViewById(R.id.editText);
        final Button btn2 = (Button)findViewById(R.id.btn2);
        final TextView final_note = (TextView)findViewById((R.id.textView6));
        in_Percentage.setText("");
        in_Note.setText("");

        in_Note.setInputType(InputType.TYPE_CLASS_NUMBER);
        in_Percentage.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Create a List from String Array elements
        final List<String> note_list = new ArrayList<String>();

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, note_list);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNote = in_Note.getText().toString();
                String newPercentage = in_Percentage.getText().toString();
                if (newPercentage.matches("")){
                    newPercentage = "100";
                }
                if (newNote.matches("")){
                    in_Note.setText("");
                    in_Percentage.setText("");
                    in_Note.setFocusableInTouchMode(true);
                    in_Note.requestFocus();
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                }else {
                    String newNotePercentage = newNote + "," + newPercentage;
                    // Add new Items to List
                    note_list.add(newNotePercentage);
                /*
                    notifyDataSetChanged ()
                        Notifies the attached observers that the underlying
                        data has been changed and any View reflecting the
                        data set should refresh itself.
                 */
                    arrayAdapter.notifyDataSetChanged();
                    in_Note.setText("");
                    in_Percentage.setText("");

                    in_Note.setFocusableInTouchMode(true);
                    in_Note.requestFocus();
                }


            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                note_list.remove(i); // remove item at index in list datasource
                arrayAdapter.notifyDataSetChanged(); // call it for refresh ListView
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double noteSum = 0;
                double percentageSum = 0;
                for (int i = 0; i < note_list.size(); i++){
                    String str = note_list.get(i);
                    String[] str1 = str.split(",", 2);
                    int newNote;
                    int newPercentage;
                    newNote= Integer.parseInt(str1[0]);
                    newPercentage= Integer.parseInt((str1[1]));

                    noteSum = noteSum + (newNote * newPercentage);
                    percentageSum += newPercentage;
                }
                double finalNote = noteSum/percentageSum;
                final_note.setText(Double.toString(finalNote));
                if (finalNote > 6){
                    Toast.makeText(getApplicationContext(), "Yay!!", Toast.LENGTH_LONG).show();
                }else if (finalNote < 6){
                    Toast.makeText(getApplicationContext(), "R.I.P", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}