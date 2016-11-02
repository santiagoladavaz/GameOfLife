package edu.unq.pconc.gameoflife.solution;
import java.awt.Dimension;
import edu.unq.pconc.gameoflife.CellGrid;


public class GameOfLifeGrid implements CellGrid {

	boolean [][] tablero;
	int filas;
	int columnas;
	int generaciones;
	int threads;
	


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

	private boolean [][] copyTablero() {
		boolean [][] copy = new boolean [filas][columnas];
		for(int i = 0; i<filas;i++){
			for(int j = 0; j<columnas;j++){
				copy[i][j]=tablero[i][j];
			}
		}
		return copy;
	}
	
	@Override
	public synchronized void next() {
		boolean [][] copy = copyTablero();
		for(int i = 0; i<filas;i++){
			for(int j = 0; j<columnas;j++){
				tablero[i][j]=evolucionar(copy, i,j);
			}
		}
		this.generaciones++;
	}
	
	private boolean evolucionar(boolean [][] copy, int f, int c)
	{
		int alive = 0;
		
		for(int i = f-1; i <= f+1; i++)
			for(int j = c-1; j <= c+1; j++)
				if(i >= 0 && i < this.filas && j >= 0 && j < this.columnas && !(i == f && j == c))
					if(copy[i][j])
						alive++;
		
		if(copy[f][c]){
			return (alive == 2 || alive == 3 );
		}
		else{
			return (alive == 3);
		}
	}
	
}
