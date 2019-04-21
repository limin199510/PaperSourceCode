package Lsh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import BenchMark.Base;
import BenchMark.Common;

public class Verify {
	public ArrayList<HashSet> findCandidate(ArrayList<int[]> pointID) throws Exception {
		ArrayList<HashSet> candidateList=new ArrayList();
		Common co=new Common();
		SimilaritySearch ss=new SimilaritySearch();
		for(int i=0;i<Common.embedCount;i++) {
			ArrayList<int[]> embedID=co.Embedding(pointID, 0, i);
			for(int j=0;j<Common.hashCount;j++) {
				int fileName=i*Common.count+j;
				ArrayList<String> hashBucket=co.Lsh(embedID, 0, 0, fileName);
				String path=ss.getFiles("D:/论文/程序/SIMS/索引", fileName);
				ArrayList<Node> nodeList=ss.objectRead(path);
				String finalResult="";
				for(int k=0;k<hashBucket.size();k++) {//20条查询序列对应20次
					String []bucket=hashBucket.get(k).split(",");
					Node n=ss.search(bucket, nodeList);
					if(n!=null) {
						String []candidate=n.set.split(",");
						if(i==0&&j==0) {
							HashSet hs=new HashSet();
							for(int p=0;p<candidate.length;p++) 
								hs.add(candidate[p]);
							candidateList.add(hs);
						}else {
							HashSet hs=candidateList.get(k);
							for(int p=0;p<candidate.length;p++)
								hs.add(candidate[p]);
							candidateList.set(k, hs);
						}
					}
					
				}//20条查询序列对应candidateList.size()为20
			}
		}//抻长哈希结束
		return candidateList;
	}
	public void finalVerify(ArrayList<HashSet> candidateList,ArrayList<double[]> database,ArrayList<double[]> query) {
		//ArrayList<String> verifyList=new ArrayList();
		Common co=new Common();
		//System.out.println(candidateList.size());
		for(int i=0;i<candidateList.size();i++) {
			String dtwVerify="";
			Iterator it=candidateList.get(i).iterator();
    		while(it.hasNext()) {
    			int index =Integer.parseInt((String)it.next());
    			double dtw=co.calDtw(query.get(i), database.get(index-1));
    			if(dtw<=Base.threshold) 
    				dtwVerify+=String.valueOf(index)+",";
    		}
    		co.dataOutput("D:/论文/程序/SIMS/lshSymbols.txt", dtwVerify.split(","));
		}
	}
	public static void main(String[] args) throws Exception {
		long start=System.currentTimeMillis();   //获取开始时间
		Common co=new Common();
		ArrayList<double[]> database=co.dataInput(Common.databasePath);
		ArrayList<double[]> query=co.dataInput(Common.queryPath);
		ArrayList<int[]> pointID=co.divCell(query);
		Verify vf=new Verify();
		ArrayList<HashSet> candidateList=vf.findCandidate(pointID);
		vf.finalVerify(candidateList, database, query);
		long end=System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
	}

}
