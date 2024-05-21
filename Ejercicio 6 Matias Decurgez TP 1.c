/*6.	Realiza un programa que determine si una cadena de caracteres ingresada por el usuario es un anagrama de otra cadena también ingresada por el usuario. 
Un anagrama es una palabra o frase formada por las mismas letras de otra palabra o frase, pero en un orden diferente, como por ejemplo "roma" y "amor".*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main()
{
	char cadena1[20], cadena2[20], aux;
	int longitud1, longitud2, x, y;
	printf("Ingrese una cadena de caracteres: ");
	scanf("%s", &cadena1);
	longitud1=strlen(cadena1);
	printf("\nIngrese otra cadena de caracteres: ");
	scanf("%s", &cadena2);
	longitud2=strlen(cadena2);
	if(longitud1==longitud2)
	{
		for(x=0;x<longitud1-1;x++)
    	{	
	    	for(y=0;y<longitud1-x-1;y++)
	    	{
	    		if(cadena1[y] > cadena1[y+1])
	    		{
	    			aux = cadena1[y];
	    			cadena1[y] = cadena1[y+1];
	    			cadena1[y+1] = aux;
				}
			}
		}
		for(x=0;x<longitud2-1;x++)
    	{	
	    	for(y=0;y<longitud2-x-1;y++)
	    	{
	    		if(cadena2[y] > cadena2[y+1])
	    		{
	    			aux = cadena2[y];
	    			cadena2[y] = cadena2[y+1];
	    			cadena2[y+1] = aux;
				}
			}
		}
		int resultado = strcmp(cadena1, cadena2);
		if(resultado==0)
		{
			printf("\nEs un anagrama.");
		}
		else
		{
			printf("\nNo es un anagrama.");
		}
	}
	else
	{
		printf("\nNo es un anagrama.");
	}
}
