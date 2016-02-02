package chi.jyu.fi.big_data.hadoop.UniqueItems;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DistinctReducer extends Reducer <Text, IntWritable, Text, IntWritable> {
	private IntWritable count = new IntWritable();
	
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context)
	throws IOException, InterruptedException
	{
		int total = 0;
		for(IntWritable value : values){
			total+= value.get();
		}
		count.set(total);
		context.write(key, count);
	}
}
