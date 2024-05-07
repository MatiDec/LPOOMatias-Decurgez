/*Realizar un programa que realice las siguientes acciones
a. Guarde el registro del nombre y el puntaje de un jugador (solicitando el ingreso
de los datos del usuario)
b. Muestre el resultado de los 10 mejores jugadores ordenados por mayor
puntaje.
c. Realice y muestre una búsqueda de un Nombre y devuelva sus resultados
NOTA: el registro debe permanecer aunque se apague la PC.*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
struct datostemp{
	char nombretemp[20];
	int puntajetemp;
};
void menu();
void mostrarDatos(struct datostemp *datos, int cantidad);
void ordenarPorPuntaje(struct datostemp *datos, int cantidad);

int main()
{
	FILE * fp;
	fp = fopen("jugadores.txt", "a");
	char nombre[20], nombre_leido[20], nombrebuscar[20];
	int opcion, puntaje, puntaje_leido, cantidad, encontrado;
	struct datostemp datostemp, datos[100];
	do
	{
		system("CLS");
		menu();
		scanf("%d", &opcion);
		switch(opcion)
		{
			case 1:
				fp = fopen("jugadores.txt", "a+");
				system("CLS");
				fflush(stdin);
				printf("Ingrese su nombre: ");
				gets(nombre);
				printf("\nIngrese su puntaje: ");
				scanf("%d", &puntaje);
            	fprintf(fp, "%s %d", nombre, puntaje);
				fclose(fp);
				system("PAUSE");
				system("CLS");
			break;
			case 2:
				fp = fopen("jugadores.txt", "r"); // Reabrir el archivo
			    if (fp == NULL)
				{
			        printf("Error al abrir el archivo.\n");
			        break;
			    }
			    cantidad = 0; // Reiniciar la cantidad de datos
			    while (fscanf(fp, "%s %d", datos[cantidad].nombretemp, &datos[cantidad].puntajetemp) == 2)
				{
			        cantidad++; // Incrementamos la cantidad por cada dato leído
			    }
			    fclose(fp); // Cerrar el archivo después de leer los datos
			    ordenarPorPuntaje(datos, cantidad);
			    printf("Datos ordenados por puntaje:\n");
			    mostrarDatos(datos, cantidad);
			break;
			case 3:
				system("CLS");
			    fp = fopen("jugadores.txt", "r");
			    if (fp == NULL)
				{
			        printf("Error al abrir el archivo.\n");
			        break;
			    }
			    fflush(stdin);
			    printf("Ingrese el nombre de la persona que busca: ");
			    gets(nombrebuscar);
				while(fscanf(fp, "%s %d", datostemp.nombretemp, &datostemp.puntajetemp) != EOF)
				{
			        if(strcmp(datostemp.nombretemp, nombrebuscar) == 0)
					{
	                    printf("Nombre: %s\nPuntaje: %d\n", datostemp.nombretemp, datostemp.puntajetemp);
	                    encontrado = 1;
	                    break;
			        }
	            }
	            if(!encontrado)
	            {
	            	system("CLS");
	            	printf("Persona no encontrada.\n");
	            	system("PAUSE");
	            	system("CLS");
	            	break;
				}
			    fclose(fp);
			    system("PAUSE");
			    system("CLS");
			break;
			case 4:
				system("CLS");
				printf("Programa Finalizado.\n");
			break;
		}
	}while(opcion!=4);
}

void menu()
{
	printf("1. Guardar nombre y puntaje\n2. Mejores jugadores\n3. Busqueda de jugadores\n4. Salir\nOpcion: ");
}
void ordenarPorPuntaje(struct datostemp *datos, int cantidad)
{
    int x, y;
    struct datostemp temp;

    for (x = 0; x < cantidad - 1; x++)
	{
        for (y = 0; y < cantidad - x - 1; y++)
		{
            if (datos[y].puntajetemp < datos[y + 1].puntajetemp)
			{
                temp = datos[y];
                datos[y] = datos[y + 1];
                datos[y + 1] = temp;
            }
        }
    }
}
void mostrarDatos(struct datostemp *datos, int cantidad) {
    int x;
    system("CLS");
    printf("Nombre / Puntaje\n");
    for (x = 0; x < cantidad; x++) {
        printf("%s / %d\n", datos[x].nombretemp, datos[x].puntajetemp);
    }
    system("PAUSE");
    system("CLS");
}
