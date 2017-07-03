package com.androidsrc.client;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    TextView response;
    Button buttonForm;
    public Client myClient = null;
    public ImageView layout;
    public Dialog dialog = null;
    public EditText editName, editNumber, editEmail;
    TextView scoreText ;
    Button scoreButton,destination,fly,start;
    LinearLayout form ;
    public  String name, number, email,ip;
    Spinner spinner ;

    boolean flag,flag1 = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (ImageView) findViewById(R.id.background);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        editName = (EditText) findViewById(R.id.logName);
        editNumber = (EditText) findViewById(R.id.logMob);
        editEmail = (EditText) findViewById(R.id.logEmail);
        buttonForm = (Button) findViewById(R.id.logSub);
        destination = (Button) findViewById(R.id.destination);
        form = (LinearLayout)findViewById(R.id.linearLayout);
        fly = (Button) findViewById(R.id.fly);
        scoreText = (TextView)findViewById(R.id.scoreText);
        scoreButton = (Button)findViewById(R.id.scoreButton);
       // start = (Button)findViewById(R.id.start);
        SocketConnectServerPopup(); // calling
    }

    void SocketConnectServerPopup() {
        Log.d("Socket", "Inside Function : SocketConnectServerPopup ");
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.popup_example);
        dialog.setTitle("Connect to Server");

        final EditText editIpText = (EditText) dialog.findViewById(R.id.addressEditText);
        Button btnSave = (Button) dialog.findViewById(R.id.connectButton);
        spinner = (Spinner) findViewById(R.id.spinner);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Bucharest");
        categories.add("Kiev");
        categories.add("Warsaw");
        categories.add("Skopje");
        categories.add("Rome");
        categories.add("Pisa");

        categories.add("Milan");
        categories.add("Venice");
        categories.add("Geneva");
        categories.add("Berlin");
        categories.add("Munich");
        categories.add("Frankfurt");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);





        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("Socket", "Server Details Entered  ");
                Log.d("Socket", "IP : " + editIpText.getText());
                myClient = new Client(editIpText.getText().toString(),8008);
                myClient.execute();
                layout.setImageResource(R.drawable.tablet1);
                ip = editIpText.toString();
                dialog.dismiss();
                flag = true;

            }
        });
        dialog.show();
       // dialog.dismiss(); // to be removed
    }
    public void openGmail(Activity activity, String email, String subject, String content) {

        Log.d("Socket", "Inside Function : openGmail ");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.setType("*/*");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivityForResult(emailIntent, 1);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        if(flag1) {
            String item = parent.getItemAtPosition(position).toString();

            // Showing selected spinner item
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            if (myClient != null) {
                name = item.toString();
                myClient.msg = "fly;" + name;
            }
        }
        else
        {
            flag1 = true;
        }

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            Log.d("Socket", "Mail sent - sending done");
           // myClient.msg = "done";

        } catch (Exception ex) {ex.printStackTrace();
        }

    }

    class Client extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        Socket socket = null;
        int dstPort;
        String response = "";
        String mainresponse = "yo";
        TextView textResponse;
        public DataOutputStream ouputStream = null;
        public InputStream inputStream = null;
        String msg = null;


        Client(String addr, int port) {
            Log.d("Socket", "Entered Client Constructor");
            dstAddress = addr;
            dstPort = port;
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("Socket", "Entered doInBackground");
            try {
                socket = new Socket(dstAddress, dstPort);
                Log.d("Socket", "Created socket");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                        1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                inputStream = socket.getInputStream();
                ouputStream = new DataOutputStream(socket.getOutputStream());
                msg = "hi";
                ouputStream.write(msg.getBytes());
                Log.d("Socket", "Data sending to Server: " + msg);
                ouputStream.flush(); // Send off the data

			/*
			 * notice: inputStream.read() will block if no data return
			 */
                while ((bytesRead = inputStream.read(buffer)) != -1) {

                    ouputStream.write(msg.getBytes());
                    Log.d("Socket", "Data sending to Server: " + msg);
                    ouputStream.flush();
                    Thread.sleep(1000);
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response = "";
                    response = byteArrayOutputStream.toString("UTF-8");

                    Log.d("Socket", "Data received from Client > " + response + "  bytes received > " + bytesRead);
                    if (response.contains("hello")) {
                        Log.d("Socket", "Server connected");
                        mainresponse = response;
                        //mActivity.submitForm();
                        byteArrayOutputStream.flush();
                        publishProgress();

                    }
                    if (response.contains("yo")) {
                        Log.d("socket", "received response for one");
                        mainresponse = response;
                        //mActivity.countryPicker();
                        byteArrayOutputStream.flush();
                        publishProgress();

                    }
                    if (response.contains("okready")) {
                        Log.d("Socket", "received response for ready");
                        mainresponse = response;
                        //msg = "Socket;india";
                        byteArrayOutputStream.reset();
                        publishProgress();

                    }
                    if (response.contains("okfly")) {
                        Log.d("Socket", "received response for Socket");
                        mainresponse = response;
                        byteArrayOutputStream.reset();
                        publishProgress();

                    }
                    if (response.contains("okstart")) {
                        Log.d("Socket", "received response for start");
                        mainresponse = response;
                        byteArrayOutputStream.reset();
                        publishProgress();

                    }
                    if (response.contains("okscore")) {
                        Log.d("Socket", "received response for okscore");
                        mainresponse = response;
                        byteArrayOutputStream.reset();
                        publishProgress();
                    }
                    if (response.contains("naval")) {
                        Log.d("Socket", "Game finished");
                        mainresponse = response;
                        byteArrayOutputStream.reset();
                        publishProgress();
                    }
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            if ((myClient.mainresponse.contains("hello"))) {
                Log.d("Socket", "Received -----> hello");

             //   InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//Hide:
             //   imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                form.setVisibility(View.VISIBLE);
                layout.setImageResource(R.drawable.tablet1);
                dialog.cancel();
                buttonForm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Log.d("Socket", "Entered User Details ");



                        name = editName.getText().toString();
                        number = editNumber.getText().toString();
                        email = editEmail.getText().toString();

                        Log.d("Socket", "Name : " + name);
                        Log.d("Socket", "Number : " + number);
                        Log.d("Socket", "Email : " + email);
                        myClient.msg = "one";

                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);


                        // msg = "one";
                    }
                });
            }
            if ((myClient.mainresponse.contains("yo"))) {
                editNumber.setText("");
                editName.setText("");
                editEmail.setText("");
                Log.d("Socket", "Received -----> yo");
             //   InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//Hide:
             //   imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                form.setVisibility(View.GONE);
                myClient.msg = "ready";
                layout.setImageResource(R.drawable.tablet2);
            }

            if ((myClient.mainresponse.contains("okready"))) {
                Log.d("Socket", "Received -----> okready");
                layout.setImageResource(R.drawable.tablet3);
                //destination.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
            }

            if ((myClient.mainresponse.contains("okfly"))) {
                destination.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                fly.setVisibility(View.VISIBLE);
                Log.d("Socket", "Received -----> okfly");
                layout.setImageResource(R.drawable.tablet4);
                fly.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        myClient.msg = "start";
                    }
                });

            }
            if ((myClient.mainresponse.contains("okstart"))) {
                Log.d("Socket", "Received -----> okstart");
                fly.setVisibility(View.GONE);
                layout.setImageResource(R.drawable.tablet5);
                        myClient.msg = "score";
            }

            if ((myClient.mainresponse.contains("okscore"))) {
                scoreText.setVisibility(View.VISIBLE);
                scoreButton.setVisibility(View.VISIBLE);
                Log.d("Socket", "Received -----> okscore");



                String[] scoreCard = myClient.mainresponse.split(";");
                scoreText.setText(scoreCard[1]);
                Integer x = Integer.valueOf(scoreCard[1]);
                if(x>=60) {
                    layout.setImageResource(R.drawable.tablet7);
                    scoreText.setVisibility(View.GONE);

                }
                else {
                    layout.setImageResource(R.drawable.tablet6);
                    scoreText.setVisibility(View.VISIBLE);
                }
                Log.d("Socket", "Final Score is :" + scoreCard[1]);
              //  myClient.msg = "done";
                scoreButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        scoreText.setVisibility(View.GONE);
                        scoreButton.setVisibility(View.GONE);
                    /*    openGmail(MainActivity.this, "mhiarbangalore@gmail.com", "Fly with Qatar Airways ", "\n" +
                            "\nDear User, \n\nThank you for visiting the Glenmorangie Augmented Reality Zone. \n\nPlease find attached the pictures from the experience.\n\n" +
                            "We hope you enjoy your favourite single malt from the Glenmorangie Range.\n" +
                            "\n" +
                            "Thank you!\n" +
                            "\n" + "Warm regards,\n" + "\n" +
                            "Glenmorangie India");*/

                        myClient.msg = "done";
                        name = "";
                        number = "";
                        email = "";
                    }



                });

            }

            if ((myClient.mainresponse.contains("naval"))) {
                Log.d("Socket", "Received -----> okdone");
                try {
                     ouputStream.flush();
                     inputStream.reset();
                   // myClient.socket.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                // move to main screen

                    try {
                        Thread.sleep(3000);
                        Log.d("Socket", "Starting Again");
                      //  myClient = new Client(ip.toString(), 8008);
                      //  myClient.execute();
                        myClient.msg = "hi";
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
            super.onProgressUpdate(values);
        }


    }
}
