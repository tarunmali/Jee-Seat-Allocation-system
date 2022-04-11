package project;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.plaf.synth.SynthSpinnerUI;

class Candidate{
	int air; 
	int category;// 0 = general, 1 = obc, 2 = sc, 3 = st
	int[] seatPrefList;
}

class Seat{
	int Id;
	int category;// 0 = general, 1 = obc, 2 = sc, 3 = st
	int[] candidatePrefList;
}


public class Jee {
	
	static int noOfCandidates = 50;
	static int noOfColleges = 5;
	static Seat[] seats = new Seat[5*10];
	static Candidate[] candidates = new Candidate[noOfCandidates];
	static Scanner sc = new Scanner(System.in);	

	public static void main(String[] args) {
		candidatesSetUP();
		seatsSetUP();	

		int[][] input = mergePreferenceList();
		
		int[] res = seatAllocation(input);
		output(res);
		
	}
	
	
	/**
	 * Stable Marriage/ Gale-Shipley algorithm
	 * @param input int[2N][N] array with first N rows as candidate's seat preference list, and last N rows as seat's candidate preference list
	 * @return int[N] array with indexes as seats and data as air of candidates
	 */
	private static int[] seatAllocation(int[][] input) {
	    int wPartner[] = new int[seats.length];	 
	    boolean mFree[] = new boolean[seats.length];
	 
	    Arrays.fill(wPartner, -1);
	    int freeCount = seats.length;
	 
	    while (freeCount > 0) {
	        int m;
	        for (m = 0; m < seats.length; m++)
	            if (mFree[m] == false)
	                break; 
	        for (int i = 0; i < seats.length && mFree[m] == false; i++)  {
	            int w = input[m][i];
	 
	            if (wPartner[w - seats.length] == -1) {
	                wPartner[w - seats.length] = m;
	                mFree[m] = true;
	                freeCount--;
	            } 
	            else {
	                int m1 = wPartner[w - seats.length];
	 
	                if (wPrefersM1OverM(input, w, m, m1) == false) {
	                    wPartner[w - seats.length] = m;
	                    mFree[m] = true;
	                    mFree[m1] = false;
	                }
	            }
	        }
	    }
	    return wPartner;
	}
	
	
	/**
	 * Utility function for gale-shipley algorithm
	 * @param input 2d array on which gale-shipley is working on
	 * @param w the specific seat in question
	 * @param m candidate 1
	 * @param m1 candidate 2
	 * @return boolean whether seat prefers one candidate over other
	 */
	private static boolean wPrefersM1OverM(int[][] input, int w, int m, int m1) {
	    for (int i = 0; i < seats.length; i++) {
	        if (input[w][i] == m1)
	            return true;
	        if (input[w][i] == m)
	        return false;
	    }
	    return false;
	}

	
	/**
	 * merges two preference list to make input list for gale-shipley algorithm
	 * @return int[2N][N] array with first N rows as candidate's seat preference list, and last N rows as seat's candidate preference list
	 */
	private static int[][] mergePreferenceList() {
		int[][] arr = new int[noOfCandidates + seats.length][seats.length];
		for(int i = 0; i < noOfCandidates; i++) {
			for(int j = 0; j < seats.length; j++) {
				arr[i][j] = candidates[i].seatPrefList[j];
			}
		}
		for(int i = 0; i < seats.length; i++) {
			for(int j = 0; j < seats.length; j++) {
				arr[i+noOfCandidates][j] = seats[i].candidatePrefList[j];
			}
		}
		return arr;
	}
	
	/**
	 * displays output in AIR ----- college manner
	 * @param arr int[N] array with indexes as seats and data as air of candidates, output from gale-shipley algorithm
	 */
	private static void output(int[] arr) {
		System.out.println("AIR       College");
		for(int i = 0; i < arr.length; i++) {
			int index = search(arr, i);
			int cllgId = (index+1) / 10;
			if(i < 9) {
				System.out.println(i+1 + "            " + (cllgId+1));
			}
			else System.out.println(i+1 + "           " + (cllgId+1));
		}
	}
	
	
	/**
	 * Making each candidates seats preference list
	 */
	private static void candidatesSetUP() {	
		//giving random category to candidates
		for(int i = 0; i < candidates.length; i++) {
			candidates[i] = new Candidate();
			//candidates[i].air = i+1;
			candidates[i].air = i;	
			candidates[i].seatPrefList = new int[seats.length];
			
			Random random = new Random();   
			int buffcat = random.nextInt(4);
			candidates[i].category = buffcat;

			
		}
		


		int noOfInput;
		System.out.println("Enter the number of candidates you want to enter preference list manually\nFor the rest, the preference list will be genrated automatically");
		System.out.print("=> ");		
		noOfInput = sc.nextInt();
	
		input(noOfInput);//buliding candidate preference through input	
		buildrandom(noOfInput);//building candidate preference randomly	
	}
	
	
	/**
	 * Setting up seats[] and seats's candidate preference list
	 */
	private static void seatsSetUP() {		
		for(int i = 0; i < seats.length; i++) {	
			seats[i] = new Seat();
			seats[i].Id = noOfCandidates + i;
			
			//40% general, 30% obc, 20% sc, 10% st seats
			if((i>=0 && i<4) || (i>=10 && i<14) || (i>=20 && i<24) || (i>=30 && i<34) || (i>=40 && i<44)) {
				seats[i].category = 0;
			}
			else if((i>=4 && i<7) || (i>=14 && i<17)  || (i>=24 && i<27)  || (i>=34 && i<37)  || (i>=44 && i<47)) {
				seats[i].category = 1;
			}
			else if((i>=7 && i<9) || (i>=17 && i<19) || (i>=27 && i<29) || (i>=37 && i<39) || (i>=47 && i<49)) {
				seats[i].category = 2;
			}
			else if(i==9 || i==19 || i==29 || i==39 || i==49) {
				seats[i].category = 3;
			}		
		}	
		
		for(int i = 0; i < seats.length; i++) {			
			seats[i].candidatePrefList = new int[seats.length];
			
			if(seats[i].category == 0) {
				for(int j = 0; j < seats[i].candidatePrefList.length; j++) { 
					seats[i].candidatePrefList[j] = j;
				}	
			}
			else if(seats[i].category == 1) {
				int count = 0;
				for(int j = 0; j < noOfCandidates; j++) {
					if(candidates[j].category == 1) {
						seats[i].candidatePrefList[count] = candidates[j].air;
						count++;
					}
					else continue;
				}
				int count2 = 0;
				for(int j = 0; j < noOfCandidates; j++) {
					if(count+count2 >= seats.length) break;
					if(candidates[j].category == 1) continue;
					else {
						seats[i].candidatePrefList[count+count2] = candidates[j].air; 
						count2++;
					}
				}
			}
			else if(seats[i].category == 2) {
				int count = 0;
				for(int j = 0; j < noOfCandidates; j++) {
					if(candidates[j].category == 2) {
						seats[i].candidatePrefList[count] = candidates[j].air;
						count++;
					}
					else continue;
				}
				int count2 = 0;
				for(int j = 0; j < noOfCandidates; j++) {
					if(count+count2 >= seats.length) break;
					if(candidates[j].category == 2) continue;
					else {
						seats[i].candidatePrefList[count+count2] = candidates[j].air;
						count2++;
					}
				}
			}		
			else {
				int count = 0;
				for(int j = 0; j < noOfCandidates; j++) {
					if(candidates[j].category == 3) {
						seats[i].candidatePrefList[count] = candidates[j].air;
						count++;
					}
					else continue;
				}
				int count2 = 0;
				for(int j = 0; j < noOfCandidates; j++) {
					if(count+count2 >= seats.length) break;
					if(candidates[j].category == 3) continue;
					else {
						seats[i].candidatePrefList[count+count2] = candidates[j].air;
						count2++;
					}
				}
			}
		}			
	}
	
	
	/**
	 * Gets college choice list from User
	 * @param noOfInput number of times scanner asks for input
	 */
	private static void input(int noOfInput) {
		for(int j=0; j < noOfInput; j++) {		
			System.out.println("Enter the order in which you want to choose the colleges for the candidate " + (j+1));
			System.out.println("1 for IIIt Vadodara");
            System.out.println("2 for IIIT Delhi");
            System.out.println("3 for IIITV ICD");
            System.out.println("4 for IIIT Allahabad");
            System.out.println("5 for MNNIT Allahabad");

            int[] choices = new int[5];
            for(int i = 0; i < choices.length; i++) {               
            	choices[i] = sc.nextInt();
            }
            for(int i = 0; i < choices.length; i++) {
            	build(choices[i],i,j);
            }
		}      
	}
	
	
	/**
	 * Builds Array with random choices using genRandom()
	 * @param noOfInput number of times scanner asks for input
	 */
	private static void buildrandom(int noOfInput) {
		for(int l = noOfInput; l < noOfCandidates; l++) {		
            int[] choices = genRandom(1, 5);
            for(int i = 0; i < choices.length; i++) {
                build(choices[i],i,l);
            }
        }



	}
	
	
	/**
	 * Generates random array of size numbersNeeded with data in range(init, init+numbersNeeded)
	 * @param init initial key, such that data_in_arr >init
	 * @param numbersNeeded size of the array
	 * @return
	 */
	private static int[] genRandom(int init, int numbersNeeded) {
		Random rng = new Random();

		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < numbersNeeded){
		    Integer next = rng.nextInt(numbersNeeded);

		    generated.add(init + next);
		}
		Object[] arr = generated.toArray();
		int[] res = new int[arr.length];
		for(int i = 0; i < arr.length; i++) {
			res[i] = (int) arr[i];
		}
		return res;
	}
	
	/**
	 * Building the preference array of colleges of students, it puts the 10 college seats in candidates's preference list
	 * @param x
	 * @param time
	 * @param idxCand
	 */
	private static void build(int x, int time, int idxCand) {
		int y = x * 10 + 40;
		for(int i=time*10, j=y, k=0; k<10; j++, k++) {
			candidates[idxCand].seatPrefList[i+k]=j;
		}
		


	}
		
	/**
	 * linear search for a key
	 * @param arr 
	 * @param key
	 * @return index where the key is found in arr
	 */
	private static int search(int[] arr, int key){    
        for(int i=0;i<arr.length;i++){    
            if(arr[i] == key){    
                return i;    
            }    
        }    
        return -1;    
    }    
	
	
	@SuppressWarnings("unused")
	private static void printArray(int[] array) {
		for(int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
	}	
	
	@SuppressWarnings("unused")
	private static void print2DArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++)
                System.out.print(array[i][j] + " ");
            System.out.println();
        }  
	}
	
}
