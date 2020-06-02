package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;


public class Query3_2 {
	HashMap<String,String> map = new HashMap<String,String>();
	
	public Query3_2() {}
	
	static class MyMapper2 extends TableMapper<Text, Text> {

		@Override
		protected void map(ImmutableBytesWritable rowkey, Result value, Context context)
				throws IOException, InterruptedException {

			String key = Bytes.toString(rowkey.get()).substring(4);
			
        	String r = new String(value.getValue(Bytes.toBytes("cf"), Bytes.toBytes("reussite")));
        	String t = new String(value.getValue(Bytes.toBytes("cf"), Bytes.toBytes("total")));

			if(r != null && !r.equals("")) {
				context.write(new Text(key),
						new Text("reussite"+r));
			}
			if(t != null && !t.equals("")) {
				context.write(new Text(key),
						new Text("total"+t));
			}

		}
	}
	
	public HashMap<String,String> tauxParIdCours(String id_cours) throws IOException, ClassNotFoundException, InterruptedException{
		Query3 q3 = new Query3();
		q3.tauxParIdCours(id_cours);
		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);
		
		TableName tableResponse3 = TableName.valueOf("response3_3");
		HTableDescriptor response3 = new HTableDescriptor(tableResponse3);
		try {
			admin.disableTable(tableResponse3);
			admin.deleteTable(tableResponse3);
		} catch (Exception ignored) {
		}

		response3.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response3);
		
		Job job2 = Job.getInstance(config, "Taux");
		job2.setJarByClass(Query3_2.class);
		Scan scan2 = new Scan();

		TableMapReduceUtil.initTableMapperJob("response3_2", scan2, MyMapper2.class, Text.class, Text.class, job2);
		TableMapReduceUtil.initTableReducerJob("response3_3", Query3Reducer2.class, job2);

		boolean b2 = job2.waitForCompletion(true);
		if (!b2) {
			throw new IOException("error with job 2!");
		}
		
		HTable tableTaux = new HTable(config, "response3_3");
		Scan scanResponse = new Scan();
		ResultScanner resultScanner = tableTaux.getScanner(scanResponse);
		Result result = resultScanner.next();
		String id = "";
		String valeur = "";
		while (result != null) {
			List<Cell> cellsGrades = result.listCells();
			for (Cell cell : cellsGrades) {
				id = Bytes.toString(CellUtil.cloneRow(cell));
				valeur = Bytes.toString(CellUtil.cloneValue(cell));
			}
			map.put(id, valeur);
			result = resultScanner.next();
		}
		
		return map;
	}

}
