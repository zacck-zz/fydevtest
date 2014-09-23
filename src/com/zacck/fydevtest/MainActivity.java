package com.zacck.fydevtest;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	EditText etUid, etAkey, etAppid, etPub0;
	Button btReq;
	String TAG = "fydevtestMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// do ui initialization
		init();
	}

	private void init() {
		// link our ui elements to the functions
		etUid = (EditText) findViewById(R.id.etUid);
		etAkey = (EditText) findViewById(R.id.etKey);
		etAppid = (EditText) findViewById(R.id.etAppid);
		etPub0 = (EditText) findViewById(R.id.etPubO);

		btReq = (Button) findViewById(R.id.btRequest);

		// add a clicklistener to the button
		btReq.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btRequest:
			// lets run our thread here
			MakeRequest mr = new MakeRequest();
			mr.execute();
			break;
		}

	}

	/*
	 * lets make an async task class so we can call the network and get results
	 * for now this will mostly use public variables in the class ideally vars
	 * should be passed in when as needed out put will be a list of the results
	 * or a toast incase results are empty
	 */
	class MakeRequest extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// get the string resources so we can use all this stuff
			Resources mRes = getResources();
			// lets make a timestamp

			String backFromServer = "";

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("appid", etAppid
					.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("uid", etUid.getText()
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("locale", mRes
					.getString(R.string.locale)));
			nameValuePairs.add(new BasicNameValuePair("os_version",
					android.os.Build.VERSION.RELEASE));
			nameValuePairs.add(new BasicNameValuePair("timestamp", System
					.currentTimeMillis() + ""));
			nameValuePairs.add(new BasicNameValuePair("offer_types", mRes
					.getString(R.string.offer_types)));
			nameValuePairs.add(new BasicNameValuePair("pub0", mRes
					.getString(R.string.pubo)));
			String t = genHash(nameValuePairs);

			// lets create a http client and use it to do a post to get some
			// results
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://api.sponsorpay.com/feed/v1/offers.json?");

			try {
				// Add your data to send to the api
				Log.d(TAG, "adding hashkey now");
				nameValuePairs.add(new BasicNameValuePair("hashkey", t));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Execute HTTP Post Request
				Log.d(TAG, "hash is" + t);
				HttpResponse response = httpclient.execute(httppost);
				Log.d(TAG, "Status is "
						+ response.getStatusLine().getStatusCode());
				backFromServer = EntityUtils.toString(response.getEntity());
				Log.d(TAG, backFromServer);

			} catch (Exception e) {
				// log an exception if we find one
				Log.d(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}

	private String genHash(List<NameValuePair> nameValuePairs) {
		String mhash = "";
		String mYHash = "";
		String ret = null;
		Resources hashRes = getResources();
		Comparator<NameValuePair> comp = new Comparator<NameValuePair>() { // solution
																			// than
																			// making
																			// method
																			// synchronized
			@Override
			public int compare(NameValuePair p1, NameValuePair p2) {
				return p1.getName().compareTo(p2.getName());
			}
		};

		Collections.sort(nameValuePairs, comp);
		for (int i = 0; i < nameValuePairs.size(); i++) {
			mhash += nameValuePairs.get(i).getName() + "="
					+ nameValuePairs.get(i).getValue() + "&";
			Log.d(TAG, "inner" + mhash);
		}
		mYHash = hashRes.getString(R.string.key);
		mhash += mYHash;
		Log.d("conc string", mhash);
		try {
			Log.d("running md on", mhash);

			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(mhash.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			ret = sb.toString();			
			Log.d("hk", ret);
		} catch (Exception e) {

		}
		return ret;

	}

}
