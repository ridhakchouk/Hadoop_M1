package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import bdma.bigdata.aiwsbu.mapreduce.ValueComparator;

public class Query7 {
	
	public Query7() {}
	
	public TreeMap<String,Double> moyenneEtudiant(String program, String annee)
			throws IOException, ClassNotFoundException, InterruptedException {
		HashMap<String,Double> map = new HashMap<String,Double>();
		
		Configuration config = HBaseConfiguration.create();
		HTable responseTable = new HTable(config, "response7");
		Scan scan = new Scan();
		ResultScanner resultScanner = responseTable.getScanner(scan);
		Result result = resultScanner.next();
		String id_response = "";
		String valeur_response = "";
		
		while (result != null) {
			List<Cell> cells = result.listCells();
			for (Cell cell : cells) {
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if(id_response.substring(11,15).equals(annee) && id_response.substring(15).equals(program)) {
					map.put(id_response.substring(0, 10), Double.parseDouble(valeur_response));
				}
			}
			result = resultScanner.next();
		}
		
		ValueComparator comparateur = new ValueComparator(map);
		TreeMap<String,Double> mapTrie = new TreeMap<String,Double>(comparateur);
		mapTrie.putAll(map);
		return mapTrie;
	}

}
