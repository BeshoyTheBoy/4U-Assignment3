/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eva
 */
public class A3Q5 {

    public void swapString(String[] array, int p1, int p2) {
        String temp = array[p1];
        array[p1] = array[p2];
        array[p2] = temp;
    }

    public void selectionStringSort(String[] array) {
        // keep track of which position we are sorting
        for (int position = 0; position < array.length; position++) {
            // go through the rest looking for a smaller number
            for (int i = position + 1; i < array.length; i++) {
                // have we found a smaller string?
                int compare = array[i].compareTo(array[position]);
                //if the returned value is less than 0, that means array[i] is less than array[position]
                if (compare < 0) {
                    // swap strings
                    swapString(array, i, position);
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q5 test = new A3Q5();
        
        String[] array = new String[5];
        
        array[0] = "bun";
        array[1] = "cheese";
        array[2] = "cat";
        array[3] = "apple";
        array[4] = "promethazene";
        
        test.selectionStringSort(array);
        
        System.out.println(array);
    }
}
