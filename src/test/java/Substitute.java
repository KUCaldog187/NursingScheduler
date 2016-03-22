import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Substitute{
	
	public static void main(String[] args) {
		String x = "TRADINGFEW";
		String y = "LGXWEV";
		
		System.out.println(getValue(x, y));
	}
 	
    private static final int KEY_LENGTH = 10; 
    private static final String DEFAULT_STRING_VALUE = ""; 
    private static final int ERROR_VALUE = -1;
    
    /**
     * @param key
     * @param code
     * @return
     */
    public static int getValue(String key, String code){
    	int intValue = ERROR_VALUE;
        String stringValue = DEFAULT_STRING_VALUE;
       
        if(key.length() != KEY_LENGTH){
        	return ERROR_VALUE;    
        }
        
        LinkedHashMap<Integer,Character> linkedHashMap = new LinkedHashMap<Integer,Character>(10);
        
		for(int i=0; i<key.length(); i++){
            	if(i == key.length()-1){
                    linkedHashMap.put(0,key.charAt(i));
                }else{
                    linkedHashMap.put(i+1,key.charAt(i));
                }
        	    
       	}
        
        
        for(int i=0; i<code.length(); i++){
            if(linkedHashMap.containsValue(code.charAt(i))){
                for (Iterator<Entry<Integer, Character>> iterator = linkedHashMap.entrySet().iterator(); iterator.hasNext();) {
					Entry<Integer, Character> entry = (Entry<Integer, Character>)iterator.next();
					if(entry.getValue() == code.charAt(i)){
                    	stringValue +=entry.getKey();
                        break;
                    }
				}
            }
       	}
        intValue = Integer.parseInt(stringValue);
        return intValue;
    
    }
    
}