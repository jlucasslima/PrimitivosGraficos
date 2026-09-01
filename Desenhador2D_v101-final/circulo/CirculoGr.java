package circulo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representação e desenho do primitivo Círculo.
 * 
 * Integrantes do Grupo:
 * - João Lucas de Lima Souza - RA00360044
 * - Pedro Chagas Neves de Farias Nascimento - RA00359511
 * - Guilherme Pereira de Rivoredo - RA00359303
 * - Eduardo Achkar avancini - RA00359889
 */
public class CirculoGr {
    private int x, y, raio, esp;
    private Color cor;

    public CirculoGr(int x1, int y1, int x2, int y2, Color cor, int esp) {
        this.x = x1; 
        this.y = y1;
        this.raio = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        this.cor = cor;
        this.esp = esp;
    }

    public void desenharCirculo(Graphics g) {
        g.setColor(cor);
        for (int i = 0; i < esp; i++) {
            int ajuste = i / 2;
            g.drawOval(x - raio - ajuste, y - raio - ajuste, (raio + ajuste) * 2, (raio + ajuste) * 2);
        }
    }
}