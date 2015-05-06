package net.kad.http;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Downloader {
	private URL url;
	private HttpURLConnection connection;
	private DataInputStream inputStream;

	public class BadStatusCodeException extends Exception {
		private static final long serialVersionUID = -1892503506335657263L;
		private int statusCode;
		private String responseString;
		public BadStatusCodeException(int statusCode, String responseString) {
			this.statusCode = statusCode;
			this.responseString = responseString;
		}
		public int getStatusCode() {
			return statusCode;
		}
		public String getResponseString() {
			return responseString;
		}
	}

	public Downloader(String urlString){
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void download(File file) throws BadStatusCodeException {
		connect();
		byte[] buffer = new byte[4096];
		int readByte = 0;
		try {
			DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			while ((readByte = inputStream.read(buffer)) != -1)
				outputStream.write(buffer, 0, readByte);
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void connect() throws BadStatusCodeException {
		try {
			connection = (HttpURLConnection) url.openConnection();

			//connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			//connection.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
			//connection.setRequestProperty("Referer", "http://kissanime.com/Search/Anime");
			//connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			//connection.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");

			connection.setAllowUserInteraction(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST");
			//connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			int statusCode = connection.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK)
				throw new BadStatusCodeException(statusCode, connection.getResponseMessage());
			inputStream = new DataInputStream(connection.getInputStream());
		} catch (ProtocolException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}


}
