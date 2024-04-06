/*2.	Realiza un programa que cuente cuántas vocales tiene una cadena de caracteres ingresada por el usuario.*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main()
{
	int x, longitud, vocales=0;
	char cadena[20];
	printf("Ingrese una cadena de caracteres: ");
	fgets(cadena, sizeof(cadena), stdin);
	longitud = strlen(cadena);
	for(x=0;x<=longitud;x++)
	{
		if(cadena[x]=='a'||cadena[x]=='e'||cadena[x]=='i'||cadena[x]=='o'||cadena[x]=='u'||cadena[x]=='A'||cadena[x]=='E'||cadena[x]=='I'||cadena[x]=='O'||cadena[x]=='U')
		{
			vocales++;
		}
	}
	printf("\nSu cadena de caracteres tiene %d vocales.", vocales);
}
