package Lb_keogh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import BenchMark.Base;
import BenchMark.Common;

public class Lb {
	public static int r=2;
	public ArrayList<double[]> dataInput(String path) {
		ArrayList<double[]> sequence=new ArrayList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			while ((str = br.readLine()) != null) {
				String[] oneSequence = str.split(",");
				double[] point=new double[oneSequence.length];
				for(int i=0;i<oneSequence.length;i++)
					point[i]=Double.parseDouble(oneSequence[i]);
				sequence.add(point);
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		return sequence;
	}
	public void findUL(ArrayList<double[]> sequence) {
		//看查询序列和数据库中序列的长度，以最长的为基准，为较短的计算上边界和下边界
		Common co=new Common();
		//int r=2;
		for(int i=0;i<sequence.size();i++) {
			String []up=new String[sequence.get(i).length];
			String []lo=new String[sequence.get(i).length];
			for(int j=0;j<sequence.get(i).length;j++) {
				double max=-65535,min=65535;
				if(j-r>=0&&j+r<=sequence.get(i).length) {
					for(int l=j-r;l<j+r;l++) {
						if(max<sequence.get(i)[l])
							max=sequence.get(i)[l];
						if(min>sequence.get(i)[l])
							min=sequence.get(i)[l];
					}
				}else if(j-r<0) {
					for(int l=0;l<j+r;l++) {
						if(max<sequence.get(i)[l])
							max=sequence.get(i)[l];
						if(min>sequence.get(i)[l])
							min=sequence.get(i)[l];
					}
				}else {
					for(int l=j-r;l<sequence.get(i).length;l++) {
						if(max<sequence.get(i)[l])
							max=sequence.get(i)[l];
						if(min>sequence.get(i)[l])
							min=sequence.get(i)[l];
					}
				}
				up[j]=String.valueOf(max);
				lo[j]=String.valueOf(min);
			}
			co.dataOutput("D:/论文/程序/SIMS/upper.txt", up);
			co.dataOutput("D:/论文/程序/SIMS/lower.txt", lo);
		}
	}
	public double calLb(double[] sequence,double[] upper,double[] lower) {
		Common co=new Common();
		double d=0;
		//ArrayList<String> canList=new ArrayList();
		//计算查询序列和数据库中序列的下界
		double dist=0;
		//String candidate="";
		int i=0;
		for(i=0;i<upper.length;i++) {
			if(sequence[i]>upper[i]) 
				d=(sequence[i]-upper[i])*(sequence[i]-upper[i]);
			else if(sequence[i]<lower[i]) 
				d=(sequence[i]-lower[i])*(sequence[i]-lower[i]);
			else 
				d=0;
			dist+=d;
		}
		
		return Math.sqrt(dist);
	}
	public void unEqual(double []shortS,double []longS,double []up,double[]lo) {
		for(int k=shortS.length;k<longS.length;k++) {
			if(k-r>=longS.length) {
				up[k]=up[k-1];
				lo[k]=lo[k-1];
			}
			else if(k+r<=longS.length) {
				double max=-65535,min=65535;
				for(int l=k-r;l<k+r;l++) {
					if(max<longS[l])
						max=shortS[l];
					if(min>longS[l])
						min=shortS[l];
				}
				up[k]=max;
				lo[k]=min;
			}else {
				double max=-65535,min=65535;
				for(int l=k-r;l<longS.length;l++) {
					if(max<longS[l])
						max=shortS[l];
					if(min>longS[l])
						min=shortS[l];
				}
				up[k]=max;
				lo[k]=min;
			}
		}
	}
	public static void main(String[] args) {
		long start=System.currentTimeMillis();   //获取开始时间
		Common co=new Common();
		Lb lb=new Lb();
		ArrayList<double[]> database=co.dataInput(Common.databasePath);
		//lb.findUL(database);
		ArrayList<double[]> query=co.dataInput(Common.queryPath);
		ArrayList<double[]> upper=lb.dataInput("D:/论文/程序/SIMS/upper.txt");
		ArrayList<double[]> lower=lb.dataInput("D:/论文/程序/SIMS/lower.txt");
		ArrayList<String> candidateList=new ArrayList();
		for(int i=0;i<query.size();i++) {
			String candidate="";
			//遍历数据库中的每一条序列
			for(int j=0;j<upper.size();j++) {
				double []up= {0};double []lo= {0};
				//判断查询序列和数据库序列的长度是否相同
				if(query.get(i).length>upper.get(j).length) {
					up=new double[query.get(i).length];
					lo=new double[query.get(i).length];
					for(int k=0;k<upper.get(j).length;k++) {
						up[k]=upper.get(j)[k];
						lo[k]=lower.get(j)[k];
					}
					lb.unEqual(upper.get(j), query.get(i), up, lo);
					upper.set(j, up);
				    lower.set(j, lo);
				    double dist=lb.calLb(query.get(i), upper.get(j), lower.get(j));
				    if(dist<=Base.threshold)
				    	candidate+=j+",";
				}//当查询序列的长度大于数据库中序列的长度时，需要对提前构建的上界和下界拉长到和查询序列一样的长度
				else if(query.get(i).length<upper.get(j).length) {
					up=new double[database.get(i).length];
					lo=new double[database.get(i).length];
					double max=-65535,min=65535;
					for(int k=0;k<query.get(i).length;k++) {
						if(k-r>=0&&k+r<=query.get(i).length) {
							for(int l=k-r;l<k+r;l++) {
								if(max<query.get(i)[l])
									max=query.get(i)[l];
								if(min>query.get(i)[l])
									min=query.get(i)[l];
							}
						}else if(k-r<0) {
							for(int l=0;l<j+r;l++) {
								if(max<query.get(i)[l])
									max=query.get(i)[l];
								if(min>query.get(i)[l])
									min=query.get(i)[l];
							}
						}else {
							for(int l=k-r;l<query.get(i).length;l++) {
								if(max<query.get(i)[l])
									max=query.get(i)[l];
								if(min>query.get(i)[l])
									min=query.get(i)[l];
							}
						}
						up[k]=max;
						lo[k]=min;
					}
					lb.unEqual(query.get(i), upper.get(j), up, lo);
					//upper.set(j, up);
				    //lower.set(j, lo);
				    double dist=lb.calLb(database.get(j), up,lo);
				    if(dist<=Base.threshold)
				    	candidate+=j+",";
			    }//当查询序列的长度小于数据库中序列的长度时，需要对查询序列构建上界和下界
				else {
					double dist=lb.calLb(query.get(i), upper.get(j), lower.get(j));
					//System.out.print(dist+",");
					if(dist<=Base.threshold) 
				    	candidate+=j+",";
				}
			}
			System.out.println(candidate.split(",").length);
			candidateList.add(candidate);
		}
		for(int i=0;i<candidateList.size();i++) {
			String []candidateArray=candidateList.get(i).split(",");
			String dtwVerify="";
			for(int j=0;j<candidateArray.length;j++) {
				int index=Integer.parseInt(candidateArray[j]);
				double dtw=co.calDtw(query.get(i), database.get(index));
				if(dtw<=Base.threshold) 
    				dtwVerify+=String.valueOf(index+1)+",";
			}
			co.dataOutput("D:/论文/程序/SIMS/lbSymbols.txt", dtwVerify.split(","));
		}
		long end=System.currentTimeMillis();   //获取开始时间
		System.out.println((end-start)/1000.0);
	}
}
