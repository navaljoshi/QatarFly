package com.androidsrc.client;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView response;
	EditText editTextAddress, editTextPort;
	Button buttonConnect, buttonClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		flyConnectServerPopup();



		buttonClear = (Button) findViewById(R.id.clearButton);
		response = (TextView) findViewById(R.id.responseTextView);



		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				response.setText("");
				flyConnectServerPopup(); // fn to show a connect pop up to enter IP and port .

			}
		});
	}

	void flyConnectServerPopup()
	{
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.popup_example);
		dialog.setTitle("Connect to Server");

		final EditText editIpText = (EditText) dialog.findViewById(R.id.addressEditText);
		final EditText editPortText = (EditText) dialog.findViewById(R.id.portEditText);
		Button btnSave          = (Button) dialog.findViewById(R.id.connectButton);

		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editIpText.getText()
						.toString(), Integer.parseInt(editPortText
						.getText().toString()), response);
				myClient.execute();
				dialog.cancel();

				setContentView(R.layout.material_design_login_form);
			}
		});
		dialog.show();
	}



}
