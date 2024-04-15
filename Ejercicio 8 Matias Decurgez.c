/*8. Realizar un programa en el cual el usuario primero ingrese una oración
la cual puede estar en mayúscula o minúscula o de manera alternada
y luego pueda seleccionar que realice las siguientes acciones
a) Mostrar la oración toda en mayúscula
b) Mostrar la oración todo en minúsculas
c) Mostrar la oración alternando una mayúscula y una minúscula
D) Mostrar la oración comenzando todas las palabras con mayúscula*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main()
{
	char oracion[30], oracionintercalada[30];
	int x, longitud, index=2;
	printf("Ingrese una oracion: ");
	fgets(oracion, 30, stdin);
	longitud = strlen(oracion);
	for(x=0;x<longitud;x++)
	{
		if(oracion[x]>=97&&oracion[x]<=122)
		{
			oracion[x]=oracion[x]-32;
		}
	}
	printf("\nOracion en mayuscula: %s", oracion);
	for(x=0;x<longitud;x++)
	{
		if(oracion[x]>=65&&oracion[x]<=90)
		{
			oracion[x]=oracion[x]+32;
		}
	}
	printf("\nOracion en minuscula: %s", oracion);
	for(x=0;x<longitud;x++)
	{
		if(oracion[x]!=' ')
		{
			if(index%2==0)
			{
				if(oracion[x]>=97&&oracion[x]<=122||oracion[x]>=65&&oracion[x]<=90)
				{
					oracion[x]=oracion[x]-32;
				}
			}
			index++;
		}
	}
	printf("\nOracion intercalada: %s", oracion);
	for(x=0;x<longitud;x++)
	{
		if(oracion[x]>=65&&oracion[x]<=90)
		{
			oracion[x]=oracion[x]+32;
		}
	}
	for(x=0;x<longitud;x++)
	{
		if(oracion[x]==' '&&oracion[x+1]>=97&&oracion[x+1]<=122)
		{
			oracion[x+1]=oracion[x+1]-32;
		}
		else if(x==0&&oracion[x]>=97&&oracion[x]<=122)
		{
			oracion[x]=oracion[x]-32;
		}
	}
	printf("\nOracion con la primera letra de cada palabra en mayuscula: %s", oracion);
}
