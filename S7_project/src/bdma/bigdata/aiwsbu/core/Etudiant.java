package bdma.bigdata.aiwsbu.core;

import java.util.HashMap;

public class Etudiant {
	private String Nom;
	private String Email;
	private String Program;
	
	HashMap<String, HashMap<String,String>> First = new HashMap<String, HashMap<String,String>>();
	HashMap<String, HashMap<String,String>> Second = new HashMap<String, HashMap<String,String>>();
	
	/*
	HashMap<String, String> notes1 = new HashMap<String, String>();
	HashMap<String, String> notes2 = new HashMap<String, String>();
	*/
	public Etudiant() {}

	public String getNom() {
		return Nom;
	}

	public String getEmail() {
		return Email;
	}

	public String getProgram() {
		return Program;
	}

	public HashMap<String, HashMap<String,String>> getNotes1() {
		return First;
	}

	public HashMap<String, HashMap<String, String>> getNotes2() {
		return Second;
	}

	public void setNom(String nom) {
		this.Nom = nom;
	}

	public void setEmail(String email) {
		this.Email = email;
	}

	public void setProgram(String program) {
		this.Program = program;
	}

	public void setNotes1(HashMap<String, HashMap<String, String>> notes1) {
		this.First = notes1;
	}

	public void setNotes2(HashMap<String, HashMap<String, String>> notes2) {
		this.Second = notes2;
	}

	@Override
	public String toString() {
		return "Etudiant [Nom=" + Nom + ", Email=" + Email + ", Program=" + Program + ", First=" + First + ", Second="
				+ Second + "]";
	}



/*
	public HashMap<String, String> getNotes1() {
		return notes1;
	}

	public void setNotes1(HashMap<String, String> notes1) {
		this.notes1 = notes1;
	}

	public HashMap<String, String> getNotes2() {
		return notes2;
	}

	public void setNotes2(HashMap<String, String> notes2) {
		this.notes2 = notes2;
	}
*/	
	

}
