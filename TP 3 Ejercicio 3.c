/*3. Encontrar el mayor y el menor elemento de un arreglo usando punteros y
funciones.*/

#include <stdio.h>
#include <stdlib.h>

int numero, mayor, menor;
void encontrar(int *vector);

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
	encontrar(vector);
	printf("\nNumero Mayor: %d \nNumero Menor: %d", mayor, menor);
}

void encontrar(int *vector)
{
	int x, y, aux;
	for(x=0;x<numero-1;x++)
	{
		for(y=0;y<numero-x-1;y++)
		{
			if(vector[y] > vector[y+1])
			{
				aux = vector[y];
				vector[y] = vector[y+1];
				vector[y+1] = aux;
			}
		}
	}
	menor = vector[0];
	mayor = vector[numero-1];
}
