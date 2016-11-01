package com.blake.storage.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class HadoopServer {
	
	static String PATH = "hdfs://master:9000/";
	
	static String FILENAME = "crawler";
	
	public static Configuration conf =  new Configuration();
	static DistributedFileSystem  fs;
	
	static {
		
        conf.set("fs.defaultFS", PATH); 
        try {
			fs = (DistributedFileSystem) FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}