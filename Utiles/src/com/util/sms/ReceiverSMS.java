/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms;

import tactic.movil.R;
import tactic.movil.gui.interfazPrincipal;
import tactic.movil.sms.gui.nuevos.LeerSMS;
import tactic.movil.util.Vibration.DoVibration;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;

/**
 * ReceiverSMS.java 09/03/2011
 * @author mtrujillo
 */
public class ReceiverSMS extends BroadcastReceiver {
	
	public static String KEY_SMS = "SMS";
	public static String KEY_CONTACTO = "CONTACTO";
	
	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID = 1984;


	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context mContext, Intent intent) {

		//---Recuperamos el SMS---
		Bundle bundle = intent.getExtras();        
		SmsMessage[] msgs = null;
		String textoSMS = "";      
		
		if (bundle != null)
		{
			//---Recuperamos el mensaje---
			// PDU = protocol discription unit
			
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				textoSMS += "SMS de " + msgs[i].getOriginatingAddress() + "  \n";                     
				textoSMS += msgs[i].getMessageBody().toString();
				textoSMS += "\n";        
			}
			//---Mostrar el mensaje del SMS---
			//Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			DoVibration.SMS((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE));
			creaNotification(textoSMS,mContext,msgs[0].getOriginatingAddress());

		}
	}

	private void creaNotification(String textSMS,Context mContext,String contacto){
		mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notifyDetails = new Notification(R.drawable.tacticlogo,"Nuevo SMS",System.currentTimeMillis());
        Intent i = new Intent (mContext,LeerSMS.class);
        i.putExtra(KEY_SMS, textSMS);
        i.putExtra(KEY_CONTACTO, contacto);        
        //PendingIntent myIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, SMSNuevosViejos.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent myIntent = PendingIntent.getActivity(mContext, 0, i, 0);
        notifyDetails.setLatestEventInfo(mContext, textSMS, "Pulsa para abrir", myIntent);
        notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
	}

}
