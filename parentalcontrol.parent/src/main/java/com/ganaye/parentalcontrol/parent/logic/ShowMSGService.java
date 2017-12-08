package com.ganaye.parentalcontrol.parent.logic;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class ShowMSGService extends IntentService{

	public ShowMSGService(){
		super("ShowMSGService");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Uri uriSMSURI = Uri.parse("content://sms/sent");
		Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,
				null);
		String sms = "";
		while (cur.moveToNext()) {
			sms += "From :" + cur.getString(2) + " : " + cur.getString(12)
					+ "\n";
		}
		Intent i = new Intent(getApplicationContext(), com.ganaye.parentalcontrol.parent.ui.SMSReceiveActivity.class);
		i.putExtra("sms", sms);
		startService(i);
	}
	
	

}
