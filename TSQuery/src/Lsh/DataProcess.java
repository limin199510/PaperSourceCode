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
	private Node root=new Node(); //�� 
	private ArrayList<Node> nodes=new ArrayList();//�ڵ��б�
	private int randPara[];//�ӳ��������
	private int hashPara[];//��ϣλ��
	public DataProcess(){  
		root = new Node("");   
	}
	//���뺯�������ж��Ƿ��иù�ϣֵ�����û�У�����˳���ж�
    //�����������֣������ж���һ������û��������ֵ�ʱ�򣬶������ĸnewһ��node���󣬷��뵽��һ�����ֵ�LinkedList����       
    public Node insert(String[] bucket,int index,ArrayList<Node> n){ 	
        //����ҵ��ͷ���
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
        //�ж��Ƿ��Ǹù�ϣֵ�Ľ�β
        if (current.isEnd()== true) return current;  
        else return null;  
    }
 
    public  void writeToFile(String path,ArrayList<Node> nodes) throws IOException, IOException {  
    	File file=new File(path);
        boolean isexist=false;//����һ�������ж��ļ��Ƿ���Ҫ�ص�ͷaced 0005��
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
		//��ȡϵͳ��ǰʱ�䲢����ת��Ϊstring����
		String fileName=fileN+"---"+sFormat.format(calendar.getTime());
    	File f=File.createTempFile(fileName, ".txt",file);
        FileOutputStream fo=new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fo);
        oos.writeObject(nodes);//�������л�
        oos.close();  
    }
    public static void main(String[] args) throws Exception {
		Common co=new Common();
		//��ȡ���ݿ������е�����
		ArrayList<double[]> database=co.dataInput(Common.databasePath);
		int rowNum=5,colNum=database.get(0).length;
		//�ҵ������е���ֵ������
		co.saveDivPara(database, rowNum, colNum);
		//�����ݿ��е����л��ֵ�cell��
		ArrayList<int[]> pointID=co.divCell(database);
		for(int i=0;i<Common.embedCount;i++) {
			//��cell���н��е�i���ӳ�
			ArrayList<int[]> embedID=co.Embedding(pointID, 1, 0);
			for(int j=0;j<Common.hashCount;j++) {
				//��cell�����ڵ�i���ӳ�������� ���е�j�ι�ϣ
				DataProcess dp=new DataProcess();
				ArrayList<String> hashBucket=co.Lsh(embedID, Common.hashPosCount, 1, 0);
				dp.nodes=new ArrayList();
				//�Դ˴��ӳ���ϣ��Ľ����Ϊcell���н���ǰ׺����
				for(int k=0;k<hashBucket.size();k++) {
					String []bucket=hashBucket.get(k).split(",");
					dp.insert(bucket, k+1,dp.nodes);
				}
				String fileName=Integer.toString(i*Common.count+j+1);
				if(fileName.length()==1)
					fileName="00"+fileName;
				else if(fileName.length()==2)
					fileName="0"+fileName;	
				//���˴ε������������
				dp.objectSave("D:/����/����/SIMS/����",dp.nodes,fileName);
			}
		}
	}
}
