package edu.unq.pconc.gameoflife;

import java.awt.Dimension;

/** Interface to interact with a grid of cells. */
public interface CellGrid {
	
  /** Gets the status of a cell (dead or alive).
   *  @param col x-position.
   *  @param row y-position.
   *  @return living or not. */
  public boolean getCell( int col, int row );

  /** Sets the status of a cell (dead or alive).
   *  @param col x-position.
   *  @param row y-position.
   *  @param cell living or not. */
  public void setCell( int col, int row, boolean cell );

  /** Gets the dimension of the grid.
   *  @return a Dimension object. */
  public Dimension getDimension();
  
  /** Resize the cell grid.
   *  @param col new number of columns.
   *  @param row new number of rows. */
  public void resize( int col, int row );

  /** Clears grid. */
  public void clear();
  
  /** Sets the number of threads to be used by the simulation. */
  public void setThreads(int threads);
  
  /** Gets the number of generations since initialization.
   *  @return number of generations. */
  public int getGenerations();
  
  /** Creates the next generation from the current grid state. */
  public void next();
  
}
