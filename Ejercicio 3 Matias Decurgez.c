/*3.	Realiza un programa que determine si una cadena de caracteres ingresada por el usuario es palíndromo (se lee igual de izquierda a derecha que de derecha a izquierda).*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main()
{
	char cadena[20];
	int longitud, y, contpalin=0;
	printf("Ingrese solo una cadena de caracteres: ");
	scanf("%s", &cadena);
	longitud = strlen(cadena);
	for(y=longitud-1;cadena[y]==cadena[contpalin]&&y>=0;y--)
	{
		contpalin++;
	}
	if(contpalin==longitud)
	{
		printf("\nEs palindromo.");
	}
	else
	{
		printf("\nNo es palindromo.");
	}
}
