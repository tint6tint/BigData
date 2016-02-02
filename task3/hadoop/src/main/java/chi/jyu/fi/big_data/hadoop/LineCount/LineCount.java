package chi.jyu.fi.big_data.hadoop.LineCount;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
public class LineCount {
	// CLASS Reporter: A facility for Map-Reduce applications to report progress and update counters, status information etc.
	// CLASS Counters: Counters represent global counters, defined either by the Map-Reduce framework or applications. 
	// Each Counter can be of any Enum type.
	// CLASS OutputCollector: Is the generalization of the facility provided by the Map-Reduce framework to collect data output 
	// by either the Mapper or the Reducer i.e. intermediate outputs or the output of the job.

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text("Total Lines");
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			//  Adds a key/value pair to the output.
			output.collect(word, one);
		}
	}
	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
	}
	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(LineCount.class);
		conf.setJobName("LineCount");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		JobClient.runJob(conf);
	}
}