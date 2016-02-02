package BD3.BD3;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import org.apache.commons.io.IOUtils;

public class readElements {
	public static long getTime(){
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0L;

		while (elapsedTime < 5*60*1000) {
		    //perform db poll/check
		    elapsedTime = (new Date()).getTime() - startTime;
		}
		return elapsedTime;
	}
	public static Set<String> readInputSet() {
		
		InputStream in = null;
		try {
			Process proc = Runtime.getRuntime().exec("java -jar generator.jar 1000");
			in = proc.getInputStream();
			InputStream err = proc.getErrorStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String theString = null;
		try {
			theString = IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		theString = theString.replace("_", "");
		String[] strArray = new String[]{theString};
		//System.out.println(strArray[0]);

		HashSet<String> set = new HashSet<String>();

	    for (String s : strArray) {
	        set.add(s);
	    }
	    //System.out.println(set);
	    return set;
	}
}
