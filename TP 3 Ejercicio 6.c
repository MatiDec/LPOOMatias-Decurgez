/*6. Contar el número de ocurrencias de un elemento en un arreglo usando
puntero y funciones*/
#include <stdio.h>
#include <string.h>

char cadena[20], elemento;
int contocurrencias = 0;

void ocurrencias(char *cadena, int longitud);

int main()
{
	printf("Ingrese la cadena de caracteres: ");
	fgets(cadena, sizeof(cadena), stdin);
	int longitud = sizeof(cadena);
	printf("\nIngrese el elemento para buscar sus ocurrencias dentro de la cadena: ");
	scanf("%c", &elemento);
	ocurrencias(cadena, longitud);
	printf("\nEl elemento %c se encuentra repetido %d vez/veces en la cadena ingresada.", elemento, contocurrencias);
}

void ocurrencias(char *cadena, int longitud)
{
	int x;
	for(x=0;x<longitud;x++)
	{
		if(*(cadena+x) == elemento)
		{
			contocurrencias++;
		}
	}
}
