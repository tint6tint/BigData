package partOne;

import java.util.HashSet;

public class TestSuite {
	String generatorPath ="";
	StreamReader sr;
	StreamReader sr2;
	SetGenerator sg;
	Timer tr;
	
	public TestSuite(String generatorPath){
		this.generatorPath = generatorPath;	
		this.tr = new Timer();
	}
	
	Long runningTime(int gen){
		tr.stop();
		sr = new StreamReader(generatorPath, gen);
		sr.readStreams();
		HashSet<String> hs = new SetGenerator(sr.in).hs;
		tr.stop();
		return tr.deltaTime();

	}
	Long differenceInRunningTime(int gen1, int gen2){
		sr = new StreamReader(generatorPath, gen1);
		sr2 = new StreamReader(generatorPath, gen2);
		sr.readStreams();
		sr2.readStreams();
		tr.stop();
		HashSet<String> firstSet = new SetGenerator(sr.in).hs;
		tr.stop();
		Long gen1Units = tr.deltaTime();
		tr.stop();
		HashSet<String> secondSet = new SetGenerator(sr2.in).hs;
		tr.stop();
		Long gen2Units = tr.deltaTime();
		if(gen2Units > gen1Units) return gen2Units-gen1Units;
		else return gen1Units-gen2Units; 
	}
}
