package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Query3 {
	HashMap<String, String> cours = new HashMap<String, String>();

	public Query3() {
	}

	public void remplirMapCours(String idCours) throws IOException {
		cours = new HashMap<String,String>();
		HashMap<String, String> cours2 = new HashMap<String, String>();

		Configuration config = HBaseConfiguration.create();
		HTable tableCourses = new HTable(config, "21407340t:C");
		Scan scanResponse = new Scan();
		ResultScanner resultScanner = tableCourses.getScanner(scanResponse);
		Result result = resultScanner.next();
		String id = "";
		String valeur = "";
		while (result != null) {
			List<Cell> cellsGrades = result.listCells();
			for (Cell cell : cellsGrades) {
				id = Bytes.toString(CellUtil.cloneRow(cell));
				if (id.contains(idCours)) {
					if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("#")
							&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("N")) {
						valeur = Bytes.toString(CellUtil.cloneValue(cell));
						cours2.put(id, valeur);
					}
				}
			}
			result = resultScanner.next();
		}
		cours.putAll(cours2);

		Set<Entry<String, String>> setHm = cours2.entrySet();
		Iterator<Entry<String, String>> it = setHm.iterator();
		String derniereCle = "";
		String derniereValeur = "";
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			int year = Integer.parseInt(e.getKey().substring(8));
			year--;
			while (year > 7970 && !cours2.containsKey(e.getKey().substring(0, 8) + year)) {
				cours.put(e.getKey().substring(0, 8) + year, e.getValue());
				year--;
			}
			derniereCle = e.getKey();
			derniereValeur = e.getValue();
		}
		int derniereAnnee = Integer.parseInt(derniereCle.substring(8));
		for(int i=derniereAnnee; i<7999;i++) {
			if(!cours.containsKey(derniereCle.substring(0,8)+i)) {
				cours.put(derniereCle.substring(0,8)+i, derniereValeur);
			}
		}
	}


	public void tauxParIdCours(String id_cours)
			throws IOException, ClassNotFoundException, InterruptedException {

		remplirMapCours(id_cours);
		HashMap<String, String> map = new HashMap<String, String>();
		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);

		TableName tableResponse3_2 = TableName.valueOf("response3_2");
		HTableDescriptor response2 = new HTableDescriptor(tableResponse3_2);
		try {
			admin.disableTable(tableResponse3_2);
			admin.deleteTable(tableResponse3_2);
		} catch (Exception ignored) {
		}

		response2.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(response2);

		HTable responseTable = new HTable(config, "response3");
		HTable response2Table = new HTable(config, "response3_2");
		Scan scanResponse = new Scan();
		ResultScanner resultScanner = responseTable.getScanner(scanResponse);
		Result result = resultScanner.next();
		String id_response;
		String valeur_response;
		int year = 0;
		String value = "";
		while (result != null) {
			List<Cell> cellsGrades = result.listCells();
			for (Cell cell : cellsGrades) {
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if (id_response.substring(0, 7).equals(id_cours)) {

					year = Integer.parseInt(id_response.substring(8));
					value = cours.get(id_response);
					
					Put put = new Put(Bytes.toBytes(id_response.substring(8) + value));
					if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("cf")
							&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("reussite")) {
						put.addImmutable(Bytes.toBytes("cf"), Bytes.toBytes("reussite"),
								Bytes.toBytes(valeur_response));
					} else if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("cf")
							&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("total")) {
						put.addImmutable(Bytes.toBytes("cf"), Bytes.toBytes("total"), Bytes.toBytes(valeur_response));

					}
					response2Table.put(put);
				}
			}
			result = resultScanner.next();
		}
	}
	

}
