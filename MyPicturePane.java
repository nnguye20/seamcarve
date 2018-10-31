package seamcarve;

import javafx.scene.layout.BorderPane;
import support.seamcarve.*;

/**
 * This class is your seam carving picture pane. It is a subclass of
 * PicturePane, an abstract class that takes care of all the drawing,
 * displaying, carving, and updating of seams and images for you. Your job is to
 * override the abstract method of PicturePanel that actually finds the lowest
 * cost seam through the image.
 * 
 * See method comments and handouts for specifics on the steps of the seam
 * carving algorithm.
 *
 * 
 * @version 01/07/2017
 */

public class MyPicturePane extends PicturePane {

    /**
     * The constructor accepts an image filename as a String and passes it to
     * the superclass for displaying and manipulation.
     * 
     * @param pane
     * @param filename
     */
    public MyPicturePane(BorderPane pane, String filename) {
        super(pane, filename);

    }

    /**
     * This method returns the lowest cost seam for a given picture. It
     * retrieves importance values for each pixel via getImportanceValues, and
     * uses such values to create both a costs and directions array. Then, it
     * finds the best seam by starting at the lowest costing pixel in the top
     * row, and tracing downwards based on values in the directions array. The x
     * coordinate of the seam for each row is stored in a bestSeam array, which
     * is returned at the end.
     */
    protected int[] findLowestCostSeam() {

        // Cleanliness/convenience--picHeight does not change
        int picHeight = this.getPicHeight();
        int picWidth = this.getPicWidth();

        int[][] costs = new int[picHeight][picWidth];
        int[][] dirs = new int[picHeight][picWidth];
        // Convenience
        int[][] vals = this.getImportanceValues(picHeight, picWidth);
        int[] bestSeam = new int[picHeight];

        this.fillCostsAndDirs(costs, dirs, vals, picHeight, picWidth);

        // Initialize the first column of the seam
        bestSeam[0] = this.argmin(costs[0]);

        // Create vertical seam represented by column coordinates
        for (int row = 0; row < picHeight - 1; row++) {
            bestSeam[row + 1] = bestSeam[row] + dirs[row][bestSeam[row]];
        }

        return bestSeam;
    }

    /**
     * This private method takes in an array of importance values and fills both
     * the costs and directions array based on such values. For each pixel, it
     * finds the lowest cost from the bottom of the picture to such pixel by
     * comparing the costs of 3 adjacent pixels (leftDown, middleDown,
     * rightDown). The respective direction of the minimum costing adjacent
     * pixel is stored in the direction array.
     */
    private void fillCostsAndDirs(int[][] costs, int[][] dirs, int[][] vals,
            int picHeight, int picWidth) {

        // Initialize bottom row of costs
        costs[picHeight - 1] = vals[picHeight - 1];

        // From the second to bottom row to the first row
        for (int row = (picHeight - 2); row >= 0; row--) {
            // From the rightmost column to the leftmost
            for (int col = (picWidth - 1); col >= 0; col--) {
                // Initialize variables
                int leftDownCost = 0;
                int middleDownCost = costs[row + 1][col];
                int rightDownCost = 0;
                int minimumCost = 0;
                int direction = 0;

                // If the left down pixel is out of bounds
                if ((col - 1) < 0) {
                    // Invalid pixel will never be evaluated as having min cost
                    leftDownCost = 999999999;
                } else {
                    // Set as actual cost
                    leftDownCost = costs[row + 1][col - 1];
                }

                // Same for the right down pixel
                if ((col + 1) >= picWidth) {
                    rightDownCost = 999999999;
                } else {
                    rightDownCost = costs[row + 1][col + 1];
                }

                // Convenience variable
                minimumCost = Math.min(Math.min(leftDownCost, middleDownCost),
                        rightDownCost);
                // Add minimum cost to immediate pixel importance value and
                // store as total cost
                costs[row][col] = vals[row][col] + minimumCost;

                // If the minimum cost was from the leftDown pixel
                if (minimumCost == leftDownCost) {
                    // Set direction as left
                    direction = -1;
                } else if (minimumCost == middleDownCost) {
                    direction = 0;
                } else if (minimumCost == rightDownCost) {
                    direction = 1;
                }

                dirs[row][col] = direction;
            }

        }

    }

    /**
     * This method takes in an array of integers and returns the index of
     * minimum value.
     */
    private int argmin(int[] array) {

        // Initialize
        int lowestCost = array[0];
        int lowestCostIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < lowestCost) {
                lowestCost = array[i];
                lowestCostIndex = i;
            }

        }
        return lowestCostIndex;

    }

    /**
     * This method calculates the importance values for each pixel in a given
     * picture. To find the importance value, it compares the RGB values of the
     * current pixel and all adjacent pixels (excluding diagonal). The magnitude
     * of the difference is stored as the current pixel's importance value.
     * 
     */
    private int[][] getImportanceValues(int height, int width) {

        // convenience variables
        int picHeight = height;
        int picWidth = width;

        int[][] importanceArray = new int[picHeight][picWidth];

        // For each row
        for (int i = 0; i < picHeight; i++) {
            // For each column
            for (int j = 0; j < picWidth; j++) {

                // Initialize
                int importanceValue = 0;
                int adjacentPixelCount = 0;

                // Calculate RGB values of current pixel
                int currentPixelRed = this.getColorRed(getPixelColor(i, j));
                int currentPixelGreen = this.getColorGreen(getPixelColor(i, j));
                int currentPixelBlue = this.getColorBlue(getPixelColor(i, j));

                // For each of the four possible adjacent pixels
                for (int k = 0; k < Constants.ADJACENT_INDICES.length; k++) {
                    // Convenience variables
                    int adjacentPixelRow = i + Constants.ADJACENT_INDICES[k][0];
                    int adjacentPixelCol = j + Constants.ADJACENT_INDICES[k][1];

                    // If such adjacent pixel is not outside the picture
                    if ((adjacentPixelRow >= 0) && (adjacentPixelCol >= 0)
                            && (adjacentPixelRow < picHeight)
                            && (adjacentPixelCol < picWidth)) {
                        // Increment count
                        adjacentPixelCount += 1;

                        // Readability; retrieve RGB values of adjacent pixel
                        int adjacentPixelRed = this.getColorRed(getPixelColor(
                                adjacentPixelRow, adjacentPixelCol));
                        int adjacentPixelGreen = this
                                .getColorGreen(getPixelColor(adjacentPixelRow,
                                        adjacentPixelCol));
                        int adjacentPixelBlue = this
                                .getColorBlue(getPixelColor(adjacentPixelRow,
                                        adjacentPixelCol));

                        // Add the absolute values of the difference in Red,
                        // Green, and Blue between the current pixel and given
                        // adjacent pixel. Divide by adjacent pixel count to get
                        // average difference.
                        importanceValue += ((Math.abs(currentPixelRed
                                - adjacentPixelRed)
                                + Math.abs(currentPixelGreen
                                        - adjacentPixelGreen) + Math
                                    .abs(currentPixelBlue - adjacentPixelBlue)))
                                / adjacentPixelCount;

                    }

                    // Store importance value of current pixel in array
                    importanceArray[i][j] = importanceValue;

                }

            }
        }

        return importanceArray;
    }

}
