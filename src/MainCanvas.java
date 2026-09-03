import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainCanvas extends JPanel implements Runnable {


    // porque 6400?
    private static final int FILE_BUFFER_SIZE = 64 * 1024;

    private Thread runner;
    private boolean isLoopActive = true;
    // private int paintcounter = 0; -- AINDA NÃO UTILIZADA

    private final Framebuffer framebuffer;

    private Random rand = new Random();

    // private byte memoriaPlacaVideo[]; -- AINDA NÃO UTILIZADA
    // private short paleta[][]; -- AINDA NÃO UTILIZADA

    private int framecount = 0;
    private int fps = 0;

    private Font font = new Font("", Font.PLAIN, 30);

    private int clickX = 0;
    private int clickY = 0;
    private int mouseX = 0;
    private int mouseY = 0;

    private BufferedImage imgtmp = null;

    private float posx = 00;
    private float posy = 00;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private float filtroR = 1.0f;
    private float filtroG = 1.0f;
    private float filtroB = 1.0f;

    private float q1x = 10;
    private float q1y = 100;
    private float q2x = 10;
    private float q2y = 200;

    public MainCanvas(int largura, int altura) {
        File f = new File("imgbmp.bmp");
        try {
            FileInputStream fin = new FileInputStream(f);

            byte buffer[] = new byte[FILE_BUFFER_SIZE];
            int bytesLidos = fin.read(buffer);

            fin.close();

            System.out.println("Bytes Lidos " + bytesLidos);

            for (int i = 0; i < bytesLidos; i++) {
                System.out.println(i + ": " + buffer[i]);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        setPreferredSize(new Dimension(largura, altura));
        setFocusable(true);

        // try {
        // imgtmp = ImageIO.read(getClass().getResource("fundo.jpg"));
        // System.out.println(""+imgtmp.toString());
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // }

        this.imgtmp = loadImage("res/images/gato.jpg");

        this.framebuffer = new Framebuffer(largura, altura);

        // File f = new File("t1.bmp");
        // try {
        // DataInputStream din = new DataInputStream(new FileInputStream(f));
        // byte b[] = new byte[128];
        // int quant = 0;
        // int cont = 0;
        // while((quant = din.read(b))>=0) {
        // for(int i = 0; i < quant;i++) {
        // System.out.print(""+(b[i]&0xff)+" ");
        // }
        // System.out.println();
        // cont++;
        // if(cont==10) {
        // break;
        // }
        // }
        // } catch (IOException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }

        // for(int i = 0; i < H;i++) {
        // for(int j = 0; j < W;j++) {
        // int pos = (i*W*4)+(j*4);
        //
        // int soma = bufferDeVideo[pos+1]&0xff;
        // soma += bufferDeVideo[pos+2]&0xff;
        // soma += bufferDeVideo[pos+3]&0xff;
        //
        // int media = soma/3;
        // //System.out.println(""+media);
        //
        // bufferDeVideo[pos+1] = (byte)(Math.min((media*20)/100,255)&0x00ff);
        // bufferDeVideo[pos+2] = (byte)(Math.min((media*60)/100,255)&0x00ff);
        // bufferDeVideo[pos+3] = (byte)(Math.min((media*20)/100,255)&0x00ff);
        // }
        // }

        // memoriaPlacaVideo = new byte[W*H];

        /*
         * paleta = new short[255][3];
         * 
         * for(int i = 0; i < 255;i++){
         * paleta[i][0] = (short)rand.nextInt(255);
         * paleta[i][1] = (short)rand.nextInt(255);
         * paleta[i][2] = (short)rand.nextInt(255);
         * 
         * }
         */

        // Seta Bugfeer com noise
        /*
         * for(int i = 0; i < bufferDeVideo.length;i+=4){
         * int r = rand.nextInt(255);
         * int g = rand.nextInt(255);
         * int b = rand.nextInt(255);
         * 
         * bufferDeVideo[i] = (byte)0x00ff;
         * bufferDeVideo[i+1] = (byte)(0x00ff&b);
         * bufferDeVideo[i+2] = (byte)(0x00ff&g);
         * bufferDeVideo[i+3] = (byte)(0x00ff&r);
         * }8?
         * 
         * // // 100,20 200,20
         * // for(int i = 0; i < 100;i++){
         * // int x = 100+i;
         * // int y = 20;
         * // int bt = x*4+y*640*4;
         * // bufferDeVideo[bt] = (byte)0x00ff;
         * // bufferDeVideo[bt+1] = (byte)0;
         * // bufferDeVideo[bt+2] = (byte)0;
         * // bufferDeVideo[bt+3] = (byte)0x00ff;
         * // }
         * 
         * /*for(int y = 0; y < H;y++){
         * for(int x = 0; x < W;x++){
         * memoriaPlacaVideo[x+y*W] = (byte)((y%255)&0x00ff);
         * }
         * }
         */
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) {
                    up = false;
                }
                if (key == KeyEvent.VK_S) {
                    down = false;
                }
                if (key == KeyEvent.VK_A) {
                    left = false;
                }
                if (key == KeyEvent.VK_D) {
                    right = false;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                System.out.println("CLICO " + key);
                if (key == KeyEvent.VK_W) {
                    up = true;
                }
                if (key == KeyEvent.VK_S) {
                    down = true;
                }
                if (key == KeyEvent.VK_A) {
                    left = true;
                }
                if (key == KeyEvent.VK_D) {
                    right = true;
                }
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                clickX = e.getX();
                clickY = e.getY();

                System.out.println("CLICO ");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent arg0) {
                // TODO Auto-generated method stub
                mouseX = arg0.getX();
                mouseY = arg0.getY();
            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void drawImageToBuffer(BufferedImage image, int x, int y, float fr, float fg, float fb) {
        byte[] imgBuffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        int iw = image.getWidth();
        int ih = image.getHeight();

        for (int yi = 0; yi < ih; yi++) {
            for (int xi = 0; xi < iw; xi++) {
                int pixi = yi * iw * 4 + xi * 4;

                // BW
                // int soma = (imgBuffer[pixi+1]&0xff) + (imgBuffer[pixi+2]&0xff) +
                // (imgBuffer[pixi+3]&0xff);
                // int res = (int)(soma/3);
                //
                // bufferDeVideo[pixb+1] = (byte)(res&0xff);
                // bufferDeVideo[pixb+2] = (byte)(res&0xff);
                // bufferDeVideo[pixb+3] = (byte)(res&0xff);

                // int b = (imgBuffer[pixi+1]&0xff);
                // int g = (imgBuffer[pixi+2]&0xff);
                // int r = (imgBuffer[pixi+3]&0xff);
                //
                // int media = (int)255-(((b+g+r)/3));
                // media = Math.min(255, media);
                //
                // b = media;
                // g = media;
                // r = media;

                int b = (imgBuffer[pixi + 1] & 0xff);
                int g = (imgBuffer[pixi + 2] & 0xff);
                int r = (imgBuffer[pixi + 3] & 0xff);

                b = (int) (b * fb);
                g = (int) (g * fg);
                r = (int) (r * fr);
                //
                b = Math.min(255, b);
                g = Math.min(255, g);
                r = Math.min(255, r);

                framebuffer.desenhaPixel(xi + x, yi + y, r, g, b);
            }
        }
    }

    @Override
    public void paint(Graphics g) {

        framebuffer.limpar();

        // for(int j = 0; j < H;j++) {
        // for(int i = 0; i < W;i++) {
        // int pos = i*4+W*4*j;
        // bufferDeVideo[pos] = (byte)255;
        // bufferDeVideo[pos+1] = (byte)0;
        // bufferDeVideo[pos+2] = (byte)128;
        // bufferDeVideo[pos+3] = (byte)255;
        // }
        // }
        //
        // for(int i = 0; i < 100;i++) {
        // int p0 = 50*4+W*4*100;
        // int pos = p0+i*4;
        // bufferDeVideo[pos] = (byte)255;
        // bufferDeVideo[pos+1] = (byte)255;
        // bufferDeVideo[pos+2] = (byte)0;
        // bufferDeVideo[pos+3] = (byte)0;
        // }

        drawImageToBuffer(imgtmp, (int) posx, (int) posy, filtroR, filtroG, filtroB);

        desenhaLinhaHorizontal(10, 100, 400, 0, 0, 0);

        desenhaLinhaVertical(10, 20, 200, 255, 0, 0);

        desenhaLinha(250, 250, 600, 450, 0, 160, 0);

        // desenhaLinhaVertical(300,200,200);

        // TODO Auto-generated method stub
        // super.paint(g);

        // for(int i = 0; i < bufferDeVideo.length;i+=4){
        // int rr = rand.nextInt(255);
        // int gg = rand.nextInt(255);
        // int bb = rand.nextInt(255);
        //
        // bufferDeVideo[i] = (byte)0x00ff;
        // bufferDeVideo[i+1] = (byte)(0x00ff&bb);
        // bufferDeVideo[i+2] = (byte)(0x00ff&gg);
        // bufferDeVideo[i+3] = (byte)(0x00ff&rr);
        // }

        /*
         * for(int i = 0; i < memoriaPlacaVideo.length;i++){
         * int bufferindex = i*4;
         * bufferDeVideo[bufferindex] = (byte)0x00ff;
         * bufferDeVideo[bufferindex+1] =
         * (byte)(paleta[memoriaPlacaVideo[i]&0x00ff][2]&0x00ff);
         * bufferDeVideo[bufferindex+2] =
         * (byte)(paleta[memoriaPlacaVideo[i]&0x00ff][1]&0x00ff);
         * bufferDeVideo[bufferindex+3] =
         * (byte)(paleta[memoriaPlacaVideo[i]&0x00ff][0]&0x00ff);
         * }
         */

        g.setFont(this.font);

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        // g.setColor(Color.black);
        // g.drawLine(0, 0, 640, 480);

        g.drawImage(framebuffer.imagem(), 0, 0, null);

        // g.setColor(Color.BLUE);
        // g.drawLine(clickX, clickY, mouseX, mouseY);

        g.setColor(Color.black);
        g.drawString("FPS " + fps + " mouse: " + mouseX + "," + mouseY, 10, 25);
    }

    public void desenhaLinhaHorizontal(int x, int y, int w, int r, int g, int b) {
        for (int i = 0; i < w; i++) {
            framebuffer.desenhaPixel(x + i, y, r, g, b);
        }
    }

    public void desenhaLinhaVertical(int x, int y, int h, int r, int g, int b) {
        for (int i = 0; i < h; i++) {
            framebuffer.desenhaPixel(x, y + i, r, g, b);
        }
    }

    // Algoritmo de Bresenham (1962): desenha a linha usando SO aritmetica inteira,
    // sem divisao e sem ponto flutuante. A ideia central e nunca calcular y = m*x + c;
    // em vez disso, mantem-se um acumulador de erro e a cada passo pergunta-se apenas
    // "o desvio acumulado ja passou de meio pixel?".
    public void desenhaLinha(int x0, int y0, int x1, int y1, int r, int g, int b) {
        // Distancias em cada eixo, sempre positivas: o sinal vai para sx/sy.
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        // Direcao de cada eixo (+1 ou -1). Separar "quanto" (dx/dy) de "para que lado"
        // (sx/sy) e o que faz o mesmo laco cobrir os 8 octantes sem caso especial.
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        // O acumulador. Pense nele como o saldo de um cabo de guerra entre os dois eixos:
        // andar em x "gasta" dy do saldo, andar em y "devolve" dx.
        int erro = dx - dy;

        int x = x0;
        int y = y0;

        while (true) {
            framebuffer.desenhaPixel(x, y, r, g, b);

            // Parada depois de desenhar, para que os dois extremos entrem na linha.
            if (x == x1 && y == y1) {
                break;
            }

            // Dobrar o erro equivale a comparar com "meio passo" sem usar fracao:
            // em vez de testar erro > 0.5, testa-se 2*erro > 1. Isto mantem tudo inteiro.
            int e2 = 2 * erro;

            // Os dois if sao INDEPENDENTES (nao if/else) de proposito: cada eixo decide
            // sozinho se avanca. Numa linha rasa so o primeiro dispara; numa ingreme so o
            // segundo; a 45 graus os dois disparam no mesmo passo, gerando a diagonal exata.
            if (e2 > -dy) {
                erro -= dy;
                x += sx;
            }
            if (e2 < dx) {
                erro += dx;
                y += sy;
            }
        }
    }

    public void desenhaPixel(int x, int y, int r, int g, int b) {
        framebuffer.desenhaPixel(x, y, r, g, b);
    }

    public void start() {
        this.runner = new Thread(this);
        this.runner.start();
    }

    int timer = 0;

    public void simulaMundo(long diftime) {
        float difS = diftime / 1000.0f;
        float vel = 50;

        timer += diftime;

        if (timer >= 1000) {
            timer = 0;
            filtroR = rand.nextFloat();
            filtroG = rand.nextFloat();
            filtroB = rand.nextFloat();
        }

        if (up) {
            posy -= vel * difS;
        }
        if (down) {
            posy += vel * difS;
        }
        if (left) {
            posx -= vel * difS;
        }
        if (right) {
            posx += vel * difS;
        }

        q1x += 0.2;
        // q2x=q2x+100*diftime/1000.0f;
        float dx = mouseX - q2x;
        float dy = mouseY - q2y;

        double ang = Math.atan2(dy, dx);

        q2x = (float) (q2x + Math.cos(ang) * 100 * diftime / 1000.0f);
        q2y = (float) (q2y + Math.sin(ang) * 100 * diftime / 1000.0f);
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        long segundo = time / 1000;
        // System.out.println(segundo);
        // System.exit(0);
        long diftime = 0;
        while (this.isLoopActive) {
            simulaMundo(diftime);
            paintImmediately(0, 0, getWidth(), getHeight()); // ignorar sugestão do repaint - não podemos entrar na fila de eventos do
                                              // SWING
            // paintcounter+=100; -- AINDA NÃO UTILIZADA

            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long newtime = System.currentTimeMillis();
            long novoSegundo = newtime / 1000;
            diftime = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            framecount++;
            if (novoSegundo != segundo) {
                fps = framecount;
                framecount = 0;
                segundo = novoSegundo;
            }
        }
    }

    public BufferedImage loadImage(String filename) {
        try {
            imgtmp = ImageIO.read(new File(filename));

            BufferedImage imgout = new BufferedImage(
                    imgtmp.getWidth(),
                    imgtmp.getHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);

            imgout.getGraphics().drawImage(imgtmp, 0, 0, null);

            imgtmp = null;

            return imgout;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}