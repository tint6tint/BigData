package chi.jyu.fi.big_data.hadoop.UniqueItems;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DistinctCounterJob implements Tool{
	private Configuration conf;
	public static final String NAME="distinct_counter";
	public static final String COL_POS="col_pos";

	  public static void main(String[] args) throws Exception {
		  ToolRunner.run(new Configuration(), new DistinctCounterJob(), args);
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public int run(String[] args) throws Exception {
		if(args.length != 3){
			System.err.println("distinct_counter <input> <output> <element_pos>");
			System.exit(1);
		}
		Job job = Job.getInstance(conf, "Count distinct elements at a pos");
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(DistinctMapper.class);
		job.setCombinerClass(DistinctReducer.class);
		job.setReducerClass(DistinctReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setJarByClass(DistinctCounterJob.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		return job.waitForCompletion(true) ? 1 : 0;
	}
}

