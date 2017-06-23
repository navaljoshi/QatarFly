package com.androidsrc.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Client extends AsyncTask<Void, Void, Void> {

	String dstAddress;
	Socket socket = null;
	int dstPort;
	String response = "";
    String mainresponse = "yo";
	TextView textResponse;
	public OutputStream ouputStream = null;
	public InputStream inputStream = null;
	public boolean flag = true;
	String msg = null;

	Client(String addr, int port,TextView textResponse) {
		Log.d("Socket","Entered Client Constructor");
		dstAddress = addr;
		dstPort = port;
		this.textResponse=textResponse;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		Log.d("Socket","Entered doInBackground");
		try {

			socket = new Socket(dstAddress, dstPort);
            Log.d("Socket", "Created socket");
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					1024);
			byte[] buffer = new byte[1024];

			 int bytesRead;
			 inputStream = socket.getInputStream();
			 DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
             msg = "hi";
             dOut.write(msg.getBytes());
             Log.d("Socket", "Data sending to Server: "+ msg);
             dOut.flush(); // Send off the data
			/*
			 * notice: inputStream.read() will block if no data return
			 */
			while ((bytesRead = inputStream.read(buffer)) != -1) {
			//while (true) {
				//bytesRead = inputStream.read(buffer);
                    Thread.sleep(1000);
				    dOut.write(msg.getBytes());
                    Log.d("Socket", "Data sending to Server: " + msg);
				    dOut.flush(); // Send off the data
                    //buffer = null;
					byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response = "";
					response = byteArrayOutputStream.toString("UTF-8");

					Log.d("Socket", "Data received from Client: " + response +"bytes received : "+bytesRead);
					if (response.contains("hello")) {
						Log.d("Socket", "Server connected");
                       // msg = "one";
                        mainresponse = response;
                       // byteArrayOutputStream.reset();
                        byteArrayOutputStream.flush();

					}
                if(response.contains("ok")) {
                         Log.d("socket", "received response for one");
                        // msg = "ready";
                         mainresponse = response;
                    byteArrayOutputStream.flush();

                     }
                if (response.contains("okready")) {
                    Log.d("Socket", "received response for ready");
                    mainresponse = response;
					//msg = "Socket;india";
                    byteArrayOutputStream.reset();

                }
                if (response.contains("okSocket")) {
                    Log.d("Socket", "received response for Socket");
                    mainresponse = response;
                    //msg =  "start";
                    byteArrayOutputStream.reset();

                }
                if (response.contains("okstart")) {
                    Log.d("Socket", "received response for start");
                    mainresponse = response;
                    //msg = "score";
                   //response = "";
                    byteArrayOutputStream.reset();
                  //  buffer = null;
                  //  Log.d("Socket", "Sending score ");
                }
                if (response.contains("okscore")) {
                    Log.d("Socket", "received response for one");
                    mainresponse = response;
                   // msg = "done";
                  // response = "";
                    byteArrayOutputStream.reset();
                  //  buffer = null;
                  //  Log.d("Socket", "Sending done ");
                }
				if (response.contains("okdone")) {
					Log.d("Socket", "Game finished");

					//clear sockets and streams .
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
		}
     catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        response = "IOException: " + e.toString();
    }finally {
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
	protected void onPostExecute(Void result) {
//		textResponse.setText(response);
		super.onPostExecute(result);
	}

	void sendData(Socket s,String val) {
		if (ouputStream != null) {
			Log.d("Socket", "sendData - received string for byte conversion :" + val);
			byte[] b = val.getBytes();
			try {
				ouputStream.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else
		{
			Log.d("Socket", "Socket is NUll for sending Data");
		}
	}


}
