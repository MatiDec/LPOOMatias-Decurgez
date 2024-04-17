/*5. Concatenar dos cadenas usando punteros y funciones.*/
#include <stdio.h>
#include <string.h>

char cadena1[20];
char cadena2[20];
char cadenaconcat[40];

void concatenar(char *cadena1, char *cadena2);
int main()
{
	printf("Ingrese la primera cadena de caracteres: ");
	fgets(cadena1, sizeof(cadena1), stdin);
    cadena1[strcspn(cadena1, "\n")] = '\0'; //Borra un salto de línea de más
	printf("Ingrese la segunda cadena de caracteres: ");
	fgets(cadena2, sizeof(cadena2), stdin);
	cadena2[strcspn(cadena2, "\n")] = '\0';
	concatenar(cadena1, cadena2);
	printf("\nLa cadena concatenada es: %s", cadena1);
}

void concatenar(char *cadena1, char *cadena2)
{
	strcat(cadena1, cadena2);
}
