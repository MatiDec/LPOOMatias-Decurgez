/*1. Intercambiar el valor de dos variables usando punteros y funciones.
Mostrando los valores iniciales y valores finales.*/

#include <stdio.h>
#include <stdlib.h>

int num1, num2;

int intercambio(int *a, int *b);

int main()
{
	printf("\nIngrese dato 1: ");
	scanf("%d", &num1);
	printf("\nIngrese dato 2: ");
	scanf("%d", &num2);
	printf("\nValores iniciales %d y %d", num1, num2);
	intercambio(&num1, &num2);
	printf("\nValores finales %d y %d", num1, num2);
}

int intercambio(int *a, int *b)
{
	int aux;
	aux = *a;
	*a = *b;
	*b = aux;
}
