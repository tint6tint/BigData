package partOne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class SetBenchmark {
	

	public class configs {
		public static final String filepath = "file.txt";
	}
	
	public static void main(String[] args){
		System.out.println(args[0]);
		String input ="";
		if(args.length > 0) input = readargs(args);
		else {
			System.out.println("system.in");
			input = readsystemin();
		}
		writeOutput(input);
	}

	private static String readargs(String[] args) {
		String ret = "";
		for(String a : args){
			ret += " "+a;
		}
		return ret;
	}

	private static void writeOutput(String output) {
		try {
			Path p = Paths.get(configs.filepath);
			if(!Files.exists(p)) Files.createFile(p);
			BufferedWriter bw = Files.newBufferedWriter(p, Charset.forName("UTF-8"), StandardOpenOption.WRITE);
			bw.write(output);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private static String readsystemin() {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input;
			while((input=br.readLine())!=null){
				System.out.println(input);
			}
		return input;
		}catch(IOException io){
			io.printStackTrace();
		}
		return null;
	}
}
