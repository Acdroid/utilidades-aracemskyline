/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.sms.gui;


import tactic.movil.R;
import tactic.movil.gui.interfazPrincipal;
import tactic.movil.agenda.ListContact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * ReadWrite.java 03/03/2011
 * @author mtrujillo
 */
@SuppressWarnings("unused")
public class SMSNuevosViejos extends Activity {


	public static final int TIPO_LIBRO = 0;
	public static final int TIPO_FICH = 1;

	private ImageButton btnNew;
	private ImageButton btnOld;

	public static final int ID = 8;
	static final int DIALOG_SALIR = 0;
	
	private Context mContext;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevos_viejos);
		init();
		initActions();
	}

	/**
	 * <b>init</b><br><br>
	 *   private void init()<br>
	 * <ul>Metodo de apoyo para inicializar obtener todos los elementos
	 * de la interfaz.</ul><br><br>
	 */
	private void init(){
		btnNew = (ImageButton) findViewById(R.id.buttonNewSms);
		btnOld = (ImageButton) findViewById(R.id.buttonOldSms);
		mContext = this;

	}

	
	/**
	 * <b>clickImagen</b><br><br>
	 *   public void clickImagen()<br>
	 * <ul>Este metodo es llamado cuando la imagen del androide
	 * de la presentacion es pulsada para que cambie por otra.</ul><br><br>
	 */
	private void initActions(){
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Mensajes Nuevos", Toast.LENGTH_SHORT).show();
			}
		});
		btnOld.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Intent i = new Intent(ReadWrite.this, ListContact.class);
				Toast.makeText(mContext, "Mensajes antiguos", Toast.LENGTH_SHORT).show();
			}
		});

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
			showDialog(DIALOG_SALIR);
			return true;
		case R.id.volverMenuSalVol:
			setResult(RESULT_OK);
			SMSNuevosViejos.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case interfazPrincipal.RESULT_OK:
				break;
			case interfazPrincipal.RESULT_ERROR:
				break;
			case interfazPrincipal.RESULT_SALIR:
				setResult(interfazPrincipal.RESULT_SALIR);
				SMSNuevosViejos.this.finish();
				break;
			case interfazPrincipal.RESULT_SMS_CORRECTO:
				setResult(interfazPrincipal.RESULT_OK);
				SMSNuevosViejos.this.finish();
				break;
			case interfazPrincipal.RESULT_NUEVOFICH:
				break;
			default:

			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch(id) {
		case DIALOG_SALIR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("¿Seguro que quieres salir de la aplicación?")
			.setCancelable(false)
			.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Comunicador.this.finish();
					setResult(interfazPrincipal.RESULT_SALIR);
					finish();
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
