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
            tracker[num] = tracker[num] + 1;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q4 test = new A3Q4();

        int[] array = new int[5];
        array[0] = 2;
        array[1] = 1;
        array[2] = 5;
        array[3] = 4;
        array[4] = 6;

        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
