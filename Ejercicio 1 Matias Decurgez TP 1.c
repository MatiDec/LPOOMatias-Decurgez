/*1.	Pedir al usuario que ingrese su nombre y luego imprimirlo en pantalla.*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{
	char nombre[20];
	printf("Ingrese su nombre: ");
	scanf("%s", &nombre);
	printf("Su nombre es: %s", nombre);
}
