package BD3.BD3;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main(String[] args)
    {
    	Set<String> setInstance = new HashSet<String>();

    	setInstance=readElements.readInputSet();
    	
    	System.out.println(setInstance);
    }
}
