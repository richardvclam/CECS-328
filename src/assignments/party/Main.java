package assignments.party;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		int know = 0;
		int dontKnow = 0;
		File file = new File("/Users/rvclam/Documents/workspace/CECS-328/src/assignments/party/input.txt");
		Scanner in = new Scanner(file);
		ArrayList<String> buffer = new ArrayList<String>();
		
		know = in.nextInt();
		dontKnow = in.nextInt();
		in.nextLine();
		
		while (in.hasNextLine()) {
			buffer.add(in.nextLine());
		}
		ArrayList<Integer> invited = new ArrayList<Integer>();
		ArrayList<Integer> notInvited = new ArrayList<Integer>();
		
		//String[][] inputData = readFile("C:/Users/Richard/workspace/CECS 328/src/assignments/party/description.txt", know, dontKnow);
		
		for (int i = 0; i < buffer.size(); i++) {
			int personKnows = 0;
			int personDoesntKnow = 0;
			if (notInvited.contains(i+1)) {
				continue;
			}
			for (int j = 0; j < buffer.size(); j++) {
				if (j == i) {
					continue;
				}
				if (checkRow(buffer.get(i), i, know, dontKnow)) {
					notInvited.add(i+1);
				}
				char temp = buffer.get(i).charAt(j);
				if (temp == '0') {
					personDoesntKnow++;
				} else if (temp == '1') {
					personKnows++;
				}
			}
			
			if (personKnows >= know && personDoesntKnow >= dontKnow && !notInvited.contains(i)) {
				invited.add(i+1);
			} else {
				
			}
		}
		
		for (int i = 0; i < invited.size(); i++) {
			System.out.print(invited.get(i) + ",");
		}
		in.close();
		
	}
	
	public static boolean checkRow(String row, int rowNum, int know, int doesntKnow) {
		for (int i = 0; i < row.length(); i++) {
			int personKnows = 0;
			int personDoesntKnow = 0;

			
			if (i == rowNum) {
				continue;
			}
			char temp = row.charAt(i);
			if (temp == '0') {
				personDoesntKnow++;
			} else if (temp == '1') {
				personKnows++;
			}
			
			
			if (personKnows >= know && personDoesntKnow >= doesntKnow) {
				return true;
			}
			
		}
		return false;
	}

}
