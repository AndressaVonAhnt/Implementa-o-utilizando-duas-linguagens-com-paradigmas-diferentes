// Lógica do Mandelbrot

#include <stdio.h>
#include <stdlib.h>

void calcularMandelbrot(int *pixels, int width, int height, double xMin, double xMax, double yMin, double yMax, int max_iter) {
    double dx = (xMax - xMin) / width;
    double dy = (yMax - yMin) / height;

    for (int y = 0; y < height; y++) {
        double c_im = yMin + y * dy;
        for (int x = 0; x < width; x++) {
            double c_re = xMin + x * dx;
            double z_re = 0, z_im = 0;
            int n = 0;

            // Lógica de escape (módulo > 2 ou iteração máxima)
            while (z_re * z_re + z_im * z_im <= 4.0 && n < max_iter) {
                double temp = z_re * z_re - z_im * z_im + c_re;
                z_im = 2.0 * z_re * z_im + c_im;
                z_re = temp;
                n++;
            }
            pixels[y * width + x] = n;
        }
    }
}

// Main para teste de terminal 
int main() {
    int width = 80;  
    int height = 40;
    int max_iter = 100;
    int *pixels = malloc(width * height * sizeof(int));

    calcularMandelbrot(pixels, width, height, -2.0, 1.0, -1.2, 1.2, max_iter);

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int n = pixels[y * width + x];
            if (n == max_iter) {
                printf("#"); 
            } else {
                printf("."); 
            }
        }
        printf("\n");
    }

    free(pixels);
    printf("\nCálculo do fractal concluído com sucesso no C!\n");
    return 0;
}