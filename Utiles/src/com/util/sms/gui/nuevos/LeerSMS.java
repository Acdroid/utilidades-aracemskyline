/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms.gui.nuevos;

import tactic.movil.R;
import tactic.movil.sms.ReceiverSMS;
import tactic.movil.traductor.MensajeInterno;
import tactic.movil.traductor.Traductor;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * LeerSMS.java 09/03/2011
 * @author mtrujillo
 */
public class LeerSMS extends Activity {
	private String textSMS = "TacTIC"; //Por si no se recibe ningun mensaje
	private String contacto = "TacTIC"; //Por si no se recibe ningun mensaje
	
	
	private TextView tituloContacto;
	private TextView cuerpoSMS;
	private ImageView imagen;
	
	private Traductor trad;
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			MensajeInterno resource = (MensajeInterno) msg.obj;
			if( resource.getIdImagen() != Traductor.FIN_TRADUCCION){
				getImagen().setImageResource(resource.getIdImagen());
				getCuerpoSMS().setText(resource.getTextoFormateado());
			}
			else{
				getImagen().setImageResource(R.drawable.malo_);
				getCuerpoSMS().setText(textSMS);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lector_sms_new);
		init();
	}
	
	private void init(){
		
		Bundle b = getIntent().getExtras();
		if (b != null){
			if (b.containsKey(ReceiverSMS.KEY_SMS))
				textSMS = b.getString(ReceiverSMS.KEY_SMS);
			if (b.containsKey(ReceiverSMS.KEY_CONTACTO))
				contacto = b.getString(ReceiverSMS.KEY_CONTACTO);			
		}
		
		tituloContacto = (TextView) findViewById(R.id.sms_nuevo_contacto);
		cuerpoSMS = (TextView) findViewById(R.id.sms_nuevo_textsms);
		imagen = (ImageView) findViewById(R.id.sms_nuevo_imagen);
		
		tituloContacto.setText("SMS de " + contacto);
		cuerpoSMS.setText(textSMS);
		
		trad = null;
		try {
			trad = new Traductor(0, 0,handler, 500, Traductor.IDIOMA_MALOSSI);
			trad.traduce(textSMS, "");
		} catch (Exception e) {
			Log.d("ERROR",e.getLocalizedMessage());
		}
		
	}

	public TextView getCuerpoSMS() {
		return cuerpoSMS;
	}

	public ImageView getImagen() {
		return imagen;
	}

	

}
