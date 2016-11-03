package edu.unq.pconc.gameoflife.solution;
import java.awt.Dimension;


import edu.unq.pconc.gameoflife.CellGrid;
import edu.unq.pconc.gameoflife.solution.Worker;


public class GameOfLifeGrid implements CellGrid {

	boolean [][] tablero;
	int filas;
	int columnas;
	int generaciones;
	int threads;
	int finalizaron = 0;
	


	@Override
	public boolean getCell(int col, int row) {
		return tablero[row][col] ;
	}

	@Override
	public void setCell(int col, int row, boolean cell) {
		tablero[row][col] = cell;
	}

	@Override
	public Dimension getDimension() {
		Dimension d = new Dimension(columnas,filas);
		return d;
	}

	@Override
	public void resize(int col, int row) {
		if(this.tablero == null){
			this.tablero = new boolean [row][col];
		}
		else{
			boolean [][] res = new boolean[row][col] ;
			for(int i = 0; i<row && i < filas;i++){
				for(int j = 0; j<col && j < columnas;j++){
						res [i][j] = tablero[i][j];
				}
			}
			this.tablero = res;
		}
		this.filas = row;
		this.columnas = col;
	}

	@Override
	public void clear() {
		for(int i = 0; i<filas;i++){
			for(int j = 0; j<columnas;j++){
				tablero[i][j]=false;
			}
		}
		this.generaciones = 0;
	}

	@Override
	public void setThreads(int threads) {
		this.threads = threads;
	}

	@Override
	public int getGenerations() {
		return this.generaciones;
	}
	
	public synchronized void workerTermino() {
		this.finalizaron ++;
		if (this.finalizaron == this.threads) {
			this.notifyAll();
		}
	}

	
	
	@Override
	public  synchronized void next() {
		int posicionInicial = 0;
		int threads = this.threads;
		
		
		int cantColumnas = (int) Math.floor(this.columnas / threads);
		int dif = this.columnas - cantColumnas * threads;
		Worker w = null; 
		
		for(int i = 0; i<this.threads; i++){
			
			int extra = (dif > 0) ? 1 : 0;
			w = new Worker(posicionInicial,this,cantColumnas + extra);
			w.start();
			posicionInicial = posicionInicial + cantColumnas + extra; 
		}
		
		while(this.finalizaron < this.threads)
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		this.generaciones++;
	}
	
	
	
}
