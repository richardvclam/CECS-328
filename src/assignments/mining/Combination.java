package assignments.mining;

import java.util.ArrayList;

public class Combination {

	public static int[] combinationUtil(ArrayList<Integer> arr, int[] data, int start, int end, int index, int r) {
		// Current combination is ready to be printed, print it
		if (index == r) {
			for (int j=0; j<r; j++) {
				System.out.print(data[j]+" ");
			}
			System.out.println("");
			return data;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i=start; i<=end && end-i+1 >= r-index; i++) {
			data[index] = arr.get(i);
			combinationUtil(arr, data, i+1, end, index+1, r);
		}
		return null;
	}

	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	public static void printCombination(ArrayList<Integer> arr, int r) {
		// A temporary array to store all combination one by one
		int data[]=new int[r];
	
		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, data, 0, arr.size()-1, 0, r);
	}
	
	public static void main (String[] args) {
		ArrayList<Integer> a = new ArrayList<Integer>();
        int arr[] = new int[20];
        for (int i = 0; i < 5; i++) {
        	a.add(i);
        }
        int r = 4;
        int n = a.size();
        printCombination(a, r);
    }
}
