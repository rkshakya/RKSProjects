import java.io.*;
import java.util.*;

class Node{
	int data;
	Node next;
	Node(int d){
        data=d;
        next=null;
    }
	
}

class Day15
{
	public static  Node insert(Node head,int data)
	{
  	//Complete this method
        Node temp = new Node(data);
        Node current = head;
        if(current == null){
           head = temp;
        } else{
          while(current.next != null){
            current = current.next;
          }
          current.next = temp;
        }
        return head;
	}

	public static void display(Node head)
    {
          Node start=head;
          while(start!=null)
          {
              System.out.print(start.data+" ");
              start=start.next;
          }
    }
    public static void main(String args[])
    {
          Scanner sc=new Scanner(System.in);
          Node head=null;
          int T=sc.nextInt();
          while(T-->0){
              int ele=sc.nextInt();
              head=insert(head,ele);
          }
          display(head);

   }
}