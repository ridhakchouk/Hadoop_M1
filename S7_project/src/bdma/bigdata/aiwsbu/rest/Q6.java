package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.mapreduce.Query6;
import bdma.bigdata.aiwsbu.mapreduce.Query6Bis;

public class Q6 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		Query6 q6 = new Query6();
		System.out.println(q6.tauxProf("Wcrgc Ivoktenw"));
		
		Query6Bis q6bis = new Query6Bis();
		q6bis.tauxProf("Wcrgc Ivoktenw");
	}

}
