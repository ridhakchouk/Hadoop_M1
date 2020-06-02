package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class Query3Reducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>{

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    	
        int nombreReussite = 0;
        int taille = 0;
        for (IntWritable value : values) {
            if(value.get() >= 1000) {
            	nombreReussite++;
            }
            taille ++;
        }
		Put put = new Put(Bytes.toBytes(key.toString()));
		put.add(Bytes.toBytes("cf"), Bytes.toBytes("reussite"), Bytes.toBytes(String.valueOf(nombreReussite)));
		put.add(Bytes.toBytes("cf"), Bytes.toBytes("total"), Bytes.toBytes(String.valueOf(taille)));
        context.write(null, put);
        
    }


}
