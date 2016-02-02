package chi.jyu.fi.big_data.hadoop.UniqueItems;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DistinctMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable outValue = new IntWritable(1);
		private Text outKey = new Text();
		private static int col_pos;
		private static Pattern pattern = Pattern.compile("\\t");
		@Override
		protected void setup(Context context) throws IOException, InterruptedException{
			context.getConfiguration().getInt(DistinctCounterJob.COL_POS, 0);
		}
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String field = pattern.split(value.toString())[col_pos];
			outKey.set(field);
			context.write(outKey, outValue);
		}
}	
