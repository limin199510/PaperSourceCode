package Lsh;

import java.io.Serializable;
import java.util.LinkedList;
public class Node implements Serializable {
	private static final long serialVersionUID = 4703579231229155721L;
    String content=""; //װnode�е�����
    boolean isEnd=false; //�Ƿ��ǹ�ϣֵ�Ľ�β
    String set="";  //��ͬ��ϣֵ��ʱ������ID
    LinkedList<Node> childList= new LinkedList<Node>(); //��list
    public Node(){  
        childList = new LinkedList<Node>();  
        isEnd = false;  
        content = "";  
        set = "";  
    } 
    public Node(String str){  
        childList = new LinkedList<Node>();  
        isEnd = false;  
        content = str;  
        set = "";  
    } 
    
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public LinkedList<Node> getChildList() {
		return childList;
	}

	public void setChildList(LinkedList<Node> childList) {
		this.childList = childList;
	}

	//����һ�¸ý��ĺ��ӽ�����Ƿ���������֣��о���ζ�ſ��Լ���������ȥ��û�о�û�С�
    //����еĻ��ͷ�����һ��node��û�еĻ��ͷ���null
    public Node subNode(String str){  
        if(childList != null){  
            for(Node eachChild : childList){
            	if(eachChild.content.equals(str)){  
                    return eachChild;  
                }  
            }  
        }  
        return null;  
    }  
}
