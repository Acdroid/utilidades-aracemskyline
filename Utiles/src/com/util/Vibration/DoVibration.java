/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @Tactic Movil Todos los derechos reservados 
 */
package tactic.movil.util.Vibration;

import android.os.Vibrator;

/**
 * DoVibration.java 03/03/2011
 * @author mtrujillo
 */
public class DoVibration {


	/**
	 * <b>OK</b><br><br>
	 *   public static void OK()<br>
	 * <ul>Genera una vibracion en el movil durante 150 milisegundos
	 * para indicar operacion OK.
	 * <b>USO:<br>   DoVibration.OK((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));</ul><br><br>
	 * @param Elemento Vibrator (new Vibrator)
	 */
	public static void  OK(Vibrator vibrator){
		vibrator.vibrate(150); //Vibra para avisar
	}
	/**
	 * <b>ERROR</b><br><br>
	 *   public static void ERROR()<br>
	 * <ul>Genera una vibracion en el movil durante 75 milisegundos
	 * y otra de 225 milisegundos para indicar una situacion de error.
	 * <b>USO:<br>   DoVibration.ERROR((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));</ul><br><br>
	 * @param Elemento Vibrator (new Vibrator)
	 */
	public static void ERROR(Vibrator vibrator){
		long aux[] = {0,75,250,225};
		vibrator.vibrate(aux, -1); //Vibra para avisar
	}
	
	
	/**
	 * <b>CustomSimple</b><br><br>
	 *   public static void CustomSimple()<br>
	 * <ul>Genera una vibracion en el movil de una duracion introducida
	 * en el argumento miliseg. (En milisegundos)
	 * <b>USO:<br>   DoVibration.CustomSimple((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), milisegundos);</b></ul><br><br>
	 * @param Elemento Vibrator (new Vibrator)<br>
	 * @param Milisegundos que queremos que dure la vibracion.
	 */
	public static void CustomSimple(Vibrator vibrator, long miliseg ){
		vibrator.vibrate(miliseg); //Vibra para avisar
	}
	
	/**
	 * <b>CustomSimple</b><br><br>
	 *   public static void CustomSimple()<br>
	 * <ul>Genera una vibracion en el movil de una duracion introducida
	 * en el argumento miliseg. (En milisegundos)
	 * <b>USO:<br>   DoVibration.CustomSimple((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), milisegundos[]);</b></ul><br><br>
	 * @param Elemento Vibrator (new Vibrator)<br>
	 * @param long[] con el array el tiempo en milisegundos queremos hacer vibrar el movil.<br>
	 * <b> Este array debe estar compuesto por:</b><br>
	 *      * Tiempo hasta primera vibracion.<br>
	 *      * Tiempo de la primera vibracion.<br>
	 *      * Tiempo entre vibraciones.<br>
	 *      * Tiempo de la segunda vibracion.<br>
	 *      * Tiempo entre vibraciones.<br>
	 *      * ... sucesivamente 
	 */
	public static void CustomRepeat(Vibrator vibrator, long[] miliseg ){
		vibrator.vibrate(miliseg,-1); //Vibra para avisar
	}
	
	
	/**
	 * <b>OK</b><br><br>
	 *   public static void OK()<br>
	 * <ul>Genera una vibracion en el movil durante 150 milisegundos
	 * para indicar operacion OK.
	 * <b>USO:<br>   DoVibration.OK((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));</ul><br><br>
	 * @param Elemento Vibrator (new Vibrator)
	 */
	public static void  SMS(Vibrator vibrator){
		long aux[] = {0,200,100,200,100,200,90,500,300,450,200,150,85,150,85,150};
		vibrator.vibrate(aux, -1); //Vibra para avisar
	}
	
}
