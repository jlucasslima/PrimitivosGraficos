import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ponto.PontoGr;
import reta.RetaGr;
import circulo.CirculoGr;
import retangulo.RetanguloGr;
import triangulo.TrianguloGr;

/**
 * Cria desenhos de acordo com o tipo, gerencia cliques de mouse e redesenha a partir da ED.
 * 
 * Integrantes do Grupo:
 * - João Lucas de Lima Souza - RA00360044
 * - Pedro Chagas Neves de Farias Nascimento - RA00359511
 * - Guilherme Pereira de Rivoredo - RA00359303
 * - Eduardo Achkar avancini - RA00359889
 */
@SuppressWarnings("serial")
public class PainelDesenho extends JPanel implements MouseListener, MouseMotionListener {
    JLabel msg;
    TipoPrimitivo tipo;
    Color corAtual;
    int esp;

    // Estrutura de dados instanciada
    private EstruturaDados ed = new EstruturaDados();
    
    // Filtro atual
    private String filtroAtual = "Todos";

    // Marcadores (offsets) para esconder os primitivos antigos ao limpar a tela
    private int pontoOffset = 0;
    private int retaOffset = 0;
    private int circuloOffset = 0;
    private int retanguloOffset = 0;
    private int trianguloOffset = 0;

    // Variáveis de controle de desenho
    int x1, y1, x2, y2;
    int xAtual, yAtual; // Usados para o efeito visual "em tempo real" (MS Paint)
    boolean arrastando = false; 
    int numCliques = 0; // Usado exclusivamente para o Triângulo

    public PainelDesenho(JLabel msg, TipoPrimitivo tipo, Color corAtual, int esp){
        setTipo(tipo);
        setMsg(msg);
        setCorAtual(corAtual);
        setEsp(esp);
        this.addMouseListener(this); 
        this.addMouseMotionListener(this);
    }

    public void setTipo(TipoPrimitivo tipo){ 
        this.tipo = tipo; 
        this.numCliques = 0; 
        this.arrastando = false; 
    }
    public TipoPrimitivo getTipo(){ return this.tipo; }
    public void setEsp(int esp){ this.esp = esp; }
    public int getEsp(){ return this.esp; }
    public void setCorAtual(Color corAtual){ this.corAtual = corAtual; }
    public Color getCorAtual(){ return this.corAtual; }
    public void setMsg(JLabel msg){ this.msg = msg; }
    public JLabel getMsg(){ return this.msg; }

    public void setFiltroAtual(String filtro) {
        this.filtroAtual = filtro;
        this.pontoOffset = 0;
        this.retaOffset = 0;
        this.circuloOffset = 0;
        this.retanguloOffset = 0;
        this.trianguloOffset = 0;
        repaint();
    }

    public void limparTelaVisualmente() {
        this.pontoOffset = ed.pontos.size();
        this.retaOffset = ed.retas.size();
        this.circuloOffset = ed.circulos.size();
        this.retanguloOffset = ed.retangulos.size();
        this.trianguloOffset = ed.triangulos.size();
        this.numCliques = 0; 
        this.arrastando = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        // 1. Redesenha os elementos salvos na ED
        if (filtroAtual.equals("Todos") || filtroAtual.equals("Pontos")) {
            for (int i = pontoOffset; i < ed.pontos.size(); i++) {
                ed.pontos.get(i).desenharPonto(g);
            }
        }
        if (filtroAtual.equals("Todos") || filtroAtual.equals("Retas")) {
            for (int i = retaOffset; i < ed.retas.size(); i++) {
                ed.retas.get(i).desenharRetaMp(g);
            }
        }
        if (filtroAtual.equals("Todos") || filtroAtual.equals("Circulos")) {
            for (int i = circuloOffset; i < ed.circulos.size(); i++) {
                ed.circulos.get(i).desenharCirculo(g);
            }
        }
        if (filtroAtual.equals("Todos") || filtroAtual.equals("Retangulos")) {
            for (int i = retanguloOffset; i < ed.retangulos.size(); i++) {
                ed.retangulos.get(i).desenharRetangulo(g);
            }
        }
        if (filtroAtual.equals("Todos") || filtroAtual.equals("Triangulos")) {
            for (int i = trianguloOffset; i < ed.triangulos.size(); i++) {
                ed.triangulos.get(i).desenharTriangulo(g);
            }
        }

        // 2. Desenho temporário (em tempo real) durante o arrasto do mouse
        if (arrastando) {
            if (tipo == TipoPrimitivo.RETA) {
                new RetaGr(x1, y1, xAtual, yAtual, getCorAtual(), getEsp()).desenharRetaMp(g);
            } else if (tipo == TipoPrimitivo.CIRCULO) {
                new CirculoGr(x1, y1, xAtual, yAtual, getCorAtual(), getEsp()).desenharCirculo(g);
            } else if (tipo == TipoPrimitivo.RETANGULO) {
                new RetanguloGr(x1, y1, xAtual, yAtual, getCorAtual(), getEsp()).desenharRetangulo(g);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (tipo == TipoPrimitivo.PONTO){
            ed.pontos.add(new PontoGr(e.getX(), e.getY(), getCorAtual(), getEsp()));
            repaint();
            
        } else if (tipo == TipoPrimitivo.RETA || tipo == TipoPrimitivo.CIRCULO || tipo == TipoPrimitivo.RETANGULO){
            // No primeiro clique, grava o ponto de início e avisa que está arrastando
            x1 = e.getX(); 
            y1 = e.getY();
            xAtual = x1;
            yAtual = y1;
            arrastando = true;
            
        } else if (tipo == TipoPrimitivo.TRIANGULO) {
            // Mantém a lógica de 3 cliques seguidos para o Triângulo
            if (numCliques == 0) {
                x1 = e.getX(); y1 = e.getY();
                numCliques = 1;
            } else if (numCliques == 1) {
                x2 = e.getX(); y2 = e.getY();
                numCliques = 2;
            } else {
                int x3 = e.getX(), y3 = e.getY();
                ed.triangulos.add(new TrianguloGr(x1, y1, x2, y2, x3, y3, getCorAtual(), getEsp()));
                numCliques = 0;
                repaint();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Ao arrastar (mover mouse com botão pressionado), atualiza o ponto final temporário
        if (arrastando && (tipo == TipoPrimitivo.RETA || tipo == TipoPrimitivo.CIRCULO || tipo == TipoPrimitivo.RETANGULO)) {
            xAtual = e.getX();
            yAtual = e.getY();
            repaint(); // Pinta o desenho "esticando"
        }
        this.msg.setText("(" + e.getX() + ", " + e.getY() + ") - " + getTipo() + " | Desenhando...");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Ao soltar o botão, grava o formato permanentemente na ED
        if (arrastando && (tipo == TipoPrimitivo.RETA || tipo == TipoPrimitivo.CIRCULO || tipo == TipoPrimitivo.RETANGULO)) {
            x2 = e.getX();
            y2 = e.getY();
            
            if (tipo == TipoPrimitivo.RETA) ed.retas.add(new RetaGr(x1, y1, x2, y2, getCorAtual(), getEsp()));
            if (tipo == TipoPrimitivo.CIRCULO) ed.circulos.add(new CirculoGr(x1, y1, x2, y2, getCorAtual(), getEsp()));
            if (tipo == TipoPrimitivo.RETANGULO) ed.retangulos.add(new RetanguloGr(x1, y1, x2, y2, getCorAtual(), getEsp()));
            
            arrastando = false;
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        this.msg.setText("(" + e.getX() + ", " + e.getY() + ") - " + getTipo() + (tipo == TipoPrimitivo.TRIANGULO ? " | Cliques: " + numCliques : ""));
    }
}