package Lsh;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

import BenchMark.Common;
public class DataProcess{
	private Node root=new Node(); //根 
	private ArrayList<Node> nodes=new ArrayList();//节点列表
	private int randPara[];//抻长的随机数
	private int hashPara[];//哈希位置
	public DataProcess(){  
		root = new Node("");   
	}
	//插入函数，先判断是否有该哈希值，如果没有，挨着顺序判断
    //如果有这个数字，继续判断下一个，当没有这个数字的时候，对这个字母new一个node对象，放入到上一个数字的LinkedList里面       
    public Node insert(String[] bucket,int index,ArrayList<Node> n){ 	
        //如果找到就返回
        if(search(bucket) != null) {
        	String str=search(bucket).getSet();
        	str+=Integer.toString(index)+",";
        	search(bucket).setSet(str);
        	return search(bucket);     
        }
        Node current = root;   
        for(int i = 0; i < bucket.length; i++){  
            Node child = current.subNode(bucket[i]);  
            if(child != null){   
                current = child;  
            } else { 
            	 n.add(current);
            	 LinkedList<Node> temp=current.getChildList();
            	 temp.add(new Node(bucket[i]));
                 current.setChildList(temp);
                 current = current.subNode(bucket[i]); 
                 
            }      
        }   
        String str=current.getSet();
    	str+=Integer.toString(index)+",";
    	current.setSet(str);
    	current.setEnd(true);
        n.add(current);
        return current;
    }  
    public Node search(String[] bucket){  
        Node current = root;    
        for(int i = 0; i < bucket.length; i++){      
            if(current.subNode(bucket[i]) == null)  
                return null;  
            else  
                current = current.subNode(bucket[i]);  
        }  
        //判断是否是该哈希值的结尾
        if (current.isEnd()== true) return current;  
        else return null;  
    }
 
    public  void writeToFile(String path,ArrayList<Node> nodes) throws IOException, IOException {  
    	File file=new File(path);
        boolean isexist=false;//定义一个用来判断文件是否需要截掉头aced 0005的
        if(file.exists())  
    	       isexist=true;  
    	long pos = 0;  
    	FileOutputStream fo=new FileOutputStream(file,true);    
    	ObjectOutputStream out = new ObjectOutputStream(fo);  
    	if(isexist){  
    	pos=fo.getChannel().position()-4;  
    	        fo.getChannel().truncate(pos);  
    	}    
    	out.writeObject(nodes);  
    	out.close();  
    }  
    public void objectSave(String path,ArrayList<Node> nodes,String fileN) throws IOException {
    	File file=new File(path);
    	if(!file.exists()){
			file.mkdir();
		}
    	SimpleDateFormat sFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar=Calendar.getInstance();
		//获取系统当前时间并将其转换为string类型
		String fileName=fileN+"---"+sFormat.format(calendar.getTime());
    	File f=File.createTempFile(fileName, ".txt",file);
        FileOutputStream fo=new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fo);
        oos.writeObject(nodes);//进行序列化
        oos.close();  
    }
    public static void main(String[] args) throws Exception {
		Common co=new Common();
		//获取数据库中所有的序列
		ArrayList<double[]> database=co.dataInput(Common.databasePath);
		int rowNum=5,colNum=database.get(0).length;
		//找到序列中的最值并保存
		co.saveDivPara(database, rowNum, colNum);
		//将数据库中的序列划分到cell中
		ArrayList<int[]> pointID=co.divCell(database);
		for(int i=0;i<Common.embedCount;i++) {
			//对cell序列进行第i次抻长
			ArrayList<int[]> embedID=co.Embedding(pointID, 1, 0);
			for(int j=0;j<Common.hashCount;j++) {
				//对cell序列在第i次抻长的情况下 进行第j次哈希
				DataProcess dp=new DataProcess();
				ArrayList<String> hashBucket=co.Lsh(embedID, Common.hashPosCount, 1, 0);
				dp.nodes=new ArrayList();
				//对此次抻长哈希后的结果，为cell序列建立前缀索引
				for(int k=0;k<hashBucket.size();k++) {
					String []bucket=hashBucket.get(k).split(",");
					dp.insert(bucket, k+1,dp.nodes);
				}
				String fileName=Integer.toString(i*Common.count+j+1);
				if(fileName.length()==1)
					fileName="00"+fileName;
				else if(fileName.length()==2)
					fileName="0"+fileName;	
				//将此次的索引结果保存
				dp.objectSave("D:/论文/程序/SIMS/索引",dp.nodes,fileName);
			}
		}
	}
}
