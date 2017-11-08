/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awadb3223
 */
public class A3Q4 {

    public void countingSort(int array[]) {
        int num = 0;
        int spot = 0;
        //Create tracker array 
        int[] tracker = new int[101];

        //go through input array 
        for (int i = 0; i < array.length - 1; i++) {
            //find number inside array
            array[i] = num;
            //set num to equal position of tracker array then add 1
            tracker[num] = num + 1;
            num++;
        }

        //reorganize the input array
        for (int i = 0; i < tracker.length; i++) {
            // if spot inside array greater than 0
            if(tracker[i] > 0){
                array[spot] = i;
                tracker[i]--;
                spot++;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q4 test = new A3Q4();

        int[] array = new int[11];
        System.out.println("BEFORE: ");
        for (int i = 0; i < array.length; i++) {
            //make a random number
            array[i] = (int) (Math.random() * 11);
            //sout int
            System.out.println(array[i]);
        }
        System.out.println("");
        System.out.println("");
        
        // run method
        System.out.println("AFTER: ");
        test.countingSort(array);
        
        for (int i = 0; i < 10; i++) {
            System.out.println(array[i]);
        }
       
            
        
    }
}
