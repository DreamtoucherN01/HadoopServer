package com.blake.storage.hadoop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.blake.server.handler.EventHandler;

public class HadoopServerImpl extends HadoopServer {

	Logger logger = Logger.getLogger(HadoopServerImpl.class);
	
	public static void mkdir(String dir) throws IOException{
		
        fs.mkdirs(new Path(dir));
    }
	
	@SuppressWarnings("deprecation")
	public static void listAll(String dir) throws IOException  {  
		
        FileStatus[] stats = fs.listStatus(new Path(dir));  
        for(int i = 0; i < stats.length; ++i) {  
        	
            if (!stats[i].isDir())  { 
            	
                // regular file  
                System.out.println(stats[i].getPath().toString());  
            }  else   {  
            	
                // dir  
                System.out.println(stats[i].getPath().toString());  
            }  
                  
        }  
    }  
	
	public static void readFileFromHdfs(String path) {  

		try {
			
			Path f = new Path(path);  
			FSDataInputStream dis = fs.open(f);  
			InputStreamReader isr = new InputStreamReader(dis, "utf-8");  
			BufferedReader br = new BufferedReader(isr);  
			String str = "";  
			while ((str = br.readLine()) !=null) { 
				
			    System.out.println(str);  
			}  
			br.close();  
			isr.close();  
			dis.close();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	} 
	
	public static void writeToHdfs(String path, Object data) {
	
		try {
			
			FSDataOutputStream fout = fs.create(new Path(path));
			fout.write(data.toString().getBytes());
			fout.flush();
			fout.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
	}
	
	public static void copyFileToHDFS(String dest, String src) {  
		
		try {  
			
			Path f = new Path(dest);  
			File file = new File(src);  
        
			FileInputStream is = new FileInputStream(file);  
			InputStreamReader isr = new InputStreamReader(is, "utf-8");  
			BufferedReader br = new BufferedReader(isr);  
        
			FSDataOutputStream os = fs.create(f, true);  
			Writer out = new OutputStreamWriter(os, "utf-8");  
        
			String str = "";  
			while((str=br.readLine()) != null) {  
				out.write(str+"\n");  
			}  
			br.close();  
			isr.close();  
			is.close();  
			out.close();  
			os.close();  
			System.out.println("Write content of file "+file.getName()+" to hdfs file "+f.getName()+" success");  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  
	
	public static void checkFileExist(String file) {
		
		try {
			
			Path a= fs.getHomeDirectory();  
			System.out.println("main path:"+a.toString());  
        
			Path f = new Path(file);  
			boolean exist = fs.exists(f);  
			System.out.println("Whether exist of this file:" + exist);  
        
//			//ɾ���ļ�  
//			if (exist) {  
//				boolean isDeleted = fs.delete(f, false);  
//				if(isDeleted) {  
//					System.out.println("Delete success");  
//				}                 
//			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  
}