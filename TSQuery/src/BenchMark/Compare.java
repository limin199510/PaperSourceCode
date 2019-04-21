package BenchMark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Compare {
	public ArrayList<int[]> dataInput(String path) {
		ArrayList<int[]> sequence=new ArrayList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String str;
			while ((str = br.readLine()) != null) {
				String[] oneSequence = str.split(",");
				int[] point=new int[oneSequence.length];
				for(int i=0;i<oneSequence.length;i++)
				point[i]=Integer.parseInt(oneSequence[i])-1;
				sequence.add(point);
			}
		 br.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		return sequence;
	}
	public static void main(String[] args) {
		Compare com=new Compare();
		ArrayList<int[]> baseIndexList=com.dataInput("D:/论文/程序/SIMS/baseSymbols.txt");
		ArrayList<int[]> compareIndexList=com.dataInput("D:/论文/程序/SIMS/lshSymbols.txt");
		//要看对比方法查到的序列和dtw查到的差多少
		//int finalBase=0,finalRealCount=0,finalSearchCount=0;
		double finalRecall=0,finalAcc=0;
		for(int i=0;i<baseIndexList.size();i++) {
			double recall=0,acc=0;
			int count=0;
			int baseNum=baseIndexList.get(i).length,comNum=compareIndexList.get(i).length;
			for(int j=0;j<comNum;j++) {
				int comIndex=compareIndexList.get(i)[j];
				for(int k=0;k<baseNum;k++) {
					int baseIndex=baseIndexList.get(i)[k];
					if(baseIndex==comIndex) {
						count++;
						break;
					}else if(comIndex<baseIndex) {
						break;
					}
				}
			}
			recall=count/(baseNum*1.0);
			acc=count/(comNum*1.0);
			finalRecall+=recall;
			finalAcc+=acc;
		}
		System.out.println(finalRecall/(baseIndexList.size()*1.0)+","+finalAcc/(baseIndexList.size()*1.0));
	}
}
