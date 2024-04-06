/*4.	Realiza un programa que reemplace todas las apariciones de un carácter en una cadena de caracteres ingresada por el usuario por otro carácter también ingresado por el usuario.*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main()
{
	char cadena[100], char1, char2;
	printf("Ingrese una cadena de caracteres: ");
	fgets(cadena, sizeof(cadena), stdin);
	printf("\nIngrese el caracter que quiera reemplazar: ");
	scanf(" %c", &char1);
	printf("\nIngrese el caracter para reemplazar: ");
	scanf(" %c", &char2);
	int x, longitud=strlen(cadena);
	for(x=0;x<longitud;x++)
	{
		if(cadena[x]==char1)
		{
			cadena[x]=char2;
		}
	}
	printf("\n%s", cadena);
}
