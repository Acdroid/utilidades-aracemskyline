package tactic.movil.util.proxyUtil;

/**
 * proxyConnect.java  05/11/2010<br>
 * 
 * @author David Toribio Gomez
 * @author Marcos Trujillo Seoane
 *
 */

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class proxyConnect{

	private static String username, password;

	public static void setProxyConfig(String host, String port, String username, String password) {

		if(host == null || port == null)
			return;

		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", port);

		// Si no es un proxy anonimo
		if(username!=null && password!=null){
			proxyConnect.username = username;
			proxyConnect.password = password;
			Authenticator.setDefault(
					new Authenticator() {
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(proxyConnect.username,proxyConnect.password.toCharArray());
						}
					});
		}

	}			

	public static void setProxyConfig(configurationDataProxy c) {

		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", c.getDir());
		System.setProperty("http.proxyPort", c.getPort());

		// Si no es un proxy anonimo
		if(c.getUsu()!=null && c.getPass()!=null){
			proxyConnect.username = c.desencripta(c.getUsu());
			proxyConnect.password = c.desencripta(c.getPass());
			Authenticator.setDefault(
					new Authenticator() {
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(proxyConnect.username,proxyConnect.password.toCharArray());
						}
					});
		}

	}			
}
