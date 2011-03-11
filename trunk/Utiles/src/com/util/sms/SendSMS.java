/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms;

import java.util.ArrayList;

import tactic.movil.util.Vibration.DoVibration;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * SendSMS.java 07/03/2011
 * @author mtrujillo
 */
public class SendSMS {
	public static Handler handler;
	public static final int OK = 0;
	public static final int ERROR = -1;

	@SuppressWarnings("unused")
	public static void sendSMS(String phoneNumber, String message,final Context mContext,Handler handlerResult)
	{        
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		
		handler = handlerResult;

		//---cuando el SMS ha sido enviado---
		mContext.registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					DoVibration.CustomSimple((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), 500);
					if (handler != null){
						Message msg = new Message();
						msg.obj = OK;
						handler.sendMessage(msg);
					}
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					DoVibration.ERROR((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE));
					Toast.makeText(mContext, "Ha ocurrido un error al enviar el sms.", Toast.LENGTH_SHORT).show();
					if (handler != null){
						Message msg = new Message();
						msg.obj = ERROR;
						handler.sendMessage(msg);
					}
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					DoVibration.ERROR((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE));
					Toast.makeText(mContext, "Ha ocurrido un error al enviar el sms.\nEl servicio de envio de SMS no esta disponible", Toast.LENGTH_SHORT).show();
					if (handler != null){
						Message msg = new Message();
						msg.obj = ERROR;
						handler.sendMessage(msg);
					}
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					DoVibration.ERROR((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE));
					Toast.makeText(mContext, "Ha ocurrido un error al enviar el sms.", Toast.LENGTH_SHORT).show();
					if (handler != null){
						Message msg = new Message();
						msg.obj = ERROR;
						handler.sendMessage(msg);
					}
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					DoVibration.ERROR((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE));
					Toast.makeText(mContext, "No hay cobertura para enviar el sms.", Toast.LENGTH_SHORT).show();
					if (handler != null){
						Message msg = new Message();
						msg.obj = ERROR;
						handler.sendMessage(msg);
					}
					break;
				}
			}
		}, new IntentFilter(SENT));

		//---cuando el SMS ha sido recibido---
		mContext.registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					long milisegundos[] = {0,80,100,130,80,160,50,100};
					DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), milisegundos);
					Toast.makeText(mContext, "¡ El SMS se ha recibido correctamente !", Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(mContext, "Se ha cancelado el envío del SMS.", Toast.LENGTH_SHORT).show();
					break;                        
				}
			}
		}, new IntentFilter(DELIVERED));     

		SmsManager sms = SmsManager.getDefault();
		//cortamos el mensaje en trozos pr si fuera más grande de 160
		ArrayList<String>  smsCut = sms.divideMessage(message);

		//creamos avisos para cada mensaje enviado
		ArrayList<PendingIntent> sentPIs = new ArrayList<PendingIntent>();
		ArrayList<PendingIntent> deliveredPIs = new ArrayList<PendingIntent>();
		for(int i=0; i<smsCut.size(); i++){
			sentPIs.add(PendingIntent.getBroadcast(mContext, i+1, new Intent(SENT), 0));
			deliveredPIs.add(PendingIntent.getBroadcast(mContext, i+1, new Intent(DELIVERED), 0));
		}

		sms.sendMultipartTextMessage(phoneNumber, null, smsCut, sentPIs, deliveredPIs);  
	}


}
