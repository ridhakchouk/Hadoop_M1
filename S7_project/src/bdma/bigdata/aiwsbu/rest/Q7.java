package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.mapreduce.Query7;

public class Q7 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		Query7 q7 = new Query7();
		System.out.println(q7.moyenneEtudiant("L2", "2004"));
	}

}
