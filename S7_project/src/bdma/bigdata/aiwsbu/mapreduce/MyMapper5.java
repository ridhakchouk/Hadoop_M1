package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class MyMapper5 extends TableMapper<Text, IntWritable> {

	@Override
	protected void map(ImmutableBytesWritable rowkey, Result value, Context context)
			throws IOException, InterruptedException {

		String key = Bytes.toString(rowkey.get());
		String val = new String(value.getValue(Bytes.toBytes("#"), Bytes.toBytes("G")));
		int valeur = Integer.parseInt(val);
		String semestre = key.substring(5,7);
		String program = "";
		if(semestre.equals("01") || semestre.equals("02")) {
			program = "L1";
		}
		else if(semestre.equals("03") || semestre.equals("04")) {
			program = "L2";
		}
		else if(semestre.equals("05") || semestre.equals("06")) {
			program = "L3";
		}
		else if(semestre.equals("07") || semestre.equals("08")) {
			program = "M1";
		}
		else if(semestre.equals("09") || semestre.equals("10")) {
			program = "M2";
		}
		context.write(new Text(key.substring(18) + "/" + (9999 - Integer.parseInt(key.substring(0, 4)))+program),
				new IntWritable(valeur));
	}
}
