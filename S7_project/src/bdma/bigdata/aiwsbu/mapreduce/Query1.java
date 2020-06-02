package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
import bdma.bigdata.aiwsbu.core.Etudiant;

public class Query1 {
	HashMap<String, String> cours = new HashMap<String, String>();

	Etudiant etudiant = new Etudiant();

	public Query1() {
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

	public Etudiant etudiant(String idstudent, String program) throws IOException {
		String nom = "";
		String prenom = "";
		String program_student = "";
		String semestre1 = "";
		String semestre2 = "";
		HashMap<String, ArrayList<String>> notes1 = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> notes2 = new HashMap<String, ArrayList<String>>();
		HashMap<String, HashMap<String, String>> notes_semestre1 = new HashMap<String, HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> notes_semestre2 = new HashMap<String, HashMap<String, String>>();
		// HashMap<String,String> semestre1_notes = new HashMap<String, String>();
		// HashMap<String,String> semestre2_notes = new HashMap<String, String>();

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
		}

		Configuration conf = HBaseConfiguration.create();
		HTable tableStudents = new HTable(conf, "21407340t:S");
		HTable tableGrades = new HTable(conf, "21407340t:G");
		HTable tableCourses = new HTable(conf, "21407340t:C");

		Get get = new Get(Bytes.toBytes(idstudent));
		Result resultStudent = tableStudents.get(get);
		if (resultStudent.size() > 0) {
			List<Cell> cells = resultStudent.listCells();
			for (Cell cell : cells) {
				if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("#")
						&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("F")) {
					prenom = Bytes.toString(CellUtil.cloneValue(cell));
				} else if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("#")
						&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("L")) {
					nom = Bytes.toString(CellUtil.cloneValue(cell));
				} else if (Bytes.toString(CellUtil.cloneFamily(cell)).equals("#")
						&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("P")) {
					program_student = Bytes.toString(CellUtil.cloneValue(cell));
					if (program_student.equals("1")) {
						etudiant.setProgram("L1");
					} else if (program_student.equals("2")) {
						etudiant.setProgram("L2");
					} else if (program_student.equals("3")) {
						etudiant.setProgram("L3");
					} else if (program_student.equals("4")) {
						etudiant.setProgram("M1");
					} else if (program_student.equals("5")) {
						etudiant.setProgram("M2");
					}
				} else if (Bytes.toString(CellUtil.cloneFamily(cell)) == "C"
						&& Bytes.toString(CellUtil.cloneQualifier(cell)).equals("E")) {
					etudiant.setEmail(Bytes.toString(CellUtil.cloneValue(cell)));
				}
			}
			etudiant.setNom(prenom + " " + nom);
		} else
			etudiant = null;

		if (etudiant != null) {
			Scan scanGrades = new Scan();
			ResultScanner resultScanner = tableGrades.getScanner(scanGrades);
			Result result = resultScanner.next();
			String id = "";
			String note = "";
			String cours = "";
			String annee;
			ArrayList<String> list = new ArrayList<String>(); // Note et nom du cours

			while (result != null) {
				List<Cell> cellsGrades = result.listCells();
				for (Cell cell : cellsGrades) {
					id = Bytes.toString(CellUtil.cloneRow(cell));
					if (id.substring(7, 17).equals(idstudent) && id.substring(19, 21).equals(semestre1)) {
						cours = id.substring(18);
						annee = id.substring(0, 4);
						note = Bytes.toString(CellUtil.cloneValue(cell));
						list.add(cours + "/" + (9999 - Integer.parseInt(annee)));
						list.add(note);
						notes1.put(cours, list);
						list = new ArrayList<String>();
					} else if (id.substring(7, 17).equals(idstudent) && id.substring(19, 21).equals(semestre2)) {
						cours = id.substring(18);
						annee = id.substring(0, 4);
						note = Bytes.toString(CellUtil.cloneValue(cell));
						list.add(cours + "/" + (9999 - Integer.parseInt(annee)));
						list.add(note);
						notes2.put(cours, list);
						list = new ArrayList<String>();
					}
				}
				result = resultScanner.next();
			}

			HashMap<String, String> n1 = new HashMap<String, String>();
			HashMap<String, String> n2 = new HashMap<String, String>();

			Set<Entry<String, ArrayList<String>>> setHm = notes1.entrySet();
			Iterator<Entry<String, ArrayList<String>>> it = setHm.iterator();
			String id_cours = "";
			String debut = "";
			int year = 0;
			while (it.hasNext()) {
				Entry<String, ArrayList<String>> e = it.next();
				id_cours = e.getValue().get(0);
				debut = e.getValue().get(0).substring(0, 8);
				year = Integer.parseInt(e.getValue().get(0).substring(8));
				String value = "";
				remplirMapCours(debut);

				value = this.cours.get(id_cours);
				e.getValue().add(value);
				n1 = new HashMap<String, String>();
				n1.put(e.getValue().get(2), e.getValue().get(1));
				notes_semestre1.put(e.getKey(), n1);
			}

			Set<Entry<String, ArrayList<String>>> setHm2 = notes2.entrySet();
			Iterator<Entry<String, ArrayList<String>>> it2 = setHm2.iterator();
			while (it2.hasNext()) {
				Entry<String, ArrayList<String>> e = it2.next();
				id_cours = e.getValue().get(0);
				debut = e.getValue().get(0).substring(0, 8);
				year = Integer.parseInt(e.getValue().get(0).substring(8));
				String value = "";
				remplirMapCours(debut);
				
				value = this.cours.get(id_cours);
				e.getValue().add(value);
				n2 = new HashMap<String, String>();
				n2.put(e.getValue().get(2), e.getValue().get(1));
				notes_semestre2.put(e.getKey(), n2);
			}

		}

		tableStudents.close();
		tableGrades.close();
		tableCourses.close();
		etudiant.setNotes1(notes_semestre1);
		etudiant.setNotes2(notes_semestre2);
		return etudiant;
	}

}
