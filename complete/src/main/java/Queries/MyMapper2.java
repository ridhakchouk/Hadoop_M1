package Queries;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class MyMapper2 extends TableMapper<Text, IntWritable>{

    @Override
    protected void map(ImmutableBytesWritable rowkey, Result value, Context context) throws IOException, InterruptedException{
    	
    	String key = Bytes.toString(rowkey.get());
    	String val = new String(value.getValue(Bytes.toBytes("#"), Bytes.toBytes("G")));
    	int valeur = Integer.parseInt(val);
        context.write(new Text(key.substring(5,7)+key.substring(0,4)), new IntWritable(valeur));
    }
}
