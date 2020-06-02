package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.mapreduce.Query4;

public class Q4 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		Query4 q4 = new Query4();
		System.out.println(q4.tauxIdAnnee("S10B046", "2007"));

	}

}
