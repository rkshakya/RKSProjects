package pack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Euler12 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 1){
			System.out.println("Wrong usage");		
			return;
		}
		
		String inFile = args[0];
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(inFile)));
			String line = null;				
			
			while ((line = br.readLine()) != null) {
				line = line.trim();	

				if(line.length() != 0) {
					break;
				}
			}
			
			br.close();
			br = null;
			
			boolean flag = false;
			int count = 1;
			int num = 0;
			int rem = -1;
			ArrayList<Integer> factors = new ArrayList<Integer>(50);
			
			while(!flag){
				//generate triangle numbers
				for(int itr = 1; itr <= count; itr++){
					num += itr;
				}
				
				//get the factors of the triangle numbers
				for (int cnt = 1; cnt <= num; cnt++){
			        rem = num % cnt;
			        if (rem == 0) {
				        factors.add(cnt);
					}
			    }
				
				//check the condition
				if(factors.size() > Integer.parseInt(line)){
					System.out.println(num);
					flag = true;
				}
				
				count++;
				
				//reset vars
				num = 0;
				rem = -1;
				factors.clear();
			}
			
		}catch(Exception e){
			e.getMessage();
		}

	}

}
