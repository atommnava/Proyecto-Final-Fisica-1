package computacion.cuantica.main;

import java.util.LinkedList;
import java.util.Queue;

public class LaberintoCuantico {
    public int contador = 0;
    public char[][] laberinto = {
            {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
            {'#',' ',' ',' ','#',' ',' ',' ',' ','#',' ',' ',' ',' ','#',' ',' ',' ',' ','#'},
            {'#',' ','#',' ','#',' ','#','#',' ','#',' ','#','#',' ','#',' ','#','#',' ','#'},
            {'#',' ','#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
            {'#',' ','#','#','#','#','#',' ','#','#','#','#','#',' ','#','#','#','#',' ','#'},
            {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
            {'#','#','#','#','#','#','#',' ','#','#','#','#','#','#','#','#','#',' ','#','#'},
            {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
            {'#',' ','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#',' ','#'},
            {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
            {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
    };
    private static class Nodo {
        int x, y, pasos;
        Nodo padre;
        Nodo(int x, int y, int pasos, Nodo padre) {
            this.x = x;
            this.y = y;
            this.pasos = pasos;
            this.padre = padre;
        }
    }

    public boolean resuelve(int xInicio, int yInicio) throws InterruptedException {
        Queue<Nodo> cola = new LinkedList<>();
        boolean[][] visitado = new boolean[laberinto.length][laberinto[0].length];
        cola.add(new Nodo(xInicio, yInicio, 0, null));
        visitado[xInicio][yInicio] = true;
        int[][] direcciones = {{0,1},{-1,0},{0,-1},{1,0}};

        while (!cola.isEmpty()) {
            int nodosEnEsteNivel = cola.size();
            char[][] laberintoTemp = copiarLaberinto();
            for (int i = 0; i < nodosEnEsteNivel; i++) {
                Nodo actual = cola.poll();
                contador = actual.pasos;
                if (laberinto[actual.x][actual.y] == 'X') {
                    marcarCamino(actual);
                    laberinto[xInicio][yInicio] = 'S';
                    return true;
                }
                laberintoTemp[actual.x][actual.y] = '*';
                for (int[] dir : direcciones) {
                    int nuevaX = actual.x + dir[0];
                    int nuevaY = actual.y + dir[1];

                    if (nuevaX >= 0 && nuevaX < laberinto.length &&
                            nuevaY >= 0 && nuevaY < laberinto[0].length &&
                            !visitado[nuevaX][nuevaY] &&
                            laberinto[nuevaX][nuevaY] != '#') {
                        visitado[nuevaX][nuevaY] = true;
                        cola.add(new Nodo(nuevaX, nuevaY, actual.pasos + 1, actual));
                    }
                }
            }
            laberinto = laberintoTemp;
            imprimirLaberinto();
            Thread.sleep(1000);
        }
        return false;
    }

    private char[][] copiarLaberinto() {
        char[][] copia = new char[laberinto.length][laberinto[0].length];
        for (int i = 0; i < laberinto.length; i++) {
            System.arraycopy(laberinto[i], 0, copia[i], 0, laberinto[i].length);
        }
        return copia;
    }

    private void marcarCamino(Nodo nodo) {
        while (nodo.padre != null) {
            laberinto[nodo.x][nodo.y] = '.';
            nodo = nodo.padre;
        }
    }

    private void imprimirLaberinto() {
        System.out.println("\033[H\033[2J");
        for (char[] fila : laberinto) {
            for (char c : fila) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println("Pasos (superposición cuántica): " + contador);
        System.out.println("-------------------");
    }

    public static void main(String[] args) throws InterruptedException {
        LaberintoCuantico m = new LaberintoCuantico();
        m.laberinto[1][1] = 'X'; // Salida
        m.laberinto[9][1] = 'S'; // Entrada (opcional)
        long inicio = System.nanoTime();
        boolean resuelto = m.resuelve(9, 5); // Resolver desde (9,1)
        long fin = System.nanoTime();

        if (resuelto) {
            System.out.println("\nLaberinto resuelto:");
            m.imprimirLaberinto();
            System.out.println("Pasos totales: " + m.contador);
            System.out.println("Tiempo de ejecución: " + (fin - inicio)/1_000_000 + " ms");
        } else {
            System.out.println("No se encontró solución");
        }
    }
}