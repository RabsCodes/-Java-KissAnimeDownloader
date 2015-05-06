package net.kad.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextReader {
	File file;
	public TextReader(File file) {
		this.file = file;
	}

	public String[] read() {
		ArrayList<String> list = null;
		String[] text = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			list = new ArrayList<String>();
			String string;
			while ((string = reader.readLine()) != null)
				list.add(string);
			reader.close();
			text = new String[list.size()];
			list.toArray(text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return text;
	}
}
