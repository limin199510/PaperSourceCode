package Lsh;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import BenchMark.Common;
public class SimilaritySearch{
//查询序列经过和数据库序列相同的操作之后，然后去在建立的索引中查找看是否有相似的序列
	public Node search(String[] bucket,ArrayList<Node> nodeList){  
        Node current = nodeList.get(0);    
        for(int i = 0; i < bucket.length; i++){      
            if(current.subNode(bucket[i]) == null)  
                return null;  
            else  
                current = current.subNode(bucket[i]);  
        }  
        //判断是否是该哈希值的结尾
        if (current.isEnd == true) return current;  
        else return null;  
    } 
	
	public ArrayList<Node> objectRead(String path) throws Exception{
		ArrayList<Node> nodeList=new ArrayList();
		File file = new File(path);
        if(file.exists()){
        	ObjectInputStream ois;
        	try {
        	FileInputStream fn=new FileInputStream(file);
        	ois = new ObjectInputStream(fn);
        	nodeList=(ArrayList<Node>)ois.readObject();
        	ois.close();
            } catch (Exception e1) {
            	e1.printStackTrace();
            }
        }
        return nodeList;
	} 
	public String getFiles(String path,int count) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        if(file.exists()) {
	        File[] tempList = file.listFiles();
	        if(count<tempList.length) {
		        if (tempList[count].isFile()) {
		        	return tempList[count].toString();
		        }
	        }
        }
        return "";
    }

}
