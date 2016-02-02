package chi.jyu.fi.big_data.hadoop.HyperLogLog;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public class Reader {
	private Path filep;
	private byte[] byteArray;
	private BufferedReader br;
	
	public Reader(String filep) throws IOException{
		this.filep = Paths.get(filep);
		br = Files.newBufferedReader(this.filep);
		
	}
	byte[] readLine() throws IOException{
		String line = br.readLine();
		if(line != null) {
			byteArray = line.getBytes();
			return byteArray;
		}
		else 
			return null;
	}
	byte[] readContents() throws IOException{
		this.byteArray = IOUtils.toByteArray(Files.newInputStream(filep));
		return byteArray;
	}
	public DataInputStream converToDataInputStream(){
		return new DataInputStream(new ByteArrayInputStream(byteArray));
	}

	public static void main(String[] args){
		Reader r = null;
		DataInput di = null;
		int t = 0;
			try {
				r = new Reader("data1million");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				r.readContents();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			di = r.converToDataInputStream();
			try {
				t  = di.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
