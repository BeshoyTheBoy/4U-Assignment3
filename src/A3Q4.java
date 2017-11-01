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
        
        // testing tracker array
        for (int i = 0; i < tracker.length - 1; i++) {
            System.out.println(tracker[i]);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q4 test = new A3Q4();

        int[] array = new int[101];
        for (int i = 0; i < array.length; i++) {
            //make a random number
            array[i] = (int) (Math.random() * 101);
        }
        
        test.countingSort(array);
    }
}
