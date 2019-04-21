package SetBased;
import java.util.ArrayList;
import java.util.HashSet;

import BenchMark.Common;
public class SetBaseIndex {
	public void bulidIndex(ArrayList<double []> database){
		Common co=new Common();
		ArrayList<int[]> pointID=co.divCell(database);
		String []parameter=co.readOne("D:/����/����/SIMS/divPara.txt");
		int colNum=Integer.parseInt(parameter[4]);
		int rowNum=Integer.parseInt(parameter[5]);
		ArrayList<HashSet> index=new ArrayList<>();
		for(int i=0;i<colNum*rowNum;i++) 
			index.add(new HashSet());
		for(int i=0;i<pointID.size();i++) {
			for(int j=0;j<pointID.get(i).length;j++) {
				int id=pointID.get(i)[j];
				index.get(id-1).add(i);
			}
		}
		for(int i=0;i<index.size();i++)
			co.dataOutput2("D:/����/����/SIMS/cellSymbols.txt", index.get(i));
	}
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		double jacThreshold=0.75;
		int counts=0;
		Common co=new Common();
		ArrayList<double []> database=co.dataInput(Common.databasePath);
		SetBaseIndex sbi=new SetBaseIndex();
		//sbi.bulidIndex(database);
		ArrayList<double []> query=co.dataInput(Common.queryPath);
		ArrayList<int[]>pointID=co.divCell(query);
		ArrayList<int[]>index=co.dataInput3("D:/����/����/SIMS/cellSymbols.txt",database.size());//ÿ��cell�а�������Щ���У�sizeΪcell�ĸ�����size.lengthΪ������cell�����еĸ���
		for(int i=0;i<pointID.size();i++) {
			int intersection[]=new int[database.size()];//�����ݿ���ÿһ�����еĽ���
			double []jac=new double[database.size()];//�����ݿ���ÿһ�����е�jac
			String jacIndex="";
			for(int j=0;j<pointID.get(i).length;j++) {
				int id=pointID.get(i)[j];//��i����ѯ�����еĵ�j������䵽�ĸ�cell�У�
				for(int k=0;k<index.get(id-1).length;k++){//�����ݿ�����Щ���е�ID�ڸ�cell��
					if(index.get(id-1)[k]!=database.size()) {
						intersection[index.get(id-1)[k]]++;//���ڰ�����cell�����м�1
					}
				}
			}
			int count=0;
			for(int j=0;j<intersection.length;j++) {
				double union=(database.get(j).length+pointID.get(i).length-intersection[j])*1.0;
				jac[j]=intersection[j]/union;
				if(jac[j]>=jacThreshold) { 
					jacIndex+=(j+1)+",";
					count++;
				}
			}
			System.out.println(count);
			counts+=count;
			co.dataOutput("D:/����/����/SIMS/jacSymbols.txt", jacIndex.split(","));
		}
		long end=System.currentTimeMillis();
		System.out.println(counts);
		System.out.println((end-start)/1000.0+"s");
	}
}
