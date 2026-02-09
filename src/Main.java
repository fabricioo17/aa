import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Main {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("holaaa");
        Lienzo panel = new Lienzo();
        ventana.add(panel);
        ventana.setSize(600, 700);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    static class Lienzo extends JPanel {
        // Variable para controlar el paso del tiempo
        private double t = 0;

        public Lienzo() {
            // Un Timer que se ejecuta cada 16 milisegundos (aprox 60 cuadros por segundo)
            Timer cronometro = new Timer(16, e -> {
                t += 0.05; // Aumentamos el tiempo poco a poco
                repaint(); // Obligamos a Java a llamar de nuevo a paintComponent
            });
            cronometro.start(); // Iniciamos el movimiento
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = 250;

            // Calculamos un balanceo suave usando SENO
            // Esto dará un número que oscila entre -15 y 15 píxeles
            int balanceo = (int) (Math.sin(t) * 15);

            dibujarFondo(g2, getWidth(), getHeight());

            // --- PASAMOS EL BALANCEO A LAS FUNCIONES ---
            dibujarTallo(g2, cx, cy);

            // Las hojas se moverán con el balanceo
            dibujarHojaPuntiaguda(g2, cx, cy, true, balanceo);
            dibujarHojaPuntiaguda(g2, cx, cy, false, balanceo);

            dibujarCapulloBase(g2, cx, cy);
            dibujarPetaloInterno(g2, cx, cy, true);
            dibujarPetaloInterno(g2, cx, cy, false);
            dibujarEspiralCentro(g2, cx, cy);
            dibujarTextoDecorativo(g2, cx, cy);
        }

        // --- FUNCIONES MODIFICADAS PARA EL MOVIMIENTO ---

        private void dibujarFondo(Graphics2D g2, int w, int h) {
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, w, h);
        }

        private void dibujarTallo(Graphics2D g2, int cx, int cy) {
            g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(45, 90, 110));
            g2.drawLine(cx, cy + 50, cx, cy + 250);
        }

        // Ahora recibe 'int mov' para animar la punta
        private void dibujarHojaPuntiaguda(Graphics2D g2, int cx, int cy, boolean esIzquierda, int mov) {
            Path2D.Double hoja = new Path2D.Double();
            int lado = esIzquierda ? -1 : 1;

            // Sumamos 'mov' a la Y de la punta (cy + 100 + mov)
            // Esto hará que la punta suba y baje
            hoja.moveTo(cx + (150 * lado), cy + 100 + mov);

            // Los imanes también deben moverse un poco para que la curva no se rompa
            hoja.curveTo(cx + (120 * lado), cy + 80 + mov, cx + (40 * lado), cy + 100, cx, cy + 150);
            hoja.curveTo(cx + (40 * lado), cy + 120, cx + (120 * lado), cy + 140 + mov, cx + (150 * lado), cy + 100 + mov);
            hoja.closePath();

            g2.setColor(new Color(160, 210, 180));
            g2.fill(hoja);
            g2.setColor(new Color(45, 90, 110));
            g2.draw(hoja);
        }

        private void dibujarCapulloBase(Graphics2D g2, int cx, int cy) {
            g2.setColor(new Color(100, 200, 255));
            Path2D.Double base = new Path2D.Double();
            base.moveTo(cx - 80, cy - 50);
            base.curveTo(cx - 80, cy + 80, cx + 80, cy + 80, cx + 80, cy - 50);
            base.closePath();
            g2.fill(base);
            g2.setColor(new Color(30, 70, 120));
            g2.draw(base);
        }

        private void dibujarPetaloInterno(Graphics2D g2, int cx, int cy, boolean esIzquierdo) {
            Path2D.Double petalo = new Path2D.Double();
            int lado = esIzquierdo ? -1 : 1;
            petalo.moveTo(cx + (80 * lado), cy - 50);
            petalo.curveTo(cx + (40 * lado), cy + 20, cx, cy + 20, cx, cy + 60);
            petalo.curveTo(cx + (40 * lado), cy + 60, cx + (80 * lado), cy + 20, cx + (80 * lado), cy - 50);
            g2.setColor(new Color(135, 220, 255));
            g2.fill(petalo);
            g2.setColor(new Color(30, 70, 120));
            g2.draw(petalo);
        }

        private void dibujarEspiralCentro(Graphics2D g2, int cx, int cy) {
            g2.setColor(new Color(180, 240, 255));
            g2.fillOval(cx - 40, cy - 70, 80, 50);
            g2.setColor(new Color(30, 70, 120));
            g2.drawArc(cx - 30, cy - 60, 60, 30, 0, -270);
        }

        private void dibujarTextoDecorativo(Graphics2D g2, int cx, int cy) {
            g2.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.setColor(new Color(30, 70, 120));
            g2.drawString("Para ti", cx - 40, cy + 300);
        }
    }
}