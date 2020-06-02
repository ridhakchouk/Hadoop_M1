package bdma.bigdata.aiwsbu.mapreduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;


public class Query2 {
	
	public Query2() {}
	

		
	public HashMap<String, String> tauxParAnnee(String semestre) throws IOException, ClassNotFoundException, InterruptedException{
		HashMap<String,String> map = new HashMap<String, String>();				
		Configuration config = HBaseConfiguration.create();
        
		HTable responseTable = new HTable(config, "response2");
        Scan scanResponse = new Scan();
        ResultScanner resultScanner = responseTable.getScanner( scanResponse );
        Result result = resultScanner.next();
        String id_response;
        String valeur_response;
        while(result != null) {
            List<Cell> cellsGrades = result.listCells();
            for(Cell cell : cellsGrades) {
            	id_response = Bytes.toString(CellUtil.cloneRow(cell));
            	valeur_response = Bytes.toString(CellUtil.cloneValue(cell));
            	result = resultScanner.next();
            	if(id_response.substring(0, 2).equals(semestre)) {
            		map.put(id_response.substring(2), valeur_response);
            	}
            }

        }
		return map;
	}

}
