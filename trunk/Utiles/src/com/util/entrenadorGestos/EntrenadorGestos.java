/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tactic.movil.entrenadorGestos;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import tactic.movil.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EntrenadorGestos extends Activity {
	private static final float LENGTH_THRESHOLD = 120.0f;

	private Gesture mGesture;
	private View mDoneButton;

	public String letraEncontrada = "";
	public Context mContext;
	public Gesture gestureActual=null;
	public TextView texto;

	private static final int LETRA_NUEVA = 1;
	private static final int OK_ERROR= 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_gesture);

		mDoneButton = findViewById(R.id.done);
		mContext = this;


		GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		overlay.addOnGestureListener(new GesturesProcessor());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mGesture != null) {
			outState.putParcelable("gesture", mGesture);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		mGesture = savedInstanceState.getParcelable("gesture");
		if (mGesture != null) {
			final GestureOverlayView overlay =
				(GestureOverlayView) findViewById(R.id.gestures_overlay);
			overlay.post(new Runnable() {
				public void run() {
					overlay.setGesture(mGesture);
				}
			});

			Log.d("Entrenador","onRestoreInstanceEstate");

			mDoneButton.setEnabled(true);
		}
	}

	public void addGesture(View v) {
		if (mGesture != null) {
			final TextView input = (TextView) findViewById(R.id.gesture_name);
			final CharSequence name = input.getText();
			if (name.length() == 0) {
				input.setError(getString(R.string.error_missing_name));
				return;
			}

			final GestureLibrary store = GestureBuilderActivity.getStore();
			store.addGesture(name.toString(), mGesture);
			store.save();

			final String path = new File(Environment.getExternalStorageDirectory(),
			"gestures").getAbsolutePath();
			Toast.makeText(this, getString(R.string.save_success, path), Toast.LENGTH_LONG).show();
		}

	}

	public void cancelGesture(View v) {
		setResult(RESULT_OK);
		finish();


	}

	public void doneGesture(View v) {
		ArrayList<Prediction> predictions = GestureBuilderActivity.getStore().recognize(mGesture);
		if (predictions.size() > 0) {
			if (predictions.get(0).score > 1.0) {
				String action = predictions.get(0).name;
				Double score = predictions.get(0).score;
				Toast.makeText(mContext, "Letra: " + action + " Score: " + score, Toast.LENGTH_SHORT).show();
				setLetraEncontrada(action.substring(0,1));
				gestureActual = mGesture;
				showDialog(OK_ERROR);

				//DEBUG
				Iterator<Prediction> i = predictions.iterator();
				int j=1;
				Log.d("GESTOS " + j , "Nuevo gesto!!" );
				while (i.hasNext()){
					Prediction p = i.next();
					Log.d("GESTOS " + j , p.name + " " + p.score );
					j++;
				}
				//Fin Debug

			}
			else{
				gestureActual = mGesture;
				showDialog(LETRA_NUEVA);
			}
		}
		else{
			gestureActual = mGesture;
			showDialog(LETRA_NUEVA);
		}



	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch(id) {
		case OK_ERROR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("¿Es la letra " + letraEncontrada + " ?")
			.setCancelable(false)
			.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					addGesture(null);
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		case LETRA_NUEVA:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			texto = (TextView) findViewById(R.id.gesture_name);
			builder2.setMessage("¿Añadir la letra " + texto.getText().toString().substring(0, 1) + "?")
			.setCancelable(false)
			.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					addGesture(null);
					dialog.cancel();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert2 = builder2.create();
			return alert2;
		default:
			dialog = null;
		}

		return dialog;
	}


	private class GesturesProcessor implements GestureOverlayView.OnGestureListener {
		public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
			mDoneButton.setEnabled(false);
			mGesture = null;
		}

		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		}

		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			mGesture = overlay.getGesture();
			if (mGesture.getLength() < LENGTH_THRESHOLD) {
				overlay.clear(false);
			}
			mDoneButton.setEnabled(true);

		}

		public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		}
	}


	public String getLetraEncontrada() {
		return letraEncontrada;
	}

	public void setLetraEncontrada(String letraEncontrada) {
		this.letraEncontrada = letraEncontrada;
	}
}
