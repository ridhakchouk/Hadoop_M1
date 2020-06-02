package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
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
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Query4 {
	HashMap<String, String> cours = new HashMap<String, String>();
	
	public Query4() {}
	
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

	
	public HashMap<String, String> tauxIdAnnee(String id, String annee) throws IOException, ClassNotFoundException, InterruptedException{
		HashMap<String, String>map = new HashMap<String, String>();
		remplirMapCours(id);
		Configuration config = HBaseConfiguration.create();
		
		HTable responseTable = new HTable(config, "response4");

		Scan scanResponse = new Scan();
		ResultScanner resultScanner = responseTable.getScanner(scanResponse);
		Result result = resultScanner.next();
		String id_response;
		String valeur_response;
		int year = 0;
        try {
        	year = Integer.parseInt(annee);
        }catch(Exception e) {}

		while (result != null) {
			List<Cell> cellsGrades = result.listCells();
			for (Cell cell : cellsGrades) {
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if(id_response.equals(id+"/"+(9999-year))) {
					map.put(cours.get(id_response), valeur_response);
				}
			}
			result = resultScanner.next();
		}
	
		
		return map;
	}

}
