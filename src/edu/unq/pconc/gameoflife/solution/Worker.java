package edu.unq.pconc.gameoflife.solution;

public class Worker extends Thread{
	
	
	int posicionInicial;
	int cantColumnas;
	int cantColumnasFija;
	GameOfLifeGrid gof;
	
	public Worker(int posicionInicial, GameOfLifeGrid gof,int cantColumnas, int cantColumnasFija) {
		this.posicionInicial = posicionInicial;
		this.gof = gof;
		this.cantColumnas = cantColumnas;
		this.cantColumnasFija = cantColumnasFija;
	}
	
	private boolean [][] copyTablero() {
		int filas = gof.filas;
		int col = gof.columnas;
		boolean [][] copy = new boolean [filas][col];
		for(int i = 0; i<filas;i++){
			for(int j = 0; j<col;j++){
				copy[i][j]=gof.tableroAux[i][j];
			}
		}
		return copy;
	}
	

	
	
	public synchronized void run(){
		
		boolean [][] copy = copyTablero();
		for(int i = 0; i<gof.filas;i++){
			for(int j = this.posicionInicial; j < cantColumnasFija+this.posicionInicial;j++){
				gof.tablero[i][j]=evolucionar(copy, i,j);
			}
		}
		
		gof.workerTermino();
	}
	
	
	
	
	private boolean evolucionar(boolean [][] copy, int f, int c)
	{
		int alive = 0;
		
		for(int i = f-1; i <= f+1; i++)
			for(int j = c-1; j <= c+1; j++)
				if(i >= 0 && i < gof.filas && j >= 0 && j < gof.columnas && !(i == f && j == c))
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