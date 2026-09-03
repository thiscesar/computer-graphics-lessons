import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class Framebuffer {
    private static final int BYTES_POR_PIXEL = 4;

    private final int largura;
    private final int altura;
    private final int stride;
    private final BufferedImage imagem;
    private final byte[] pixels;

    public Framebuffer(int largura, int altura) {
        if (largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("dimensoes invalidas: " + largura + "x" + altura);
        }
        this.largura = largura;
        this.altura = altura;
        this.stride = largura * BYTES_POR_PIXEL;
        this.imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_4BYTE_ABGR);
        this.pixels = ((DataBufferByte) this.imagem.getRaster().getDataBuffer()).getData();
    }

    public int largura() {
        return largura;
    }

    public int altura() {
        return altura;
    }

    public BufferedImage imagem() {
        return imagem;
    }

    private int indice(int x, int y) {
        return y * stride + x * BYTES_POR_PIXEL;
    }

    private boolean dentro(int x, int y) {
        return x >= 0 && x < largura && y >= 0 && y < altura;
    }

    // Fora dos limites e ignorado: da clipping de graca a tudo que use este metodo.
    public void desenhaPixel(int x, int y, int r, int g, int b) {
        if (!dentro(x, y)) {
            return;
        }
        // TYPE_4BYTE_ABGR guarda os canais nesta ordem fisica: A, B, G, R.
        int p = indice(x, y);
        pixels[p] = (byte) 255;
        pixels[p + 1] = (byte) b;
        pixels[p + 2] = (byte) g;
        pixels[p + 3] = (byte) r;
    }

    public void limpar() {
        Arrays.fill(pixels, (byte) 0);
    }
}
