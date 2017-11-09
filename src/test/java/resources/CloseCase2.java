package resources;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CloseCase2 {
	public void main(String s){
		File file = new File(s);
		BufferedInputStream inputStream=null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			inputStream.read();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
