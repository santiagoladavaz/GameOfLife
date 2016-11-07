package edu.unq.pconc.gameoflife.solution;
import java.awt.Dimension;


import edu.unq.pconc.gameoflife.CellGrid;
import edu.unq.pconc.gameoflife.solution.Worker;


public class GameOfLifeGrid implements CellGrid {

	public boolean [][] tablero;
	public boolean [][] tableroAux;
	public int filas;
	public int columnas;
	public int generaciones;
	public int threads;
	public int finalizaron = 0;
	


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
		this.finalizaron = 0;
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

	private boolean [][] copyTableroOriginal() {
		int filas = this.filas;
		int col = this.columnas;
		boolean [][] copy = new boolean [filas][col];
		for(int i = 0; i<filas;i++){
			for(int j = 0; j<col;j++){
				copy[i][j]=this.tablero[i][j];
			}
		}
		return copy;
	}
	
	@Override
	public  synchronized void next() {
		this.finalizaron = 0;
		int posicionInicial = 0;
		int threads = this.threads;
		this.tableroAux = copyTableroOriginal();
		
		
		int cantColumnas = (int) Math.floor(this.columnas / threads);
		int varAuxCantColumFija = cantColumnas;
		int dif = this.columnas - cantColumnas * threads;
		Worker w = null; 
		
		for(int i = 0; i<this.threads; i++){
			
			int varAuxCantColum = (i == this.threads-1) ? cantColumnas + dif : cantColumnas;
			int varAuxPosIni = (i == this.threads-1) ? posicionInicial + dif : posicionInicial;
			
			
			w = new Worker(varAuxPosIni,this,varAuxCantColum,varAuxCantColumFija);
			w.start();
			
			posicionInicial = posicionInicial + varAuxCantColum; 
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
