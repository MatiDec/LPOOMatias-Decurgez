#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void menu();
void minuscula(char *cadena, int longitud);
struct datostemp{
	char nombretemp[20];
	char apellidotemp[20];
	int dnitemp;
};
void ordenarPorDNI(struct datostemp *datos, int cantidad);
void ordenarPorNombre(struct datostemp *datos, int cantidad);
void ordenarPorApellido(struct datostemp *datos, int cantidad);
void mostrarDatos(struct datostemp *datos, int cantidad);
int main()
{
	//Declaracion de variables
	char nombre[20], apellido[20], nombrebuscar[20], nombre_actual[20], apellidobuscar[20], apellido_actual[20];//La cadena "datos" almacenará los datos del archivo en la función del caso 2
	int dni, opcion, dnibuscar, dni_actual, longitud, x, encontrado=0, dni_leido, cantidad;
	struct datostemp datostemp, datos[100];
	char nombre_leido[20], apellido_leido[20];
	FILE * fp;
	fp = fopen("datos2.txt", "a+");
	do
	{
		menu();
		scanf("%d", &opcion);
		switch(opcion)
		{
			case 1:
				fp = fopen("datos2.txt", "a+");
				system("CLS");
				fflush(stdin);
				printf("Ingrese su nombre: ");
				gets(nombre);
				longitud = sizeof(nombre);
				minuscula(nombre, longitud);//pasar a minuscula para manejar los datos
				fflush(stdin);
				printf("\nIngrese su apellido: ");
				gets(apellido);
				longitud = sizeof(apellido);
				minuscula(apellido, longitud);
				printf("\nIngrese su DNI: ");
				scanf("%d", &dni);
				fseek(fp, 0, SEEK_SET);  // Movemos el puntero al inicio del archivo
				while (fscanf(fp, "%s %s %d", nombre_leido, apellido_leido, &dni_leido) != EOF) {
                    if (dni == dni_leido)
					{
                        printf("Persona ya registrada\n");
                        break;
                    }
                }

                if (feof(fp))
				{
                    fprintf(fp, "%s %s %d", nombre, apellido, dni);
                }   
			    
				fclose(fp);
				system("PAUSE");
				system("CLS");
			break;
			case 2:
		    system("CLS");
		    fp = fopen("datos2.txt", "r");
		    if (fp == NULL)
			{
		        printf("Error al abrir el archivo.\n");
		        break;
		    }
		    fflush(stdin);
		    printf("Ingrese el nombre de la persona que busca: ");
		    gets(nombrebuscar);
		    longitud = strlen(nombrebuscar);
		    minuscula(nombrebuscar, longitud);
		    fflush(stdin);
		    printf("Ingrese el apellido de la persona que busca: ");
		    gets(apellidobuscar);
		    longitud = strlen(apellidobuscar);
		    minuscula(apellidobuscar, longitud);
			while(fscanf(fp, "%s %s %d", datostemp.nombretemp, datostemp.apellidotemp, &datostemp.dnitemp) != EOF)
			{
		        if(strcmp(datostemp.nombretemp, nombrebuscar) == 0 && strcmp(datostemp.apellidotemp, apellidobuscar) == 0)
				{
                    printf("Nombre: %s\nApellido: %s\nDNI: %d\n", datostemp.nombretemp, datostemp.apellidotemp, datostemp.dnitemp);
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
		    case 3:
		    	fp = fopen("datos2.txt", "r"); // Reabrir el archivo
			    if (fp == NULL)
				{
			        printf("Error al abrir el archivo.\n");
			        break;
			    }
			    cantidad = 0; // Reiniciar la cantidad de datos
			    while (fscanf(fp, "%s %s %d", datos[cantidad].nombretemp, datos[cantidad].apellidotemp, &datos[cantidad].dnitemp) == 3)
				{
			        cantidad++; // Incrementamos la cantidad por cada dato leído
			    }
			    fclose(fp); // Cerrar el archivo después de leer los datos
			    ordenarPorDNI(datos, cantidad);
			    printf("Datos ordenados por DNI:\n");
			    mostrarDatos(datos, cantidad);
		    break;
		    case 4:
		    	fp = fopen("datos2.txt", "r"); // Reabrir el archivo
			    if (fp == NULL) {
			        printf("Error al abrir el archivo.\n");
			        break;
			    }
			    cantidad = 0; // Reiniciar la cantidad de datos
			    while (fscanf(fp, "%s %s %d", datos[cantidad].nombretemp, datos[cantidad].apellidotemp, &datos[cantidad].dnitemp) == 3) {
			        cantidad++; // Incrementamos la cantidad por cada dato leído
			    }
			    fclose(fp); // Cerrar el archivo después de leer los datos
			    ordenarPorNombre(datos, cantidad);
			    printf("Datos ordenados por nombre:\n");
			    mostrarDatos(datos, cantidad);
		    break;
		    case 5:
		    	fp = fopen("datos2.txt", "r"); // Reabrir el archivo
			    if (fp == NULL) {
			        printf("Error al abrir el archivo.\n");
			        break;
			    }
			    cantidad = 0; // Reiniciar la cantidad de datos
			    while (fscanf(fp, "%s %s %d", datos[cantidad].nombretemp, datos[cantidad].apellidotemp, &datos[cantidad].dnitemp) == 3) {
			        cantidad++; // Incrementamos la cantidad por cada dato leído
			    }
			    fclose(fp); // Cerrar el archivo después de leer los datos
			    ordenarPorApellido(datos, cantidad);
			    printf("Datos ordenados por nombre:\n");
			    mostrarDatos(datos, cantidad);
		    break;
			case 6:
				system("CLS");
				printf("Programa Finalizado por el usuario.");
			break;
		}
	}while(opcion!=6);
}

void menu()
{
	printf("--Matias Decurgez: Menu--\n1. Ingresado de datos\n2. Muestra de datos\n3. Ordenar por DNI\n4. Ordenar por Nombre\n5. Ordenar por Apellido\n6. Salir\nOpcion: ");
}

void minuscula(char *cadena, int longitud)
{
	int x;
	
	for(x=0;x<longitud;x++)
	{
		if(*(cadena+x)>=65&&*(cadena+x)<=90)
		{
			*(cadena+x) = *(cadena+x)+32;
		}
	}
}
void ordenarPorNombre(struct datostemp *datos, int cantidad)
{
    int x, y;
    struct datostemp temp;

    for (x = 0; x < cantidad - 1; x++)
	{
        for (y = 0; y < cantidad - x - 1; y++)
		{
            if (strcmp(datos[y].nombretemp, datos[y + 1].nombretemp) > 0)
			{
                temp = datos[y];
                datos[y] = datos[y + 1];
                datos[y + 1] = temp;
            }
        }
    }
}
void ordenarPorDNI(struct datostemp *datos, int cantidad)
{
    int x, y;
    struct datostemp temp;

    for (x = 0; x < cantidad - 1; x++)
	{
        for (y = 0; y < cantidad - x - 1; y++)
		{
            if (datos[y].dnitemp > datos[y + 1].dnitemp)
			{
                temp = datos[y];
                datos[y] = datos[y + 1];
                datos[y + 1] = temp;
            }
        }
    }
}
void ordenarPorApellido(struct datostemp *datos, int cantidad)
{
    int x, y;
    struct datostemp temp;

    for (x = 0; x < cantidad - 1; x++)
	{
        for (y = 0; y < cantidad - x - 1; y++)
		{
            if (strcmp(datos[y].apellidotemp, datos[y + 1].apellidotemp) > 0)
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
    printf("Nombre / Apellido / DNI\n");
    for (x = 0; x < cantidad; x++) {
        printf("%s / %s / %d\n", datos[x].nombretemp, datos[x].apellidotemp, datos[x].dnitemp);
    }
    system("PAUSE");
    system("CLS");
}
