package partOne;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class SetGenerator {
	HashSet<String> hs;
	Scanner s;
	
	public SetGenerator(InputStream in, String encoding){
		String str = convertStreamToString(in, encoding);
		createSets(str);
		s.close();
	}
	public SetGenerator(InputStream in){
		String str = convertStreamToString(in, "UTF-8");
		createSets(str);
		s.close();
	}
	
	private void createSets(String str) {
		str.replace("_", "");
		String[] strs = str.split("\n");
		hs = new HashSet<String>(Arrays.asList(strs));
	}

	@SuppressWarnings("resource")
	private String convertStreamToString(InputStream in, String encoding) {
	    s = new Scanner(in, encoding).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}

