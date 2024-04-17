/*4. Copiar un arreglo en otro arreglo usando punteros y funciones. Mostrando
el resultado final.*/
#include <stdio.h>

int numero;
void copiararreglo(int *vector, int *vectorcopia);

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
	int vectorcopia[numero];
	copiararreglo(vector, vectorcopia);
	printf("\nEl vector copiado tiene estos valores: ");
	for(x=0;x<numero;x++)
	{
		printf("\nValor %d: %d", x+1, vectorcopia[x]);
	}
}

void copiararreglo(int *vector, int *vectorcopia)
{
	int x;
	for(x=0;x<numero;x++)
	{
		*(vectorcopia+x) = *(vector+x);
	}
}
