// Função Nativa de Cálculo do Mandelbrot
// Andressa Von Ahnt e Eduarda Fernades

#include <jni.h>
#include "Mandelbrot.h" 

JNIEXPORT void JNICALL Java_Mandelbrot_calcularMandelbrot(
    JNIEnv *env, jobject obj, jintArray pixels, jint width, jint height, 
    jdouble xMin, jdouble xMax, jdouble yMin, jdouble yMax, jint max_iter) {

    // Acesso direto ao endereço de memória do Java sem copiar os dados (mantém a taxa de quadros alta durante o zoom)
    jint *raw_pixels = (*env)->GetPrimitiveArrayCritical(env, pixels, 0);
    if (raw_pixels == NULL) return;

    // Razão de mapeamento entre pixels e plano complexo
    double dx = (xMax - xMin) / width;
    double dy = (yMax - yMin) / height;

    // Loop de renderização otimizado 
    for (int y = 0; y < height; y++) {
        double c_im = yMin + y * dy;
        for (int x = 0; x < width; x++) {
            double c_re = xMin + x * dx;
            double z_re = 0, z_im = 0;
            int n = 0;

            // Pré-cálculo dos quadrados para fazer aceleração aritmética
            double z_re2 = 0, z_im2 = 0;
            while (z_re2 + z_im2 <= 4.0 && n < max_iter) {
                z_im = 2.0 * z_re * z_im + c_im;
                z_re = z_re2 - z_im2 + c_re;
                z_re2 = z_re * z_re;
                z_im2 = z_im * z_im;
                n++;
            }

            if (n == max_iter) {
                raw_pixels[y * width + x] = 0x000000; 
            } else {
                // Cores calculadas através do deslocamento de bits
                int g = (n * 3) % 256;
                int b = (n * 10) % 256;
                raw_pixels[y * width + x] = (g << 8) | b; 
            }
        }
    }

    // Libera o bloqueio de memória e atualiza o buffer do Java
    (*env)->ReleasePrimitiveArrayCritical(env, pixels, raw_pixels, 0);
}