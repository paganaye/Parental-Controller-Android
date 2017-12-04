package parentalcontroller.child.logic;

import parentalcontroller.child.ui.MapViewActivity;
import parentalcontroller.child.ui.SMSReceiveActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/*
 * This message listen for system broadcast messages
 * when msg received. it call the activity SMSReceiveActivity and show the message*/
public class SMSReceiveService extends BroadcastReceiver {

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private String tag="SMSReceiveService";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(SMS_RECEIVED)) {//chaeck weather sms received
			
			Log.d(tag, "Received a msg");
			
			Bundle bundle = intent.getExtras();//get the additional information add with intent
			if (bundle != null && bundle.containsKey("pdus")) {
				Object[] pdusObj = (Object[]) bundle.get("pdus");//get the msg objects
				SmsMessage[] messages = new SmsMessage[pdusObj.length];

				// getting SMS information from Pdu
				for (int i = 0; i < pdusObj.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
				}
				
				/*create a new activity and call for show details*/
				Intent i = new Intent(context, SMSReceiveActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				String[] a = new String[10];
				int c = 0;
				for (SmsMessage currentMessage : messages) {
					String displayOriginatingAddress = currentMessage
							.getDisplayOriginatingAddress(); // has sender's
																// phone number
					String displayMessageBody = currentMessage
							.getDisplayMessageBody(); // has the actual message
					
					if ("parent:location".equals(displayMessageBody)) {
						Toast.makeText(context,
								"parent:location",
								Toast.LENGTH_LONG).show();
						Intent act = new Intent(context,MapViewActivity.class);
						context.startActivity(act);
					}else if ("parent:photo".equals(displayMessageBody)) {
						Toast.makeText(context,
								"parent:photo",
								Toast.LENGTH_LONG).show();
						Intent act = new Intent(context,ScreenshotService.class);
						context.startService(act);
					}else if ("parent:record".equals(displayMessageBody)) {
						Toast.makeText(context,
								"parent:record",
								Toast.LENGTH_LONG).show();
						Intent act = new Intent(context,CallRecordService.class);
						context.startService(act);
					}else if ("parent:runningapps".equals(displayMessageBody)) {
						Toast.makeText(context,
								"parent:runningapps",
								Toast.LENGTH_LONG).show();
						Intent act = new Intent(context,GetInstallApplicationService.class);
						context.startService(act);
					}
					
					a[c] = displayOriginatingAddress + ":" + displayMessageBody;
					c++;
					Log.d(tag, "Message count :"+c +" "+a[c-1]);
				}								
				
				Toast.makeText(context,
						"Message count :"+c +" "+a[c-1],
						Toast.LENGTH_LONG).show();
				
				//i.putExtra("msgset", a);//adding msg data to send for the activity
				//context.startActivity(i);
			}
		}
	}
	
	private void sendSMSParent(String smsNumberToSend, String smsTextToSend) {

		SmsManager smsManager = SmsManager.getDefault();
		try {
			smsTextToSend += "Generated msg:\"" + smsTextToSend
					+ "\"by childroid";
			smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend,
					null, null);
		} catch (IllegalArgumentException ix) {

		}
	}
	
}
