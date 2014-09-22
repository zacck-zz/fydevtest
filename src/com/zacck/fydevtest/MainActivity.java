package com.zacck.fydevtest;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements OnClickListener {
	EditText etUid,etAkey,etAppid,etPub0;
	Button btReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //do ui initialization 
        init();
    }

	private void init() {
		//link our ui elements to the functions
		etUid = (EditText)findViewById(R.id.etUid);
		etAkey = (EditText)findViewById(R.id.etKey);
		etAppid = (EditText)findViewById(R.id.etAppid);
		etPub0 = (EditText)findViewById(R.id.etPubO);
		
		btReq = (Button)findViewById(R.id.btRequest);
		
		//add a clicklistener to the button 
		btReq.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v ) {
		switch(v.getId())
		{
		case R.id.btRequest:
			
			break;
		}
		
	}
	
	/*
	 * lets make an async task class so we can call the network and get results 
	 * for now this will mostly use public variables in the class 
	 * ideally vars should be passed in when as needed 
	 * out put will be a list of the results 
	 * or a toast incase results are empty
	 */
	class MakeRequest extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
		
	}
   
}
