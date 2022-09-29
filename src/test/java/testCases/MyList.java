package testCases;

import java.util.HashSet;
import java.util.Set;

public class MyList {

	public static void main(String[] args) {
		
//		List<String> myList = new ArrayList<String>();
		Set<String> myList = new HashSet<String>();
		
			
		myList.add("Section 1");
		myList.add("Section 2");
		myList.add("Section 3");
		myList.add("Section 4");
		
		myList.add("Section 5");
		myList.add("Section 6");
		
//	System.out.println(myList);
		
//		myList
//		System.out.println(myList.get(1));
//		
		Object[] result = myList.toArray();
		System.out.println(result[1]);
		for(String i : myList) {
			System.out.println(i);
		}
		
		
		
		
		
	}

}
