#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void menu();
struct datostemp{
	char nombretemp[20];
	char apellidotemp[20];
	int dnitemp;
};
int main()
{
	//Declaracion de variables
	char nombre[20], apellido[20], datos[200];//La cadena "datos" almacenar� los datos del archivo en la funci�n del caso 2
	int dni, opcion, dnibuscar, dni_actual, encontrado = 0;
	struct datostemp datostemp;
	FILE * fp;
	FILE * fpr;
	fp = fopen("datos.txt", "a");
	fpr = fopen("datos.txt", "r");
	do
	{
		menu();
		scanf("%d", &opcion);
		switch(opcion)
		{
			case 1:
				fp = fopen("datos.txt", "a");
				system("CLS");
				fflush(stdin);
				printf("Ingrese su nombre: ");
				gets(nombre);
				fflush(stdin);
				printf("\nIngrese su apellido: ");
				gets(apellido);
				printf("\nIngrese su DNI: ");
				scanf("%d", &dni);
				system("CLS");
				fprintf(fp, "%s %s %d", nombre, apellido, dni);
				printf("Datos Ingresados correctamente.\n");
				fclose(fp);
				system("PAUSE");
				system("CLS");
			break;
			case 2:
				system("CLS");
				fp = fopen("datos.txt", "r");
				printf("Ingrese el DNI de la persona que busca: ");
				scanf("%d", &dnibuscar);//Variable que se usar� para buscar este valor en el archivo
				
                while(fscanf(fp, "%s %s %d", datostemp.nombretemp, datostemp.apellidotemp, &datostemp.dnitemp) != EOF)
				{
					if(dnibuscar == datostemp.dnitemp)
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
				system("CLS");
				printf("Programa Finalizado por el usuario.");
			break;
		}
	}while(opcion!=3);
}

void menu()
{
	printf("--Matias Decurgez: Menu--\n1. Ingresado de datos\n2. Muestra de datos\n3. Salir\nOpcion: ");
}
