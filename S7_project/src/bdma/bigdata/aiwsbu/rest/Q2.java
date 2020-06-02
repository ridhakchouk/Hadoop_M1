package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.mapreduce.Query2;

public class Q2 {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Query2 q2 = new Query2();
		System.out.println(q2.tauxParAnnee("02"));
	}

}
