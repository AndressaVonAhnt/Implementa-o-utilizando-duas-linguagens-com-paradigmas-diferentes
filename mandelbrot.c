#include <stdio.h>
#include <stdlib.h>
#include "Mandelbrot.h" 

JNIEXPORT void JNICALL Java_Mandelbrot_calcularMandelbrot(
    JNIEnv *env, jobject obj, jintArray pixels, jint width, jint height, 
    jdouble xMin, jdouble xMax, jdouble yMin, jdouble yMax, jint max_iter) {

    jint *raw_pixels = (*env)->GetIntArrayElements(env, pixels, NULL);
    if (raw_pixels == NULL) return;

    double dx = (xMax - xMin) / width;
    double dy = (yMax - yMin) / height;

    for (int y = 0; y < height; y++) {
        double c_im = yMin + y * dy;
        for (int x = 0; x < width; x++) {
            double c_re = xMin + x * dx;
            double z_re = 0, z_im = 0;
            int n = 0;

            while (z_re * z_re + z_im * z_im <= 4.0 && n < max_iter) {
                double temp = z_re * z_re - z_im * z_im + c_re;
                z_im = 2.0 * z_re * z_im + c_im;
                z_re = temp;
                n++;
            }

            raw_pixels[y * width + x] = (n * 255 / max_iter) << 8; 
        }
    }

    (*env)->ReleaseIntArrayElements(env, pixels, raw_pixels, 0);
}