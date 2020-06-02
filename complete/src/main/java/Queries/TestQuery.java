package Queries;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

public class TestQuery {
	
	public TestQuery() {}
	
	static class MyMapper extends TableMapper<Text, IntWritable>{
        private IntWritable ONE = new IntWritable(1);

        public MyMapper() {}

        @Override
        protected void map( ImmutableBytesWritable rowkey, Result value, Context context ) throws IOException, InterruptedException{
            // Get the timestamp from the row key
            String timestamp = Bytes.toString(rowkey.get());
            context.write( new Text(timestamp ), ONE );
        }
    }

    static class MyReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>{
    	
        public MyReducer() {}

        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
            // Add up all of the page views for this hour
            long sum = 0;
            for(IntWritable count : values){
                sum += count.get();
            }
    		Put put = new Put(Bytes.toBytes(key.toString()));
    		put.add(Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(sum));
            context.write(null, put );
        }
    }

    public static void main(String[] args) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);
		TableName tableName = TableName.valueOf("test");
		HTableDescriptor response = new HTableDescriptor(tableName);
        try {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (Exception ignored) {}
        
        response.addFamily(new HColumnDescriptor("cf"));
        admin.createTable(response);
        
        try{
            Configuration conf = HBaseConfiguration.create();
            Job job = Job.getInstance(conf, "PageViewCounts");
            job.setJarByClass(TestQuery.class);
            Scan scan = new Scan();

            TableMapReduceUtil.initTableMapperJob( "21407340t:S", scan, MyMapper.class, Text.class, IntWritable.class, job);
            TableMapReduceUtil.initTableReducerJob("test",MyReducer.class,job);
            
            boolean b = job.waitForCompletion(true);
            if (!b) {
                throw new IOException("error with job!");
            }

        }
        catch( Exception e ){
            e.printStackTrace();
        }
    }
}
