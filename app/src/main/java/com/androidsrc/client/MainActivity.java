package com.androidsrc.client;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import java.util.List;

public class MainActivity extends FragmentActivity {

	TextView response;
	Button Socket;
	Button dest;
	Button buttonForm;
    Button score;
	public Client myClient = null;
	public static  String countryName = null;
	public RelativeLayout rLayout ;
	public String msg = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tablet1);

		// Full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		SocketConnectServerPopup(); // calling
	}

	void SocketConnectServerPopup()
	{
		Log.d("Socket","Inside Function : SocketConnectServerPopup ");
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.popup_example);
		dialog.setTitle("Connect to Server");

		final EditText editIpText = (EditText) dialog.findViewById(R.id.addressEditText);
		final EditText editPortText = (EditText) dialog.findViewById(R.id.portEditText);
		Button btnSave  = (Button) dialog.findViewById(R.id.connectButton);

		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("Socket","Server Details Entered  ");
				Log.d("Socket","IP : " + editIpText.getText());
				Log.d("Socket","Port : " + editPortText.getText());

				new Handler().post(new Runnable() {
					@Override
					public void run() {
						// Code here will run in UI thread
						Log.d("Socket","Called Client Thread");
						/*  myClient = new Client(editIpText.getText()
								.toString(), Integer.parseInt(editPortText
								.getText().toString()), response);*/
						myClient = new Client("192.168.1.8",8008,response);
						myClient.execute();
                        if(myClient == null)
                        {
                            Log.d("Socket","Myclient is NULL - core ");
                        }
                        while(true) {
                            try {
                                synchronized(this){
                                    wait(1000);
                                }
                            }
                            catch(InterruptedException ex){
                            }
                            Log.d("Socket","Waiting for Hello");
                            if ((myClient.mainresponse.contains("hello"))) {
                                Log.d("Socket","Hello received");
                                dialog.cancel();
                                break;
                            }
                        }
                        submitForm(); // calling

                    }
				});
			}
		});
		dialog.show();
	}

void submitForm()
{
	Log.d("Socket","Inside Function : submitForm ");

	//rLayout = (RelativeLayout)findViewById(R.id.scrollView1);
	//rLayout.setBackgroundResource(R.drawable.tablet6);
	final EditText editName, editNumber, editEmail;

    editName = (EditText) findViewById(R.id.logName);
	editNumber = (EditText) findViewById(R.id.logMob);
	editEmail = (EditText) findViewById(R.id.logEmail);
	buttonForm = (Button) findViewById(R.id.logSub);

	buttonForm.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Log.d("Socket","Entered User Details " );
			Log.d("Socket","Name : " + editName.getText());
			Log.d("Socket","Number : " + editNumber.getText());
			Log.d("Socket","Email : " + editEmail.getText());
            myClient.msg = "one";
			msg = "one";
		//	setContentView(R.layout.basic);
            while(true) {
                Log.d("Socket","Waiting for ok " );
                try {
                    synchronized(this){
                        wait(1000);
                    }
                }
                catch(InterruptedException ex){
                }
                if ((myClient.mainresponse.contains("ok"))) {
                    myClient.msg = "ready";
                    Log.d("Socket","Received OK");
                   // setContentView(R.layout.tablet3); -> here tablet 3 is a xml layout
                    Log.d("Socket","setting layout ");
                    ImageView layout = (ImageView) findViewById(R.id.background);
                    layout.setImageDrawable(getResources().getDrawable(R.drawable.tablet2)); // -> here tablet 2 is a image in drawable
                  //  getWindow().getDecorView().findViewById(android.R.id.).invalidate();
					break;
                }
            }
            countryPicker();
		}
	});
}

void countryPicker()
{
    Log.d("Socket","Inside Function : countryPicker ");

    while(true) {
        try {
            synchronized(this){
                wait(1000);
            }
        }
        catch(InterruptedException ex){
        }
        Log.d("Socket","Waiting for okready " );
        if ((myClient.mainresponse.contains("okready"))) {
            Log.d("Socket","Received okready");
            setContentView(R.layout.tablet3);
            break;
        }
    }

	dest = (Button) findViewById(R.id.dest);
	dest.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {


			Log.d("Socket","Country Picker Clicked ");

			final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
			picker.setListener(new CountryPickerListener() {
				@Override
				public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
					// Implement your code here
					Log.d("Socket","Destination : "+name);
					myClient.msg = "fly;"+name;
                    while(true) {
                        try {
                            synchronized(this){
                                wait(1000);
                            }
                        }
                        catch(InterruptedException ex){
                        }
                        Log.d("Socket","Waiting for okfly " );
                        if ((myClient.mainresponse.contains("okfly"))) {
                            setContentView(R.layout.tablet4);
                            picker.dismiss();
                            break;
                        }
                    }

                    letsfly();
				}
			});
			picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
		}
	});
}
	void letsfly()
	{
		Log.d("Socket","Inside Function : letsfly ");

		Socket = (Button) findViewById(R.id.dest1);

        myClient.msg = "start";
        while(true) {
            try {
                synchronized(this){
                    wait(1000);
                }
            }
            catch(InterruptedException ex){
            }
            Log.d("Socket","Waiting for okstart " );
            if ((myClient.mainresponse.contains("okstart"))) {
                setContentView(R.layout.tablet5);

                break;
            }
        }
		Socket.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Socket.setVisibility(View.VISIBLE);
				Log.d("Socket","Button Clicked on lets Socket page ");
                setContentView(R.layout.tablet5);
                score();

			}
		});
	}

	void score()
	{
		Log.d("Socket","Inside Function : score ");

		score = (Button) findViewById(R.id.dest3);

        TextView tt = (TextView)findViewById(R.id.score);

		myClient.msg = "score";
		while(true) {
			try {
				synchronized(this){
					wait(1000);
				}
			}
			catch(InterruptedException ex){
			}
			Log.d("Socket","Waiting for okscore " );
			if ((myClient.mainresponse.contains("okscore"))) {

                String[] scoreCard = myClient.mainresponse.split(";");
				Log.d("Socket","Final Score is :"+scoreCard[1]);
				setContentView(R.layout.tablet6);
                tt.setText(scoreCard[1]);
				//dest.setVisibility(View.GONE);
				//Socket.setVisibility(View.VISIBLE);
				break;
			}
		}

        Socket.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                openGmail(MainActivity.this, "mhiarbangalore@gmail.com", "Glenmorangie Augmented Reality Experience ", "\n" +
                        "\nDear User, \n\nThank you for visiting the Glenmorangie Augmented Reality Zone. \n\nPlease find attached the pictures from the experience.\n\n" +
                        "We hope you enjoy your favourite single malt from the Glenmorangie Range.\n" +
                        "\n" +
                        "Thank you!\n" +
                        "\n" + "Warm regards,\n" + "\n" +
                        "Glenmorangie India");
            }
        });

	}

    public  void openGmail(Activity activity,String email, String subject, String content) {

        Log.d("Socket","Inside Function : openGmail ");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.setType("*/*");



        // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        //activity.startActivity(emailIntent);
        startActivityForResult(emailIntent, 1);


    }

}
