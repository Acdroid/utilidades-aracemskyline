package tactic.movil.util.proxyUtil;

import tactic.movil.util.proxyUtil.proxyConnect;

/**
 *<b>configurationDataProxy.java 24/11/2010</<b><br><br>
 *    Sencilla clase para contener los datos necesarios de un
 * proxy, tales como usuario, contrasegna, direccion y puerto.<br><br>
 * 
 * @author Marcos Trujillo
 */
public class configurationDataProxy {
	private String dir;
	private String port;
	private String usu;
	private String pass;
	
	private static final String patronBusqueda = "a*GAB1-905wxYX+ov /çÇZbijktCcF27zOmqrñlMQRSTV!¡$8%&?sy64KLP)Nd¿Ñ(pghDEefJWn@#I3H";
	private static final String patronEncripta = "¿(ÑO@bc V$%&vGHQPXwxABNgEFrIJKL¡YZajklmnñop)dD89M#0!Whief-/*yz123457st?+çÇRSTq6C";


	/**
	 * <b>configurationDataProxy</b><br><br>
	 *   public configurartionDataProxy(String dir, String port, String usu, String pass)<br>
	 * <ul>Constructor de la clase configurationDataProxy.</ul><br><br>
	 * @param dir Direccion del proxy.
	 * @param port Puerto del proxy.
	 * @param usu Usuario del proxy. Si no es necesario usar null.
	 * @param pass Contrasegna del proxy. Si no es necesario usar null.
	 */
	public configurationDataProxy(String dir, String port, String usu, String pass)throws NullPointerException{
		
		if ( (dir == null) || (dir == "") )
			throw new NullPointerException("La direccion del proxy no puede ser null.");
		if ( (port == null) || (port == "") )
			throw new NullPointerException("El puerto del proxy no puede ser null.");
		
		this.dir = dir;
		this.port = port;
		
		this.usu = encripta(usu);
		this.pass = encripta(pass);
			
	}
	
	/**
	 * <b> encripta</b><br><br>
	 *    public String encripta(String a)<br>
	 * <ul>Encrypta el String pasado como argumento. Si 
	 * el string es null devuelve null.</ul><br><br>
	 * @param a String a encryptar
	 * @return texto encryptado o null e.o.c.
	 */
	public String encripta(String a){
		
		if ( (a == null) || (a == "") )
			return null;
		
		String resultado="";
		for (int i =0; i < a.length() ;i++){
			resultado += encriptaCar(a.charAt(i),a.length(),i);
		}
		
		
		
		return resultado;
	}
	
	/**
	 * <b> encriptaCar</b><br><br>
	 *    private String encriptaCar(char a,int tam, int i)<br>
	 * <ul>encripta UN caracter.</ul><br><br>
	 * @param a caracter a encriptar
	 * @param tam tamano del texto a encriptar
	 * @param i
	 * @return caracter encriptado.
	 */
	private char encriptaCar(char a,int tam, int i){

		int j;
		
		if (patronBusqueda.indexOf(a) != -1 ){
			j = (char) ( (patronBusqueda.indexOf(a) + tam + i) % patronBusqueda.length() ) ;
			return patronEncripta.charAt(j);
		}
		
		return a;
	}
	
	
	public String desencripta(String a){
		
		if ( (a == null) || (a == "") )
			return null;
		
		String result="";
		for (int i =0; i < a.length() ;i++){
			result += desencriptaCar(a.charAt(i),a.length(),i);
		}
		
		return result;
	}
	
	/**
	 * <b> encriptaCar</b><br><br>
	 *    private String encriptaCar(char a,int tam, int i)<br>
	 * <ul>encripta UN caracter.</ul><br><br>
	 * @param a caracter a encriptar
	 * @param tam tamano del texto a encriptar
	 * @param i
	 * @return caracter encriptado.
	 */
	private char desencriptaCar(char a,int tam, int i){

		int j;
		
		if (patronEncripta.indexOf(a) != -1 ){
			if ( (patronEncripta.indexOf(a) - tam - i) > 0 )
				j = ( patronEncripta.indexOf(a) - tam - i )  % patronEncripta.length() ;
			else
				j = patronBusqueda.length() + ( ( patronEncripta.indexOf(a) - tam - i )  % patronEncripta.length() );
			
			j = j % patronEncripta.length();
			return patronBusqueda.charAt(j);
		}
		
		return a;
	}
	
	
	/**
	 * <b> conectProxy</b><br><br>
	 *    public void conectProxy()<br>
	 * <ul>Se conecta al proxy con la configuracion del propio objeto configurationDataProxy</ul><br><br>
	 */
	public void conectProxy(){
		proxyConnect.setProxyConfig(this);
	}
	
	
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the usu
	 */
	public String getUsu() {
		return usu;
	}

	/**
	 * @param usu the usu to set
	 */
	public void setUsu(String usu) {
		this.usu = usu;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
}
