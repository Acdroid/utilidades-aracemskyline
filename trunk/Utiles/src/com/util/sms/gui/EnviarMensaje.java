/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms.gui;

import tactic.movil.R;
import tactic.movil.agenda.ListContact;
import tactic.movil.gui.PantallaPrincipal;
import tactic.movil.gui.interfazPrincipal;
import tactic.movil.sms.SendSMS;
import tactic.movil.util.Vibration.DoVibration;
import tactic.movil.util.gestures.GesturesRecognizer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * EnviarMensaje.java 07/03/2011
 * @author mtrujillo
 */
public class EnviarMensaje extends Activity implements OnEditorActionListener{

	private EditText textoSMS;
	private TextView mensajeSuperior;
	private Button btnEnviar;
	
	private String numTelefono = "656416115";
	private String nombreContacto;
	
	private GestureOverlayView overlay;
	private GesturesRecognizer gr;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			recibeLetra((String) msg.obj);
		}
	};
	
	private Handler handlerEnvioSms = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int resultado = (Integer) msg.obj;
			//Comprobamos el resultado del envío del mensaje
			if (resultado == -1){
				//Se ha producido algun tipo de error
			}
			else if (resultado == 0){
				setResult(interfazPrincipal.RESULT_SMS_CORRECTO);
				EnviarMensaje.this.finish();				
			}
		}
	};
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_sms);
		init();
	}
	
	
	/**
	 * <b>init</b><br><br>
	 *   private void init()<br>
	 * <ul>Metodo de apoyo para inicializar obtener todos los elementos
	 * de la interfaz.</ul><br><br>
	 */
	private void init(){
		
		//Obtenemos el mensaje enviado por intent
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			numTelefono = bundle.getString(ListContact.NUM_PHONE);
			nombreContacto = bundle.getString(ListContact.NAME_CONTACT);
		}
		//Iniciamos la libreria de gestos
		overlay = (GestureOverlayView) findViewById(R.id.gestures_send_sms);
		try {
			gr = new GesturesRecognizer(Environment.getExternalStorageDirectory() + "/TacTIC/Gestos", "gestures", overlay, handler, GesturesRecognizer.RECONOCEDOR_BASICO);
		} catch (Exception e) {
			Toast.makeText(this, "No se ha podido iniciar el reconocedor de gestos.", Toast.LENGTH_SHORT).show();
		}
		
		mensajeSuperior = (TextView) findViewById(R.id.sendSMS_contacto);
		//mensajeSuperior.setText(nombreContacto.toUpperCase());
		mensajeSuperior.setText("Haizea".toUpperCase());
		textoSMS = (EditText) findViewById(R.id.edittext_sms);
		textoSMS.setOnEditorActionListener(this);
		btnEnviar = (Button)findViewById(R.id.buttonEnviarSms);
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_DONE:
			clickEnviar(null);
			break;
		}
		return false;
	}
	
	
	/**
	 * <b>recibeLetra</b><br><br>
	 *   private void recibeLetra()<br>
	 * <ul>Recibe una letra y la asigna al textView. La letra esta en formato
	 * Base de datos de Alfabeto completo, por lo que se pueden recibir
	 * "borrados" o espacios en blanco por ejemplo.</ul><br><br>
	 * @param Letra o comando que queremos colocar en el TextView donde
	 * se va guardando el mensaje de texto que se enviara.
	 */
	private void recibeLetra(String letra){
		
		if ( (letra.length() == 0 ) || (letra.equals("")) ){
			DoVibration.ERROR((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));



		}
		else if (letra.subSequence(0, 1).equals("_")){
			textoSMS.setText(textoSMS.getText().toString() + " ");			
		}
		else if (letra.startsWith("Borrar")){
			//Borrado momentaneo //TODO
			String aux = textoSMS.getText().toString();
			if (aux.length() != 0)
				textoSMS.setText(aux.substring(0, aux.length() - 2));
		}
		else
			textoSMS.setText(textoSMS.getText().toString() + letra.substring(0, 1).toLowerCase());
		
	}
	
	public void clickEnviar(View v){
		String sms = textoSMS.getText().toString();
		if (sms.length() == 0){
			textoSMS.setError(getString(R.string.error_missing_sms));
			return;
		}
		SendSMS.sendSMS(numTelefono,sms,this,handlerEnvioSms);
		
			
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_salir_volver, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.salirMenuSalVol:
			setResult(interfazPrincipal.RESULT_SALIR);
			showDialog(interfazPrincipal.DIALOG_SALIR);
			return true;
		case R.id.volverMenuSalVol:
			setResult(RESULT_OK);
			EnviarMensaje.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch(id) {
		case interfazPrincipal.DIALOG_SALIR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("¿Seguro que quieres salir de la aplicación?")
			.setCancelable(false)
			.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Comunicador.this.finish();
					setResult(interfazPrincipal.RESULT_SALIR);
					EnviarMensaje.this.finish();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		default:
			return dialog = null;
		}
	}
	
}
