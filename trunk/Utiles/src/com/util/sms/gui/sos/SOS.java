/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms.gui.sos;

import java.util.Timer;
import java.util.TimerTask;

import tactic.movil.R;
import tactic.movil.comunicadorGuante.Comunicador;
import tactic.movil.util.Vibration.DoVibration;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
//import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * SOS.java 10/03/2011
 * @author mtrujillo
 */
public class SOS extends Activity {

	//private static final int DELAY_CAMBIO_COLOR = 1000;
	private static final int ID = 8;

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			if (SOSLayout== null) return;

			int i = (Integer) msg.obj;
			setColorFondo(i);
		}	
	};

	Timer tiempo;


	private LinearLayout SOSLayout = null;
	private TextView numTelefono;
	//private Button btnComunica;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos);
		init();
	}

	private void init(){
		SOSLayout = (LinearLayout) findViewById(R.id.layout_sos);
		numTelefono = (TextView)findViewById(R.id.sos_telefono);
		//btnComunica = (Button) findViewById(R.id.sos_button_comunica);


		Bundle b = getIntent().getExtras();
		if (b == null ){
			numTelefono.setText("No Disponible");
		}
		else{
			numTelefono.setText(b.getString(SOSsms.KEY_TELEFONO));
		}		

		//		tiempo = new Timer();
		//		tiempo.schedule(new timerTask(), DELAY_CAMBIO_COLOR, DELAY_CAMBIO_COLOR);
	}




	public void setColorFondo (int i){
		if (i == 0){ //Si el fondo es color rojo lo cambiamos a blanco
			setContentView(R.layout.sos);
		}else{
			//setContentView(R.layout.sos_dos);
		}
		//		SOSLayout.invalidate();

	}

	/**
	 * <b>clickComunicador</b><br><br>
	 *   public void clickEntrenador()<br>
	 * <ul>Metodo para lanzar la actividad entrenador gestual</ul><br><br>
	 */
	public void clickComunicador(View v) {
		Intent i = new Intent (SOS.this,Comunicador.class);
		DoVibration.OK((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
		startActivityForResult(i, ID);
	}


	/**
	 *<b>Traductor.java 08/02/2011</<b><br><br>
	 *
	 * @author Marcos Trujillo
	 * 
	 * Clase utilizada para crear una accion para 
	 * el timer. El metodo run de esta clase se 
	 * repetira sucesivamente.
	 */
	class timerTask extends TimerTask{
		private Boolean rojo = true;
		public void run(){

			Looper.prepare();
			Message msg = new Message();
			if (rojo){
				msg.obj = 0;
				handler.sendMessage(msg);
			}
			else{
				msg.obj = 1;
				handler.sendMessage(msg);
			}
			Looper.loop();
			Looper.myLooper().quit();
		}
	};


}
