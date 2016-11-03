package edu.unq.pconc.gameoflife.solution;

public class Worker extends Thread{
	
	
	int posicionInicial;
	int cantColumnas;
	GameOfLifeGrid gof;
	
	public Worker(int posicionInicial, GameOfLifeGrid gof,int cantColumnas) {
		this.posicionInicial = posicionInicial;
		this.gof = gof;
		this.cantColumnas = cantColumnas;
	}
	
	private boolean [][] copyTablero() {
		int filas = gof.filas;
		int col = gof.columnas;
		boolean [][] copy = new boolean [filas][col];
		for(int i = 0; i<filas;i++){
			for(int j = 0; j<col;j++){
				copy[i][j]=gof.tablero[i][j];
			}
		}
		return copy;
	}
	

	
	
	public void run(){
		boolean [][] copy = copyTablero();
		for(int i = 0; i<gof.filas;i++){
			for(int j = this.posicionInicial; j < cantColumnas+this.posicionInicial;j++){
				gof.tablero[i][j]=evolucionar(copy, i,j);
			}
		}
		gof.workerTermino();
	}
	
	
	
	
	private synchronized boolean evolucionar(boolean [][] copy, int f, int c)
	{
		int alive = 0;
		
		for(int i = f-1; i <= f+1; i++)
			for(int j = c-1; j <= c+1; j++)
				if(i >= 0 && i < gof.filas && j >= 0 && j < this.cantColumnas && !(i == f && j == c))
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
