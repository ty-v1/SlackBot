package util;

import java.util.Arrays;
import java.util.List;

public class ParseUtil {

	public static List<String> parseStringToList(String string){
		return Arrays.asList(string.split(","));
	}
	
}
