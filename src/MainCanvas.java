import java.awt.Color;
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
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // porque 6400?
    private static final int FILE_BUFFER_SIZE = 64 * 1024;

    private Thread runner;
    private boolean isLoopActive = true;
    // private int paintcounter = 0; -- AINDA NÃO UTILIZADA

    private BufferedImage imageBuffer;
    private byte bufferDeVideo[];

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

    private int pixelSize = 0;
    private int Largura = 0;
    private int Altura = 0;

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

    public MainCanvas() {
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

        setSize(640, 480);
        setFocusable(true);

        Largura = 640;
        Altura = 480;

        pixelSize = 640 * 480;

        // try {
        // imgtmp = ImageIO.read(getClass().getResource("fundo.jpg"));
        // System.out.println(""+imgtmp.toString());
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // }

        this.imgtmp = loadImage("res/images/gato.jpg");

        this.imageBuffer = new BufferedImage(640, 480, BufferedImage.TYPE_4BYTE_ABGR);
        // imageBuffer.getGraphics().drawImage(imgtmp, 0, 0, null);

        this.bufferDeVideo = ((DataBufferByte) this.imageBuffer.getRaster().getDataBuffer()).getData();

        System.out.println("Buffer SIZE " + this.bufferDeVideo.length);

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
                int pixb = (yi + y) * WIDTH * 4 + (xi + x) * 4;
                this.bufferDeVideo[pixb] = imgBuffer[pixi];

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

                this.bufferDeVideo[pixb + 1] = (byte) (b & 0xff);
                this.bufferDeVideo[pixb + 2] = (byte) (g & 0xff);
                this.bufferDeVideo[pixb + 3] = (byte) (r & 0xff);
            }
        }
    }

    @Override
    public void paint(Graphics g) {

        for (int i = 0; i < this.bufferDeVideo.length; i++) {
            this.bufferDeVideo[i] = 0;
        }

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

        desenhaLinhaHorizontal((int) 10, (int) 100, 400);

        desenhaLinhaVertical((int) 10, (int) 20, 200);

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
        g.fillRect(0, 0, 640, 480);
        // g.setColor(Color.black);
        // g.drawLine(0, 0, 640, 480);

        g.drawImage(this.imageBuffer, 0, 0, null);

        // g.setColor(Color.BLUE);
        // g.drawLine(clickX, clickY, mouseX, mouseY);

        g.setColor(Color.black);
        g.drawString("FPS " + fps + " mouse: " + mouseX + "," + mouseY, 10, 25);
    }

    public void desenhaLinhaHorizontal(int x, int y, int w) {
        int pospix = y * (WIDTH * 4) + x * 4;

        for (int i = 0; i < w; i++) {

            this.bufferDeVideo[pospix] = (byte) 255;
            this.bufferDeVideo[pospix + 1] = (byte) 0;
            this.bufferDeVideo[pospix + 2] = (byte) 0;
            this.bufferDeVideo[pospix + 3] = (byte) 0;
            pospix += 4;
        }
    }

    public void desenhaLinhaVertical(int x, int y, int h) {
        int pospix = y * (WIDTH * 4) + x * 4;

        for (int i = 0; i < h; i++) {

            this.bufferDeVideo[pospix] = (byte) 255;
            this.bufferDeVideo[pospix + 1] = (byte) 0;
            this.bufferDeVideo[pospix + 2] = (byte) 0;
            this.bufferDeVideo[pospix + 3] = (byte) 255;
            pospix += (HEIGHT * 4);
        }
    }

    public void desenhaPixel(int x, int y, int r, int g, int b) {
        int pospix = y * (WIDTH * 4) + x * 4;

        this.bufferDeVideo[pospix] = (byte) 255;
        this.bufferDeVideo[pospix + 1] = (byte) (b & 0xff);
        this.bufferDeVideo[pospix + 2] = (byte) (g & 0xff);
        this.bufferDeVideo[pospix + 3] = (byte) (r & 0xff);
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
            paintImmediately(0, 0, 640, 480); // ignorar sugestão do repaint - não podemos entrar na fila de eventos do
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