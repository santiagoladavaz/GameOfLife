package edu.unq.pconc.gameoflife;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import edu.unq.pconc.gameoflife.solution.GameOfLifeGrid;

/**
 * Turns GameOfLife applet into application.
 * It adds a menu, a window, drag-n-drop etc.
 * It can be run stand alone.
 * 
 * @author Edwin Martin
 */
public class StandaloneGameOfLife extends GameOfLife {
	
  private Frame appletFrame;
  private String[] args;
  
  /**
   * main() for standalone version.
   * @param args Not used.
   */
  public static void main(String args[]) {
    StandaloneGameOfLife gameOfLife = new StandaloneGameOfLife();
    gameOfLife.args = args;
    new AppletFrame( "Game of Life", gameOfLife );
  }

  /**
   * Initialize UI.
   * @param parent Parent frame.
   * @see java.applet.Applet#init()
   */
  public void init( Frame parent ) {
    appletFrame = parent;
    getParams();

    // set background colour
    setBackground(new Color(0x999999));

    // create StandAloneGameOfLifeGrid
    gameOfLifeGrid = new GameOfLifeGrid();
    gameOfLifeGrid.resize(cellCols, cellRows);
    gameOfLifeGrid.setThreads(1);
    //gridIO = new GameOfLifeGridIO( gameOfLifeGrid );

    // create GameOfLifeCanvas
    gameOfLifeCanvas = new CellGridCanvas(gameOfLifeGrid, cellSize);

    // create GameOfLifeControls
    controls = new GameOfLifeControls();
    controls.addGameOfLifeControlsListener( this );

    // put it all together
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints canvasContraints = new GridBagConstraints();
	setLayout(gridbag);
	canvasContraints.fill = GridBagConstraints.BOTH;
	canvasContraints.weightx = 1;
	canvasContraints.weighty = 1;
	canvasContraints.gridx = GridBagConstraints.REMAINDER;
	canvasContraints.gridy = 0;
	canvasContraints.anchor = GridBagConstraints.CENTER;
	gridbag.setConstraints(gameOfLifeCanvas, canvasContraints);
	add(gameOfLifeCanvas);
	GridBagConstraints controlsContraints = new GridBagConstraints();
	canvasContraints.gridx = GridBagConstraints.REMAINDER;
	canvasContraints.gridy = 1;
	controlsContraints.gridx = GridBagConstraints.REMAINDER;
	gridbag.setConstraints(controls, controlsContraints);
	add(controls);
	setVisible(true);
	validate();
  }

  /**
   * Override method, called by applet.
   * @see java.applet.Applet#getParameter(java.lang.String)
   */
  public String getParameter( String parm ) {
        return System.getProperty( parm );
    }

  /**
   * Do not use showStatus() of the applet.
   * @see java.applet.Applet#showStatus(java.lang.String)
   */
  public void showStatus( String s ) {
    // do nothing
  }

}

/**
 * The window with the applet. Extra is the menu bar.
 *
 * @author Edwin Martin
 */
class AppletFrame extends Frame {
  private final GameOfLife applet;
    /**
     * Constructor.
   * @param title title of window
   * @param applet applet to show
   */
  public AppletFrame(String title, StandaloneGameOfLife applet) {
        super( title );
    this.applet = applet;

    enableEvents(Event.WINDOW_DESTROY);

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints appletContraints = new GridBagConstraints();
    setLayout(gridbag);
    appletContraints.fill = GridBagConstraints.BOTH;
    appletContraints.weightx = 1;
    appletContraints.weighty = 1;
    gridbag.setConstraints(applet, appletContraints);
    setResizable(true);
    add(applet);
    Toolkit screen = getToolkit();
    Dimension screenSize = screen.getScreenSize();
    // Java in Windows opens windows in the upper left corner, which is ugly! Center instead.
    if ( screenSize.width >= 640 && screenSize.height >= 480 )
        setLocation((screenSize.width-550)/2, (screenSize.height-400)/2);
    applet.init( this );
    applet.start();
    pack();
    show();
    toFront();
  }

  /**
   * Process close window button.
   * @see java.awt.Component#processEvent(java.awt.AWTEvent)
   */
  public void processEvent( AWTEvent e ) {
    if ( e.getID() == Event.WINDOW_DESTROY )
      System.exit(0);
  }

}

