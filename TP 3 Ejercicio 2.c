/*2. Encontrar la suma y el promedio de los elementos de un arreglo usando
punteros y funciones.*/

#include <stdio.h>
#include <stdlib.h>

int numero;
int suma=0;
float promedio;

void sumaypromedio(int *vector);

int main()
{
	int x;
	printf("Ingrese la cantidad de numeros: ");
	scanf("%d", &numero);
	int vector[numero];
	for(x=0;x<numero;x++)
	{
		printf("\nIngrese el valor %d: ", x+1);
		scanf("%d", &vector[x]);
	}
	sumaypromedio(vector);
	printf("\nLa suma de todos los numeros es: %d \nY su promedio: %f", suma, promedio);
}

void sumaypromedio(int *vector)
{
	int x;
	for(x=0;x<numero;x++)
	{
		suma += *(vector+x);
	}
	promedio = (float)suma/numero;
}
