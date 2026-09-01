package triangulo;

import java.awt.Color;
import java.awt.Graphics;
import reta.RetaGr;

/**
 * Representação e desenho do primitivo Triângulo.
 * 
 * Integrantes do Grupo:
 * - João Lucas de Lima Souza - RA00360044
 * - Pedro Chagas Neves de Farias Nascimento - RA00359511
 * - Guilherme Pereira de Rivoredo - RA00359303
 * - Eduardo Achkar avancini - RA00359889
 */
public class TrianguloGr {
    private int x1, y1, x2, y2, x3, y3, esp;
    private Color cor;

    public TrianguloGr(int x1, int y1, int x2, int y2, int x3, int y3, Color cor, int esp) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.x3 = x3; this.y3 = y3;
        this.cor = cor; this.esp = esp;
    }

    public void desenharTriangulo(Graphics g) {
        RetaGr r1 = new RetaGr(x1, y1, x2, y2, cor, esp); r1.desenharRetaMp(g);
        RetaGr r2 = new RetaGr(x2, y2, x3, y3, cor, esp); r2.desenharRetaMp(g);
        RetaGr r3 = new RetaGr(x3, y3, x1, y1, cor, esp); r3.desenharRetaMp(g);
    }
}