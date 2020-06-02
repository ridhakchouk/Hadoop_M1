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

public class Query3Reducer2 extends TableReducer<Text, Text, ImmutableBytesWritable>{

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    	
        int nombreReussite = 0;
        int total = 0;
        String s;
        String v = "";
        String r = "";
        String t = "";
        for (Text value : values) {
        	v  = value+"";
        	
        	if(v.contains("reussite")) {
        		s = v.substring(8);
        		nombreReussite = nombreReussite + Integer.parseInt(s);
        	}
        	else if(v.contains("total")) {
        		s = v.substring(5);
        		total = total + Integer.parseInt(s);        		
        	}
        	
        }
		Put put = new Put(Bytes.toBytes(key.toString()));
		double taux = (double) nombreReussite / total;
		put.add(Bytes.toBytes("cf"), Bytes.toBytes("taux"), Bytes.toBytes(String.valueOf(taux)));
        context.write(null, put);
        
    }


}
