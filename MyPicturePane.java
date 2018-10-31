package seamcarve;

import javafx.scene.layout.BorderPane;
import support.seamcarve.*;


/** 
 * This class is your seam carving picture pane.  It is a subclass of PicturePane,
 * that takes care of all the drawing, displaying, carving, and
 * updating of seams and images. 

 * 
 *
 * 
 * @version 01/07/2017
 */

public class MyPicturePane extends PicturePane {
	

	
	/**
	 * The constructor accepts an image filename as a String and passes
	 * it to the superclass for displaying and manipulation.
	 * 
	 * @param pane
	 * @param filename
	 */
	public MyPicturePane(BorderPane pane, String filename) {
		super(pane, filename);

	}

	
	/**
	 * This method returns an array of ints that represents a seam.  This size of this array
	 * is the height of the image.  Each entry of the seam array corresponds to one row of the 
	 * image.  The data in each entry should be the x coordinate of the seam in this row.  
	 * For example, given the below "image" where s is a seam pixel and - is a non-seam pixel
	 * 
	 * - s - -  
	 * s - - -  
	 * - s - -  
	 * - - s -  
	 * 
	 * 
	 * the following code will properly return a seam:
	 * 
	 * int[] currSeam = new int[4];
	 * currSeam[0] = 1;
	 * currSeam[1] = 0;
	 * currSeam[2] = 1;
	 * currSeam[3] = 2;
	 * return currSeam;
	 * 
 	 */
	protected int[] findLowestCostSeam() {
		// TODO: Your code here
		return null;
	}

}
