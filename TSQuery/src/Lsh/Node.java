package Lsh;

import java.io.Serializable;
import java.util.LinkedList;
public class Node implements Serializable {
	private static final long serialVersionUID = 4703579231229155721L;
    String content=""; //装node中的内容
    boolean isEnd=false; //是否是哈希值的结尾
    String set="";  //相同哈希值的时间序列ID
    LinkedList<Node> childList= new LinkedList<Node>(); //子list
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

	//遍历一下该结点的孩子结点中是否有这个数字，有就意味着可以继续查找下去，没有就没有。
    //如果有的话就返回下一个node，没有的话就返回null
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
