package hello;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import Queries.Query1;
import Queries.Query2;
import Queries.Query3;
import Queries.Query4;
import Queries.Query5;
import Queries.Query6;
import Queries.Query6Bis;
import Queries.Query7;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private final String responsenotfound = "RESPONSE NOT FOUND";

	
	//Q1
	@RequestMapping("/students/{idstudent}/transcripts/{program}")
	public @ResponseBody Object getEtu(@PathVariable(value = "idstudent") String idstudent, @PathVariable(value = "program") String program )
			throws IOException {

		Query1 q1 = new Query1();
		if(q1.etudiant(idstudent, program) != null) {
			return q1.etudiant(idstudent, program);
		}
		return responsenotfound;

	}
	
	//Q2
	@RequestMapping("rates/{semester}")
	public Object getGradeBySemester(@PathVariable(value = "semester") String semester) throws ClassNotFoundException, IOException, InterruptedException {
		Query2 q2 = new Query2();
		if(!q2.tauxParAnnee(semester).isEmpty() && q2.tauxParAnnee(semester) != null) {
			return q2.tauxParAnnee(semester);
		}
		else return responsenotfound;
	}
	
	
	//Q3 PB ICI
	@RequestMapping("courses/{id}/rates")
	public Object GetGradeByCourse(@PathVariable(value = "id") String id) throws ClassNotFoundException, IOException, InterruptedException {
		
		Query3 q3 = new Query3();
		if(!q3.tauxParIdCours(id).isEmpty() && q3.tauxParIdCours(id) != null) {
			return q3.tauxParIdCours(id);
		}
		else return responsenotfound;

	}
	
	//Q4
	@RequestMapping("/courses/{id}/rates/{year}")
	public Object getGradeByYear(@PathVariable(value = "id") String id, @PathVariable(value = "year") String year) throws ClassNotFoundException, IOException, InterruptedException {
		Query4 q4 = new Query4();
		if(!q4.tauxIdAnnee(id, year).isEmpty() && q4.tauxIdAnnee(id, year) != null) {
			return q4.tauxIdAnnee(id, year);
		}
		else return responsenotfound;
		 
	}
	
	//Q5
	@RequestMapping("/programs/{program}/means/{year}")
	public Object avgByProgramYear(@PathVariable(value = "program") String program, @PathVariable(value = "year") String year) throws ClassNotFoundException, IOException, InterruptedException {
		Query5 q5 = new Query5();
		if(!q5.moyenneProgramAnnee(program, year).isEmpty() && q5.moyenneProgramAnnee(program, year) != null) {
			return q5.moyenneProgramAnnee(program, year);
		}
		else return responsenotfound;
	}
	
	//Q6
	@RequestMapping("/instructors/{name}/rates")
	public Object gradeByInstructor(@PathVariable(value = "name") String name) throws ClassNotFoundException, IOException, InterruptedException {
		Query6Bis q6 = new Query6Bis();
		if(!q6.tauxProf(name).isEmpty() && q6.tauxProf(name) != null) {
			return q6.tauxProf(name);
		}
		else return responsenotfound;
		
	}
	
	
	@RequestMapping("/ranks/{program}/years/{year}")
	public Object rankByProgramYear (@PathVariable(value = "program") String program,@PathVariable(value = "year") String year) throws ClassNotFoundException, IOException, InterruptedException {
		Query7 q7 = new Query7();
		if(!q7.moyenneEtudiant(program, year).isEmpty() && q7.moyenneEtudiant(program, year) != null) {
			return q7.moyenneEtudiant(program, year);
		}
		else return responsenotfound;
	}
	
	@RequestMapping("/greeting")
	public @ResponseBody Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}
