package BenchMark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Base {
	public static double threshold=13.0;
	public ArrayList<double[]> dataInput(String path,int flag) {
		ArrayList<double[]> sequence=new ArrayList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			while ((str = br.readLine()) != null) {
				String[] oneSequence = str.split(",");
				double[] point=new double[oneSequence.length-1];
				for(int i=1,j=0;i<oneSequence.length;i++,j++)
					point[j]=Double.parseDouble(oneSequence[i]);
				if(flag==1)
					Common.kinds[Integer.parseInt(oneSequence[0])-1]++;
				sequence.add(point);
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		return sequence;
	}
	public static void main(String[] args) {
		long start=System.currentTimeMillis();   //获取开始时间
		int counts=0;
		Common co=new Common();
		Base ba=new Base();
		double dtw;
		ArrayList<double[]> database=ba.dataInput(Common.databasePath,1);
		ArrayList<double[]> query=co.dataInput(Common.queryPath);
		for(int i=0;i<query.size();i++) {
			String index="";
			int count=0;
			for(int j=0;j<database.size();j++) {
				dtw=co.calDtw(query.get(i), database.get(j));
				if(dtw<=threshold) { 
					index+=(j+1)+",";
					count++;
				}
			}
			System.out.println(count);
			counts+=count;
			co.dataOutput("D:/论文/程序/SIMS/baseSymbols.txt", index.split(","));
		}
		long end=System.currentTimeMillis();   //获取开始时间
		System.out.println(counts);
		System.out.println((end-start)/1000.0+"s");

		
	}

}
