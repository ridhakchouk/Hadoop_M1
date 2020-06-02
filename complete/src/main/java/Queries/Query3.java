package Queries;

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

	public Query3() {
	}

	public HashMap<String, Double> tauxParIdCours(String id_cours)
			throws IOException, ClassNotFoundException, InterruptedException {
		HashMap<String, Double> mapFinal = new HashMap<String, Double>();
		HashMap<String, Integer[]> map1 = new HashMap<String, Integer[]>();

		Configuration config = HBaseConfiguration.create();
		HTable responseTable = new HTable(config, "response3_2");

		Scan scan = new Scan();
		ResultScanner resultScanner = responseTable.getScanner(scan);
		Result result = resultScanner.next();
		String id_response = "";
		String valeur_response = "";

		while (result != null) {
			List<Cell> cellsResponses = result.listCells();
			for (Cell cell : cellsResponses) {
				Integer[] tab = {0,0};
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if (id_response.substring(5, 12).equals(id_cours)) {
					if (map1.containsKey(id_response.substring(13))) {
						if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("cf")
								&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("reussite")) {
							int r = map1.get(id_response.substring(13))[0] + Integer.parseInt(valeur_response);
							int t = map1.get(id_response.substring(13))[1];
							tab[0] = r;
							tab[1] = t;
							map1.put(id_response.substring(13), tab);
						} else if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("cf")
								&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("total")) {
							int r = map1.get(id_response.substring(13))[0];
							int t = map1.get(id_response.substring(13))[1] + Integer.parseInt(valeur_response);
							tab[0] = r;
							tab[1] = t;
							map1.put(id_response.substring(13), tab);
						}

					} else {
						if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("cf")
								&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("reussite")) {
							tab[0] = Integer.parseInt(valeur_response);
						} else if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("cf")
								&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("total")) {
							tab[1] = Integer.parseInt(valeur_response);
						}
						map1.put(id_response.substring(13), tab);
					}
				}
			}
			result = resultScanner.next();
		}
		
		Set<Entry<String, Integer[]>> setHm = map1.entrySet();
		Iterator<Entry<String, Integer[]>> it = setHm.iterator();
		while (it.hasNext()) {
			Entry<String, Integer[]> e = it.next();
			mapFinal.put(e.getKey(), (double) e.getValue()[0] / e.getValue()[1]);
		}
		
		return mapFinal;
	}

}
