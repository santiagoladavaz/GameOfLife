package edu.unq.pconc.gameoflife;

import java.applet.Applet;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import edu.unq.pconc.gameoflife.shapes.Shape;
import edu.unq.pconc.gameoflife.shapes.ShapeCollection;
import edu.unq.pconc.gameoflife.shapes.ShapeException;
import edu.unq.pconc.gameoflife.solution.GameOfLifeGrid;

/**
 * The Game Of Life Applet.
 * This is the heart of the program. It initializes everything and put it together.
 * @author Edwin Martin
 */
public class GameOfLife extends Applet implements Runnable, GameOfLifeControlsListener {
  protected CellGridCanvas gameOfLifeCanvas;
  protected GameOfLifeGrid gameOfLifeGrid;
  protected int cellSize;
  protected int cellCols;
  protected int cellRows;
  protected int genTime;
  protected GameOfLifeControls controls;
  protected static Thread gameThread = null;

  /**
   * Initialize UI.
   * @see java.applet.Applet#init()
   */
  public void init() {
    getParams();

    // set background colour
    setBackground(new Color(0x999999));

    // create gameOfLifeGrid
    gameOfLifeGrid = new GameOfLifeGrid();
    gameOfLifeGrid.resize(cellCols, cellRows);
    gameOfLifeGrid.setThreads(1);
    gameOfLifeGrid.clear();

    // create GameOfLifeCanvas
    gameOfLifeCanvas = new CellGridCanvas(gameOfLifeGrid, cellSize);

    // create GameOfLifeControls
    controls = new GameOfLifeControls();
    controls.addGameOfLifeControlsListener( this );

    // put it all together
	GridBagLayout gridbag = new GridBagLayout();
	setLayout(gridbag);
	GridBagConstraints canvasContraints = new GridBagConstraints();
	
	canvasContraints.fill = GridBagConstraints.BOTH;
	canvasContraints.gridx = GridBagConstraints.REMAINDER;
	canvasContraints.gridy = 0;
	canvasContraints.weightx = 1;
	canvasContraints.weighty = 1;
	canvasContraints.anchor = GridBagConstraints.CENTER;
	gridbag.setConstraints(gameOfLifeCanvas, canvasContraints);
	add(gameOfLifeCanvas);
	
	GridBagConstraints controlsContraints = new GridBagConstraints();
	canvasContraints.gridy = 1;
	canvasContraints.gridx = 0;
	controlsContraints.gridx = GridBagConstraints.REMAINDER;
	gridbag.setConstraints(controls, controlsContraints);
	add(controls);
    
    try {
      setShape( ShapeCollection.getShapeByName( "Glider" ) );
    } catch (ShapeException e) {
      // Ignore. Not going to happen.
    }
    setVisible(true);
    validate();
  }
  
  /**
   * Get params (cellSize, cellCols, cellRows, genTime) from applet-tag
   */
  protected void getParams() {
    cellSize = getParamInteger( "cellsize", 11 );
    cellCols = getParamInteger( "cellcols", 60 );
    cellRows = getParamInteger( "cellrows", 40 );
    genTime  = getParamInteger( "gentime", 500 );
  }
  
  /**
   * Read applet parameter (int) or, when unavailable, get default value.
   * @param name name of parameter
   * @param defaultParam default when parameter is unavailable
   * @return value of parameter
   */
  protected int getParamInteger( String name, int defaultParam ) {
    String param;
    int paramInt;

    param = getParameter( name );
    if ( param == null )
      paramInt = defaultParam;
    else
      paramInt = Integer.valueOf(param).intValue();
    return paramInt;
  }

  /**
   * Starts creating new generations.
   * No start() to prevent starting immediately.
   */
  public synchronized void start2() {
    controls.start();
    if (gameThread == null) {
      gameThread = new Thread(this);
      gameThread.start();
    }
  }

  /**
   * @see java.applet.Applet#stop()
   */
  public void stop() {
    controls.stop();
    gameThread = null;
  }

  /**
   * @see java.lang.Runnable#run()
   */
  public synchronized void run() {
    while (gameThread != null) {
      nextGeneration();
      try {
        Thread.sleep(genTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Is the applet running?
   * @return true: applet is running
   */
  public boolean isRunning() {
    return gameThread != null;
  }
  
  /**
   * Go to the next generation.
   */
  public void nextGeneration() {
    gameOfLifeGrid.next();
    gameOfLifeCanvas.repaint();
    showGenerations();
  }
  
  /**
   * Set the new shape
   * @param shape name of shape
   */
  public void setShape( Shape shape ) {
    if ( shape == null )
      return;

    try {
      gameOfLifeCanvas.setShape( shape );
      reset();
    } catch (ShapeException e) {
      alert( e.getMessage() );
    }
  }
  
  public void setThreads(int threads) {
	  gameOfLifeGrid.setThreads(threads);
  }
  
  /**
   * Resets applet (after loading new shape)
   */
  public void reset() {
    stop(); // might otherwise confuse user
    gameOfLifeCanvas.repaint();
    showGenerations();
    showStatus( "" );
  }

  /**
   * Show number of generations.
   */
  private void showGenerations() {
    controls.setGeneration( gameOfLifeGrid.getGenerations() );
  }
  
  /**
   * Set speed of new generations.
   * @param fps generations per second
   */
  public void setSpeed( int fps ) {
    genTime = fps;
  }
  
  /**
   * Sets cell size.
   * @param p size of cell in pixels
   */
  public void setCellSize( int p ) {
    cellSize = p;
    gameOfLifeCanvas.setCellSize( cellSize );
  }
  
  /**
   * Gets cell size.
   * @return size of cell
   */
  public int getCellSize() {
    return cellSize;
  }
  
  /**
   * Shows an alert
   * @param s text to show
   */
  public void alert( String s ) {
    showStatus( s );
  }

  /** Callback from GameOfLifeControlsListener
   * @see edu.unq.pconc.gameoflife.GameOfLifeControlsListener#startStopButtonClicked(edu.unq.pconc.gameoflife.GameOfLifeControlsEvent)
   */
  public void startStopButtonClicked( GameOfLifeControlsEvent e ) {
    if ( isRunning() ) {
      stop();
    } else {
      start2();
    }
  }

  /** Callback from GameOfLifeControlsListener
   * @see edu.unq.pconc.gameoflife.GameOfLifeControlsListener#nextButtonClicked(edu.unq.pconc.gameoflife.GameOfLifeControlsEvent)
   */
  public void nextButtonClicked(GameOfLifeControlsEvent e) {
    nextGeneration();
  }

  /** Callback from GameOfLifeControlsListener
   * @see edu.unq.pconc.gameoflife.GameOfLifeControlsListener#speedChanged(edu.unq.pconc.gameoflife.GameOfLifeControlsEvent)
   */
  public void speedChanged(GameOfLifeControlsEvent e) {
    setSpeed( e.getSpeed() );
  }

  /** Callback from GameOfLifeControlsListener
   * @see edu.unq.pconc.gameoflife.GameOfLifeControlsListener#speedChanged(edu.unq.pconc.gameoflife.GameOfLifeControlsEvent)
   */
  public void zoomChanged(GameOfLifeControlsEvent e) {
    setCellSize( e.getZoom() );
  }

  /** Callback from GameOfLifeControlsListener
   * @see edu.unq.pconc.gameoflife.GameOfLifeControlsListener#shapeSelected(edu.unq.pconc.gameoflife.GameOfLifeControlsEvent)
   */
  public void shapeSelected(GameOfLifeControlsEvent e) {
    String shapeName = (String) e.getShapeName();
    Shape shape;
    try {
      shape = ShapeCollection.getShapeByName( shapeName );
      setShape( shape );
    } catch (ShapeException e1) {
      // Ignore. Not going to happen.
    }
  }
  
  public void threadsChanged(GameOfLifeControlsEvent e) {
	  setThreads(e.getThreads());
  }

}

