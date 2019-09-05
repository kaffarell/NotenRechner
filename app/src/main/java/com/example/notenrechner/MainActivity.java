package com.example.notenrechner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {

    private String m_Text = "";

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
        final Button button = (Button)findViewById(R.id.button);
        final Button removeFile = (Button)findViewById(R.id.button5);

        final Button loadButton = (Button)findViewById(R.id.button2);
        in_Percentage.setText("");
        in_Note.setText("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            negNotification();
        }


        //set move file button and input when keyboard is out
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //set number keyboard
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
                if (newNote.matches("") || Double.parseDouble(newNote) < 3 || Double.parseDouble(newNote) > 10 || Double.parseDouble(newPercentage) > 100 || Double.parseDouble(newPercentage) <= 0){
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
                    final_note.setText("");
                }


            }
        });

        removeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_note.setText("");
                final List<String> fileList= new ArrayList<String>();
                String path = getApplicationContext().getFilesDir().toString();
                File directory = new File(path);
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++)
                {
                    fileList.add(files[i].getName());
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ein Fach auswählen");

                // add a list
                final String[] item = fileList.toArray(new String[fileList.size()]);
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note_list.clear();
                        arrayAdapter.notifyDataSetChanged();
                        File dir = getFilesDir();
                        File file = new File(getApplicationContext().getFilesDir(), item[which]);
                        boolean deleted = file.delete();
                        if (deleted == true){
                            Toast.makeText(getApplicationContext(), "Fach gelöscht", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

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
                double finalNoteRound = Math.round(finalNote * 100.0) / 100.0;
                final_note.setText(Double.toString(finalNoteRound));
                if (finalNoteRound >= 6){
                    Toast.makeText(getApplicationContext(), "Yay!!", Toast.LENGTH_LONG).show();
                    final_note.setTextColor(Color.parseColor("#00FF00"));
                }else if (finalNoteRound < 6){
                    Toast.makeText(getApplicationContext(), "R.I.P", Toast.LENGTH_LONG).show();
                    final_note.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_note.setText("");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String filename = m_Text.toString();
                        File file = new File(getApplicationContext().getFilesDir(), filename + ".txt");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                        for (int i = 0; i < note_list.size(); i++) {
                            try {
                                bw.write(note_list.get(i).toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bw.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_note.setText("");
                final List<String> fileList= new ArrayList<String>();
                String path = getApplicationContext().getFilesDir().toString();
                File directory = new File(path);
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++)
                {
                    fileList.add(files[i].getName());
                }

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ein Fach auswählen");

                // add a list
                final String[] item = fileList.toArray(new String[fileList.size()]);
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get the text file
                        File file = new File(getApplicationContext().getFilesDir(), item[which]);
                        //Read text from file
                        note_list.clear();
                        arrayAdapter.notifyDataSetChanged();


                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                note_list.add(line);
                            }
                            br.close();
                        }
                        catch (IOException e) {
                            //You'll need to add proper error handling here
                        }

                    }
                });


                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void negNotification(){
        //read all files to see if someone is neg. and send push notific.
        final List<String> fileList= new ArrayList<>();
        final List<String> markList = new ArrayList<>();
        final List<String> negSub = new ArrayList<>();
        String path = getApplicationContext().getFilesDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            fileList.add(files[i].getName());
        }

        for (int i = 0; i < fileList.size(); i++){
            markList.clear();
            //Get the text file
            File file = new File(getApplicationContext().getFilesDir(), fileList.get(i));
            //Read text from file


            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    markList.add(line);
                }
                br.close();
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
            double noteSum = 0;
            double percentageSum = 0;
            for (int a = 0; a < markList.size(); a++){
                String str = markList.get(a);
                String[] str1 = str.split(",", 2);
                int newNote;
                int newPercentage;
                newNote= Integer.parseInt(str1[0]);
                newPercentage= Integer.parseInt((str1[1]));

                noteSum = noteSum + (newNote * newPercentage);
                percentageSum += newPercentage;
            }
            double finalNote = noteSum/percentageSum;
            double finalNoteRound = Math.round(finalNote * 100.0) / 100.0;
            if (finalNote < 6){
                String str = fileList.get(i).substring(0, fileList.get(i).indexOf("."));
                negSub.add(str);
            }

        }


        for (int b = 0; b < negSub.size(); b++){
            showNotification("NotenRechner", "Du bist negativ in: " + negSub.get(b), String.valueOf(b));
        }



    }
    void showNotification(String title, String message, String index) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("negId",
                    "negName",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Send notification when negative");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "negId")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); //   clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(Integer.parseInt(index), mBuilder.build());
    }

}