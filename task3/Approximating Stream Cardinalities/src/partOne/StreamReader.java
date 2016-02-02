package partOne;

import java.io.IOException;
import java.io.InputStream;

public class  StreamReader{
	InputStream in;
	InputStream err;
	String jarPath;
	int generations;
	
	public StreamReader(String jarPath, int generations){
		this.jarPath = jarPath;
		this.generations = generations;
	}
	public StreamReader(String jarPath){
		this.jarPath = jarPath;
		this.generations = 1000; // Defualt
	}
	
	public void newCycle(int generations){
		try {
			getJarInput(generations);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readStreams(){
		try {
			getJarInput(generations);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private  void getJarInput(int generations) throws IOException {
		String cmd = "java -jar "+jarPath+" "+generations;
		Process proc = Runtime.getRuntime().exec(cmd);
		in = proc.getInputStream();
		err = proc.getErrorStream();
		
	}
}
