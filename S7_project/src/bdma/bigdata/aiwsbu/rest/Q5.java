package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.mapreduce.Query5;

public class Q5 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		Query5 q5 = new Query5();
		System.out.println(q5.moyenneProgramAnnee("L1", "2002"));
	}

}
