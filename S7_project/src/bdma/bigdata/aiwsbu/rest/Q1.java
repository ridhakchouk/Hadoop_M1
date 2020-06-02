package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.core.Etudiant;
import bdma.bigdata.aiwsbu.mapreduce.Query1;

public class Q1 {

	public static void main(String[] args) throws IOException {
		Query1 q = new Query1();
		Etudiant e = q.etudiant("2001000047", "L2");
		System.out.println(e.toString());

	}

}
