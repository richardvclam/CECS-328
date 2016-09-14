package assignments.party.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Party {

	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		int know = 0;
		int dontKnow = 0;
		//File file = new File("/Users/rvclam/Documents/workspace/CECS-328/src/assignments/party/input.txt");
		File file = new File("C:/Users/Richard/workspace/CECS 328/src/assignments/party/description_1.txt");
		Scanner in = new Scanner(file);
		ArrayList<String> buffer = new ArrayList<String>();
		
		know = in.nextInt();
		dontKnow = in.nextInt();
		in.nextLine();
		
		while (in.hasNextLine()) {
			buffer.add(in.nextLine());
		}
		
		int size = buffer.size();
		System.out.println("Number of people: " + size);
		ArrayList<Integer> invited = new ArrayList<Integer>();
		ArrayList<Integer> notInvited = new ArrayList<Integer>();
		int lastSize = notInvited.size();
		int currentSize = -1;
		
		do {
			lastSize = notInvited.size();
			for (int i = 0; i < size; i++) {
				int personKnows = 0;
				int personDoesntKnow = 0;
				if (notInvited.contains(i+1)) {
					continue;
				}
				for (int j = 0; j < size; j++) {
					if (i == j || notInvited.contains(j+1)) {
						continue;
					}
					char temp = buffer.get(i).charAt(j);
					if (temp == '0') {
						personDoesntKnow++;
					} else if (temp == '1') {
						personKnows++;
					}
				}
				
				if (personKnows < know || personDoesntKnow < dontKnow) {
					notInvited.add(i+1);
				} 
			}
			currentSize = notInvited.size();
		} while (currentSize != lastSize);
		
		for (int i = 0; i < size; i++) {
			if (!notInvited.contains(i+1)) {
				invited.add(i+1);
			}
		}
		
		Collections.sort(invited);
		Collections.sort(notInvited);
		
		PrintWriter writer = new PrintWriter("src/assignments/party/party.txt");
		
		System.out.println("Invited: ");
		for (int i = 0; i < invited.size(); i++) {
			if (i != 0) {
				System.out.print(",");
			}
			System.out.print(invited.get(i));
			writer.println(invited.get(i));
		}
		
		System.out.println();
		System.out.println("Not Invited: ");
		for (int i = 0; i < notInvited.size(); i++) {
			if (i != 0) {
				System.out.print(",");
			}
			System.out.print(notInvited.get(i));
		}
		
		
		in.close();
		writer.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}

}
