package com.example.sev_user.bookmanager;

public class ConvertString {
	private char[] charA= {'à','á','ạ','ả','ã','â','ầ','ấ','ậ','ẩ','ẫ','ă','ằ','ắ','ặ','ẳ','ẵ'};
    private char[] charE={'è','é','ẹ','ẻ','ẽ','ê','ề','ế','ệ','ể','ễ'};
    private char[] charI={'ì','í','ị','ỉ','ĩ','g'}; 
    private char[] charO={'ò','ó','ọ','ỏ','õ','ô','ồ','ố','ộ','ổ','ỗ','ơ','ờ','ớ','ợ','ở','ỡ'}; 
    private char[] charU={'ù','ú','ụ','ủ','ũ','ư','ừ','ứ','ự','ử','ữ'};
    private char[] charY={'ỳ','ý','ỵ','ỷ','ỹ'}; 
    private char[] charD={'đ'};
    
	// convert method
	public String convertStirng( String input ){
		String output=input.toLowerCase();
		for(int i=0;i<input.length();i++){
			
		for(int j=0;j<charA.length;j++){
			if(output.charAt(i)==charA[j]){
				output=output.replace(charA[j], 'a');
				break;
			   }
		    }
			
		for(int j=0;j<charE.length;j++){
			if(output.charAt(i)==charE[j]){
				output=output.replace(charE[j], 'e');
				break;
			   }
		    }	
			
		for(int j=0;j<charI.length;j++){
			if(output.charAt(i)==charI[j]){
				output=output.replace(charI[j], 'i');
				break;
			   }
		    }
		
		
		for(int j=0;j<charO.length;j++){
			if(output.charAt(i)==charO[j]){
				output=output.replace(charO[j], 'o');
				break;
			   }
		    }
		
		for(int j=0;j<charU.length;j++){
			if(output.charAt(i)==charU[j]){
				output=output.replace(charU[j], 'u');
			   }
		    }
		
		for(int j=0;j<charY.length;j++){
			if(output.charAt(i)==charY[j]){
				output=output.replace(charY[j], 'y');
				break;
			   }
		    }
		
		for(int j=0;j<charD.length;j++){
			if(output.charAt(i)==charD[j]){
				output=output.replace(charD[j], 'd');
				break;
			   }
		    }
		
		}
	    
	  return output;
	}
	
}
