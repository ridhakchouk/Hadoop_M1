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

public class MoyenneReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>{
	
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int somme = 0;
        int taille = 0;
        for (IntWritable value : values) {
            somme += value.get();
            taille ++;
        }
        double moyenne = somme / taille;
		Put put = new Put(Bytes.toBytes(key.toString()));
		put.add(Bytes.toBytes("cf"), Bytes.toBytes("moyenne"), Bytes.toBytes(String.valueOf(moyenne)));
        context.write(null, put);
    }    
   
}
