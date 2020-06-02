package bdma.bigdata.aiwsbu.rest;

import java.io.IOException;

import bdma.bigdata.aiwsbu.mapreduce.Query3_2;

public class Q3 {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Query3_2 q3 = new Query3_2();
		System.out.println(q3.tauxParIdCours("S10B046"));
	}

}
