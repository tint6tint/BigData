package partOne;

import java.util.ArrayList;

public class Timer {
	ArrayList<Long> stops = new ArrayList<Long>();
	// Long millis = System.currentTimeMillis() % 1000;
	public Timer(){
		stops.add(getCurrentMillis());
	}
	void stop(){
		stops.add(getCurrentMillis());
	}
	Long deltaTime(){
		Long first = stops.get(stops.size()-2);
		Long second = stops.get(stops.size()-1);
		return second-first;
	}
	Long getCurrentMillis(){
		Long millis = System.currentTimeMillis();
		return millis;
	}
}
