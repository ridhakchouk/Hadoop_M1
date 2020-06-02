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
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class Query6 {
	
	HashMap<String, String> cours = new HashMap<String, String>();
	ArrayList<String> list_matieres = new ArrayList<String>();


	public Query6() {
	}
	
	public boolean enseigneMatiere(String matiere) {
		for(int i=0; i<list_matieres.size();i++) {
			if(list_matieres.get(i).equals(matiere)) {
				return true;
			}
		}
		return false;
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

	public HashMap<String, HashMap<String, String>> tauxProf(String nom_prof)
			throws IOException, ClassNotFoundException, InterruptedException {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		HashMap<String,String> map2 = new HashMap<String,String>();
		
		Configuration config = HBaseConfiguration.create();
		HTable responseTable = new HTable(config, "response4");
		HTable tableCourses = new HTable(config, "21407340t:C");
		Scan scanCourses = new Scan();
		ResultScanner resultScanner = tableCourses.getScanner(scanCourses);
		Result result = resultScanner.next();
		String id_course = "";
		String valeur_course = "";
		
		while (result != null) {
			List<Cell> cellsCourses = result.listCells();
			for (Cell cell : cellsCourses) {
				id_course = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_course = Bytes.toString(CellUtil.cloneValue(cell));
				if(valeur_course.equals(nom_prof)) {
					list_matieres.add(id_course);
				}
			}
			result = resultScanner.next();
		}
				
		Scan scanResponses = new Scan();
		ResultScanner resultScannerResponses = responseTable.getScanner(scanResponses);
		Result resultResponse = resultScannerResponses.next();
		String id_response = "";
		String valeur_response = "";
		String nom = "";
		
		while (resultResponse != null) {
			List<Cell> cellsResponses = resultResponse.listCells();
			for (Cell cell : cellsResponses) {
				id_response = Bytes.toString(CellUtil.cloneRow(cell));
				valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
				if(enseigneMatiere(id_response)) {
					remplirMapCours(id_response.substring(0, 7));
					nom = cours.get(id_response);
					map2.put(nom, valeur_response);
					map.put(id_response.substring(0,8)+(9999 - Integer.parseInt(id_response.substring(8))), map2);
				}
				map2 = new HashMap<String,String>();
			}
			resultResponse = resultScannerResponses.next();
		}
		
		return map;
	}
	
}
