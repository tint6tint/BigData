package partOne;

public class Benchmark {
	
	public static void main(String[] args){
		StreamReader sr = new StreamReader("libs/generator.jar", 1000);
		sr.readStreams();
		SetGenerator sg = new SetGenerator(sr.in, "UTF-8");
		
		TestSuite ts = new TestSuite("libs/generator.jar");
		Long tests = ts.runningTime(1000000);
		Long tests2 = ts.runningTime(10000);
		Long tests3 = ts.runningTime(1000);
		
		System.out.println(tests);
		System.out.println(tests2);
		System.out.println(tests3);
		
	} 
}
