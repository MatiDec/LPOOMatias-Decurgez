#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{
	int x;
	char cadena1[50], cadena2[50], cadena3[50], cadena4[50], cadena5[50], copia[50];
	printf("Ingrese la cadena 1: ");
	scanf("%s", cadena1);
	printf("\nIngrese la cadena 2: ");
	scanf("%s", cadena2);
	printf("\nIngrese la cadena 3: ");
	scanf("%s", cadena3);
	printf("\nIngrese la cadena 4: ");
	scanf("%s", cadena4);
	printf("\nIngrese la cadena 5: ");
	scanf("%s", cadena5);
	for(x=0;x<50;x++)
	{
		if(cadena1[x] == cadena2[x] && cadena1[x] == cadena3[x] && cadena1[x] == cadena4[x] && cadena1[x] == cadena5[x])
		{
			copia[x]=cadena1[x];
		}
	}
	printf("%s", cadena1);
	printf(" son los que coinciden.");
}
