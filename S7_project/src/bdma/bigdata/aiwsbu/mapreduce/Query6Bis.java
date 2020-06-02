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
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class Query6Bis {


	public Query6Bis() {
	}

	public static HashMap<String, HashMap<String, String>> tauxProf(String nom_prof)
			throws IOException, ClassNotFoundException, InterruptedException {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();

		Configuration config = HBaseConfiguration.create();
		HTable responseTable = new HTable(config, "response4");
		HTable tableCourses = new HTable(config, "response6Cours");
		HTable tableI = new HTable(config, "21407340t:I");

		Scan scanI = new Scan();
		ResultScanner resultScannerI = tableI.getScanner(scanI);
		Result resultI = resultScannerI.next();
		String id_response = "";
		String valeur_response = "";

		while (resultI != null) {
			List<Cell> cellsResponses = resultI.listCells();
			for (Cell cell : cellsResponses) {
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if (id_response.contains(nom_prof)) {
					list.add(id_response.substring(id_response.length() - 4) + "/" + valeur_response);
				}
			}
			resultI = resultScannerI.next();
		}

		for (int i = 0; i < list.size(); i++) {
			Get get = new Get(Bytes.toBytes(list.get(i).substring(5)));
			Result resultStudent = tableCourses.get(get);
			if (resultStudent.size() > 0) {
				List<Cell> cells = resultStudent.listCells();
				for (Cell cell : cells) {
					list2.add(Bytes.toString(CellUtil.cloneValue(cell))+"/"+list.get(i));
				}
			}
		}
		
		
		for (int i = 0; i < list2.size(); i++) {
			Get get = new Get(Bytes.toBytes(list2.get(i).substring(0,8)+(9999-Integer.parseInt(list2.get(i).substring(8, 12)))));
			Result result = responseTable.get(get);
			if (result.size() > 0) {
				List<Cell> cells = result.listCells();
				for (Cell cell : cells) {
					map2.put(list2.get(i).substring(13), Bytes.toString(CellUtil.cloneValue(cell)));
					map.put(list2.get(i).substring(0, 12), map2);
					map2 = new HashMap<String,String>();
				}
			}
		}

		return map;
	}
	

}
