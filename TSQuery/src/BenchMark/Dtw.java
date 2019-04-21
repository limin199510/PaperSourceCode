package BenchMark;

public class Dtw {
	static String path="";
	public double cal(double data1[],double data2[]) {
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
	public static String find(int i,int j,double output[][]) {
		int p1,p2;
		/*if(i-1<0&&j-1<0){
			path+="0,0";
			return path;
		}*/
		if(i-1<0) {
			while(j-1>0) {
				path+="0,"+(j-1)+"\t";
				j--;
			}
			return path;
		}
		if(j-1<0) {
			while(i-1>0) {
				path+=(i-1)+",0\t";
				i--;
			}
			return path;
		}
		if(output[i-1][j]<output[i][j-1]){
			if(output[i-1][j]<output[i-1][j-1]){
				p1=i-1;p2=j;
			}
			else {
				p1=i-1;p2=j-1;
			}
				
		}else {
			if(output[i][j-1]<output[i-1][j-1]) {
				p1=i;p2=j-1;
			}
			else {
				p1=i-1;p2=j-1;
			}
		}
		path+=p1+","+p2+"\t";
		find(p1,p2,output);
		return path;	
	}
	public static void main(String[] args) {
		double []d1={8,9,1};
		double []d2={2,5,4,6};
		Dtw dt=new Dtw();
		double ca=dt.cal(d1, d2);
		System.out.println(ca);
	}

}
