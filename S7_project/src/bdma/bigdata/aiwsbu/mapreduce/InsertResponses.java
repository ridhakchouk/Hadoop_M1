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
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import bdma.bigdata.aiwsbu.Namespace;

public class InsertResponses {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);

		TableName table2 = TableName.valueOf("response2");
		HTableDescriptor response2 = new HTableDescriptor(table2);
		try {
			admin.disableTable(table2);
			admin.deleteTable(table2);
		} catch (Exception ignored) {
		}

		response2.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response2);

		TableName table3 = TableName.valueOf("response3");
		HTableDescriptor response3 = new HTableDescriptor(table3);
		try {
			admin.disableTable(table3);
			admin.deleteTable(table3);
		} catch (Exception ignored) {
		}

		response3.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response3);
		

		TableName table4 = TableName.valueOf("response4");
		HTableDescriptor response4 = new HTableDescriptor(table4);
		try {
			admin.disableTable(table4);
			admin.deleteTable(table4);
		} catch (Exception ignored) {
		}

		response4.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response4);

		TableName table5 = TableName.valueOf("response5");
		HTableDescriptor response5 = new HTableDescriptor(table5);
		try {
			admin.disableTable(table5);
			admin.deleteTable(table5);
		} catch (Exception ignored) {
		}

		response5.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response5);

		TableName table6 = TableName.valueOf("response6Cours");
		HTableDescriptor response6 = new HTableDescriptor(table6);
		try {
			admin.disableTable(table6);
			admin.deleteTable(table6);
		} catch (Exception ignored) {
		}

		response6.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response6);

		TableName table7 = TableName.valueOf("response7");
		HTableDescriptor response7 = new HTableDescriptor(table7);
		try {
			admin.disableTable(table7);
			admin.deleteTable(table7);
		} catch (Exception ignored) {
		}

		response7.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response7);

		Job job2 = Job.getInstance(config, "TauxParAnnee");
		job2.setJarByClass(MyMapper2.class);
		Scan scan = new Scan();
		TableMapReduceUtil.initTableMapperJob("21407340t:G", scan, MyMapper2.class, Text.class, IntWritable.class,
				job2);
		TableMapReduceUtil.initTableReducerJob("response2", TauxReussiteReducer.class, job2);
		boolean b = job2.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job 2!");
		}
		

		Job job3 = Job.getInstance(config, "TauxParNom");
		job3.setJarByClass(MyMapper3.class);
		TableMapReduceUtil.initTableMapperJob("21407340t:G", scan, MyMapper3.class, Text.class, IntWritable.class,
				job3);
		TableMapReduceUtil.initTableReducerJob("response3", Query3Reducer.class, job3);
		b = job3.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job 3!");
		}
		

		Job job4 = Job.getInstance(config, "TauxParIdNom");
		job4.setJarByClass(MyMapper3.class);
		TableMapReduceUtil.initTableMapperJob("21407340t:G", scan, MyMapper3.class, Text.class, IntWritable.class,
				job4);
		TableMapReduceUtil.initTableReducerJob("response4", TauxReussiteReducer.class, job4);
		b = job4.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job 4!");
		}
		
		Job job5 = Job.getInstance(config, "MoyenneMatiereParProgramAnnee");
		job5.setJarByClass(MyMapper5.class);
		TableMapReduceUtil.initTableMapperJob("21407340t:G", scan, MyMapper5.class, Text.class, IntWritable.class,
				job5);
		TableMapReduceUtil.initTableReducerJob("response5", MoyenneReducer.class, job5);
		b = job5.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job 5!");
		}
		
		
		Job job7 = Job.getInstance(config, "MoyenneEtudiantParProgramAnnee");
		job7.setJarByClass(MyMapper3.class);
		TableMapReduceUtil.initTableMapperJob("21407340t:G", scan, MyMapper7.class, Text.class, IntWritable.class,
				job7);
		TableMapReduceUtil.initTableReducerJob("response7", MoyenneReducer.class, job7);
		b = job7.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job 7!");
		}
		
		HTable tableCourses = new HTable(config, "21407340t:C");
		HTable table6Cours = new HTable(config, "response6Cours");
		Scan scanCourses = new Scan();
		ResultScanner resultScanner = tableCourses.getScanner(scanCourses);
		Result result = resultScanner.next();
		String id_response = "";
		String valeur_response = "";
		
		while (result != null) {
			List<Cell> cellsResponses = result.listCells();
			for (Cell cell : cellsResponses) {
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if(Bytes.toString(CellUtil.cloneFamily(cell)).equals("#")
						&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("N")) {
					Put put = new Put(Bytes.toBytes(valeur_response));
					put.add(Bytes.toBytes("cf"), Bytes.toBytes("N"), Bytes.toBytes(id_response.substring(0, 7)));
					table6Cours.put(put);
				}

			}
			result = resultScanner.next();
		}

	}

}
