package BenchMark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Common {
	public static int []kinds=new int[6];
	public static String databasePath="D:/论文/程序/SIMS/Symbols.txt";
	public static String queryPath="D:/论文/程序/SIMS/querySymbols.txt";
	public static int embedCount=2,hashCount=2,hashPosCount=7,count=2,embed=3;	
	public double calDtw(double data1[],double data2[]) {
		double [][]distance=new double[data1.length][data2.length];
		double [][]output=new double[data1.length][data2.length];
		//计算点与点之间的欧式距离
		for(int i=0;i<data1.length;i++){
			for(int j=0;j<data2.length;j++){
				distance[i][j]=Math.abs(data1[i]-data2[j]);			
			}
		}
		//DP过程，计算DTW距离
		for(int i=0;i<data1.length;i++){
			for(int j=0;j<data2.length;j++){
				if(i-1<0&&j-1<0)
					output[i][j]=distance[i][j];
				else if(i-1<0)
					output[i][j]=output[i][j-1]+distance[i][j];
				else if(j-1<0)
					output[i][j]=output[i-1][j]+distance[i][j];
				else
					output[i][j]=Math.min(Math.min(output[i-1][j-1],output[i][j-1]),output[i-1][j])+distance[i][j];
			}
		}
		//寻找最佳规整路径
		/*int i=data1.length-1,j=data2.length-1;
		path=i+","+j+"\t";
		find(i,j,output);
		System.out.println(path);*/
		return output[data1.length-1][data2.length-1];
	}
	public ArrayList<double[]> dataInput(String path) {
		ArrayList<double[]> sequence=new ArrayList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			while ((str = br.readLine()) != null) {
				String[] oneSequence = str.split(",");
				double[] point=new double[oneSequence.length-1];
				for(int i=1,j=0;i<oneSequence.length;i++,j++)
					point[j]=Double.parseDouble(oneSequence[i]);
				//kinds[Integer.parseInt(oneSequence[0])-1]++;
				sequence.add(point);
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		return sequence;
	}
	public ArrayList<int[]> dataInput3(String path,int len) {
		ArrayList<int[]> sequence=new ArrayList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			while ((str = br.readLine()) != null) {
				if(str.length()>1) {
					String[] oneSequence = str.split(",");
					int[] point=new int[oneSequence.length];
					for(int i=0;i<oneSequence.length;i++)
						point[i]=Integer.parseInt(oneSequence[i]);
					sequence.add(point);
				}else {
					int[] point=new int[1];
					point[0]=len;
					sequence.add(point);
				}
				
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		return sequence;
	}

	public void dataOutput(String file, String[] content) {
    	BufferedWriter out = null;
    	try {
    		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
    		for(int i=0;i<content.length;i++)
    			out.write(content[i]+",");
    		out.write("\r\n");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
	    	try {
	    	out.close();
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
    	}
    }
	public void dataOutput2(String file, HashSet hs) {
    	BufferedWriter out = null;
    	try {
    		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
    		Iterator it=hs.iterator();
    		while(it.hasNext()) {
    			out.write(it.next()+",");
    		}	
    		out.write("\r\n");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
	    	try {
	    	out.close();
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
    	}
    }
	public double getMean(double point[]) {
		double mean=0;
		for(int i=0;i<point.length;i++) {
			mean+=point[i];
		}
		mean/=point.length;	
		return mean;
	}
	public double getStd(double point[],double mean) {
		double std=0;
		for(int i=0;i<point.length;i++) {
			std+=(point[i]-mean)*(point[i]-mean);
		}
		std=Math.sqrt(std/point.length);
		return std;
		
	}
	public ArrayList<double[]>  preProcess(ArrayList<double[]> sequence) {
		ArrayList<double[]> preSequence=new ArrayList();
		for(int i=0;i<sequence.size();i++) {
			double mean=getMean(sequence.get(i));
			double std=getStd(sequence.get(i),mean);
			double pre[]=new double[sequence.get(i).length];
			for(int j=0;j<sequence.get(i).length;j++) {
				pre[j]=(sequence.get(i)[j]-mean)/std;
			}
			preSequence.add(pre);
		}
		return preSequence;
		
	}
	public void saveDivPara(ArrayList<double[]> sequence,int rowNum,int colNum) {
		double min=sequence.get(0)[0];
		double max=sequence.get(0)[0];
		double maxLength=-65535;
		ArrayList<int[]> pointID=new ArrayList();
		for(int i=0;i<sequence.size();i++) {
			for(int j=0;j<sequence.get(i).length;j++) {
				if(max<sequence.get(i)[j])
					max=sequence.get(i)[j];
				if(min>sequence.get(i)[j])
					min=sequence.get(i)[j];
			}
			if(maxLength<sequence.get(i).length)
				maxLength=sequence.get(i).length;
		}
		double cellLength=((max-min)/(rowNum*1.0))*1.01;
		double cellWidth=((maxLength-1)/(colNum*1.0))*1.01;
		try {
			PrintStream out1 = new PrintStream("D:/论文/程序/SIMS/divPara.txt");
			out1.print(max+","+min+","+cellWidth+","+cellLength+","+colNum+","+rowNum+","+maxLength);	
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public String[] readOne(String path) {
		String[] parameter=new String[1];
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			while ((str = br.readLine()) != null) {
				parameter = str.split(",");
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		}
		return parameter;
	}
	public ArrayList<int[]> divCell(ArrayList<double[]> sequence) {
		double max=0,min=0,cellWidth=0,cellLength=0;
		int colNum=0,rowNum=0;
		String []parameter=readOne("D:/论文/程序/SIMS/divPara.txt");
		max=Double.parseDouble(parameter[0]);
		min=Double.parseDouble(parameter[1]);
		cellWidth=Double.parseDouble(parameter[2]);
		cellLength=Double.parseDouble(parameter[3]);
		colNum=Integer.parseInt(parameter[4]);
		rowNum=Integer.parseInt(parameter[5]);
		ArrayList<int[]> pointID=new ArrayList();
		for(int i=0;i<sequence.size();i++) {
			int seqID[]=new int[sequence.get(0).length];
			for(int j=0;j<sequence.get(i).length;j++) {
				int row=0,col=0;
				if(sequence.get(i)[j]<min)
					row=1;
				else if(sequence.get(i)[j]>max)
					row=rowNum;
				else
					row=(int) (Math.floor((sequence.get(i)[j]-min)/cellLength)+1);
				col=(int) (Math.floor(j/cellWidth)+1);
				if(col>colNum)
					col=colNum;
				seqID[j]=(row-1)*colNum+col;
			}
			pointID.add(seqID);
		}
		return pointID;
	}
	public ArrayList<int[]> Embedding(ArrayList<int[]> pointID,int flag,int count){
		int N=3*pointID.get(0).length;
		String path="D:/论文/程序/SIMS/embedPara.txt";
		Random rnd=new Random();
		int []randPara=new int[N];
		if(flag==1) {
			for(int i=0;i<N;i++) 
				randPara[i]=rnd.nextInt(2);
			String []content= new String[randPara.length];
			for(int i=0;i<randPara.length;i++) 
				content[i]=Integer.toString(randPara[i]);
			dataOutput(path,content);
		}else 
			randPara=getValueList(path).get(count);
		ArrayList<int[]> embedID=new ArrayList();
		for(int i=0;i<pointID.size();i++) {
			int oneEmbed[]=new int[N];
			int j=0,k=0;
			while(j<N) {
				if(k<pointID.get(i).length) {
					oneEmbed[j]=pointID.get(i)[k];
					k+=randPara[j];
				}else {
					oneEmbed[j]=0;
				}
				j++;
			}
			embedID.add(oneEmbed);
		}
		return embedID;
	}
	public ArrayList<int[]> getValueList(String path){
		ArrayList<int []> valueList=new ArrayList();
		int []values;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			String[] randStr;
			while ((str = br.readLine()) != null) {
				randStr = str.split(",");
				values=new int[randStr.length];
				for(int i=0;i<randStr.length;i++)
					values[i]=Integer.parseInt(randStr[i]);
				valueList.add(values);
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		}
		return valueList;
	}
	public ArrayList<String> Lsh(ArrayList<int[]> embedID,int m,int flag,int count) {
		String path="D:/论文/程序/SIMS/hashPara.txt"; 
		int []hashPara;
		if(flag==1) {
			//确保随机产生m个不同的位置 
			hashPara=new int[m];
			Random rnd=new Random();
			for(int i=0;i<m;i++) {
				while (true) {
					int ran=rnd.nextInt(embedID.get(0).length);
					for(int j=0;j<i;j++) {
						if(hashPara[j]==ran) {
							ran=-1;
							break;
						}
					}
					if(ran!=-1) {
						hashPara[i]=ran;
						break;
					}
				}
			}
			String []content=new String[m];
			for(int i=0;i<hashPara.length;i++)
				content[i]=String.valueOf(hashPara[i]);
			dataOutput(path,content);
		}else {
			hashPara=getValueList(path).get(count);
			m=hashPara.length;
		}
		ArrayList<String> hashBucket=new ArrayList();
		for(int i=0;i<embedID.size();i++) {
			int k=0;
			String bucket=new String("");
			while(k<m) {
				//取出该位置下的cellID
				bucket+=Integer.toString(embedID.get(i)[hashPara[k]])+",";
				k++;
			}
			hashBucket.add(bucket);
		}
		return hashBucket;
	}

	
}
