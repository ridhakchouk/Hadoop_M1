package Queries;

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
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class Query5 {
	HashMap<String, String> cours = new HashMap<String, String>();

	public Query5() {
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
	
	
	public HashMap<String, HashMap<String, String>> moyenneProgramAnnee(String program, String annee)
			throws IOException, ClassNotFoundException, InterruptedException {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		HashMap<String,String> map2 = new HashMap<String,String>();
		boolean possible = true;
		String semestre1 = "";
		String semestre2 = "";
		
		if (program.equals("L1")) {
			semestre1 = "01";
			semestre2 = "02";
		} else if (program.equals("L2")) {
			semestre1 = "03";
			semestre2 = "04";
		} else if (program.equals("L3")) {
			semestre1 = "05";
			semestre2 = "06";
		} else if (program.equals("M1")) {
			semestre1 = "07";
			semestre2 = "08";
		} else if (program.equals("M2")) {
			semestre1 = "09";
			semestre2 = "10";
		} else
			possible = false;

		int year = 0;

		try {
			year = 9999 - Integer.parseInt(annee);
		} catch (Exception e) {
			possible = false;
		}

		if (possible) {
			Configuration config = HBaseConfiguration.create();
			HTable responseTable = new HTable(config, "response5");
			HTable tableCourses = new HTable(config, "21407340t:C");
			Scan scanResponse = new Scan();
			ResultScanner resultScanner = responseTable.getScanner(scanResponse);
			Result result = resultScanner.next();
			String id_response = "";
			String valeur_response = "";
			String nom = "";
			
			while (result != null) {
				List<Cell> cellsGrades = result.listCells();
				for (Cell cell : cellsGrades) {
					id_response = Bytes.toString(CellUtil.cloneRow(cell));
					valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
					if (id_response.substring(8,12).equals(year+"") && id_response.substring(12).equals(program) ) {
						remplirMapCours(id_response.substring(0, 7));
						nom = cours.get(id_response.substring(0,7)+"/"+year);
						map2.put(nom, valeur_response);
						map.put(id_response.substring(0,7), map2);
					}
				}
				map2 = new HashMap<String,String>();
				result = resultScanner.next();
			}
		}
		return map;
	}

}
