/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms.gui.sos;

import tactic.movil.sms.SendSMS;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * SOSsms.java 10/03/2011
 * @author mtrujillo
 */
public class SOSsms implements Runnable{
	
	private static String mapsGoogle="http://maps.google.es/?ll=";
	private static String CONTACTO_DEFECTO = "656416115";
	public static String KEY_TELEFONO = "TELEFONO";
	private static Boolean DEBUG = true;
	
	public ProgressDialog pd;
	private Context context;
	
	public String telefonoAyuda="";
	
	
	
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	private Location currentLocation;
	private String sms="";
	
	
	public SOSsms(Context mContext){
		context=mContext;
		telefonoAyuda= CONTACTO_DEFECTO;
		
		//Iniciamos los dialogos
		lanzaDialog();
		//Iniciamos los hilos de busqueda de señal GPS
		Thread thread = new Thread(this);
		thread.start();
	}
	
	private void lanzaDialog(){
		
		DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context,"Señal GPS no encontrada. Mandando SMS sin coordenadas.",Toast.LENGTH_LONG).show();
                handlerNormal.sendEmptyMessage(0);
            }
		};
		pd = ProgressDialog.show(context,"Buscando...", "Buscando localizacion para mandar el SMS de ayuda",true, true, dialogCancel);
	}

	@Override
	public void run() {
		
		mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Looper.prepare();
			Toast.makeText(getContext(),"GPS",Toast.LENGTH_SHORT).show();
			mLocationListener = new MyLocationListener();
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
			Looper.loop(); 
			Looper.myLooper().quit(); 
			
		} else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			Looper.prepare();
			Toast.makeText(getContext(),"Triangulacion",Toast.LENGTH_SHORT).show();
			mLocationListener = new MyLocationListener();
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		}else{
            Toast.makeText(context,"No se encuentra señal se procede a mandar un mensaje normal",Toast.LENGTH_LONG).show();
            Looper.prepare();
            handlerNormal.sendEmptyMessage(0);
            Looper.loop();
            Looper.myLooper().quit();
		}	
		
	}
	
	
	
	
	public Context getContext(){
		return context;
	}
	
	public String getTelefonoAyuda() {
		return telefonoAyuda;
	}

	public void setCurrentLocation(Location loc){
		this.currentLocation = loc;
	}
	
	public Location getCurrentLocation(){
		return currentLocation;
	}
	
	
	
	
	/**
	 * SOSsms.java 11/03/2011
	 * @author mtrujillo
	 * 
	 * Clase de apoyo que extiende de LocationListener
	 * que se utiliza para gestionar los cambios
	 * en la recepción GPS tales como una nueva localizacion
	 * o un cambio de Status
	 */
	private class MyLocationListener implements LocationListener 
    {
        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                setCurrentLocation(loc);
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    } 
	
	//Handler para cuando 
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			mLocationManager.removeUpdates(mLocationListener);
	    	if (currentLocation!=null) {
	    		sms += "AYUDA,tengo una emergencia!\nEstoy en ";
	    		sms += mapsGoogle;
	    		sms += currentLocation.getLatitude();
	    		sms+=",";
	    		sms +=currentLocation.getLongitude();
	    		sms += " \n(Copia la direccion en un explorador web para ver el mapa)";
	    		
	    		if (DEBUG)
	    			Toast.makeText(context, sms, Toast.LENGTH_LONG).show();
	    		else
	    			SendSMS.sendSMS(getTelefonoAyuda(), sms, context, handlerEnvioSms);
	    		
	    		Intent i = new Intent(context,SOS.class);
	    		i.putExtra("TELEFONO",getTelefonoAyuda() );
	    		context.startActivity(i);
	    	}
		}
	};
	
	public Handler handlerNormal = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			mLocationManager.removeUpdates(mLocationListener);
	    	if (currentLocation!=null) {
	    		sms += "AYUDA,tengo una emergencia!" +
	    				"No funciona la geolocalizacion para mandarte mi posicion";
	    	}
	    	
	    	if (DEBUG)
	    		Toast.makeText(context, sms, Toast.LENGTH_LONG).show();
	    	else
	    		SendSMS.sendSMS(getTelefonoAyuda(), sms, context, handlerEnvioSms);
	    	
	    	
    		Intent i = new Intent(context,SOS.class);
    		i.putExtra("TELEFONO",getTelefonoAyuda() );
    		context.startActivity(i);
		}
	};
	
	public Handler handlerEnvioSms = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int resultado = (Integer) msg.obj;
			//Comprobamos el resultado del envío del mensaje
			if (resultado == -1){
				//Se ha producido algun tipo de error
			}
			else if (resultado == 0){
								
			}
		}
	};
	
	
	
	
	
	

}
