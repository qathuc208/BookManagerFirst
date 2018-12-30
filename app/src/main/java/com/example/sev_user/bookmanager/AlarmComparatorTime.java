package com.example.sev_user.bookmanager;

import java.util.Comparator;
import database.Alarm;

public class AlarmComparatorTime implements Comparator<Alarm>{
	@Override
	public int compare(Alarm a1, Alarm a2) {
		// TODO Auto-generated method stub
		int check=1;
		String[] a1_time=a1.getmTime().split(":");
		String[] a2_time=a2.getmTime().split(":"); 
		//so sanh;
		if(a1_time[0]==a2_time[0]){
			if(a1_time[1]==a2_time[1]){
				check=Integer.parseInt(a1_time[2])>Integer.parseInt(a2_time[2])?1:-1;
				
			}else{
				check=Integer.parseInt(a1_time[1])>Integer.parseInt(a2_time[1])?1:-1;
			}
			
			
		}else{
			check=Integer.parseInt(a1_time[0])>Integer.parseInt(a2_time[0])?1:-1;
		}
		
		return check;
	}

}
