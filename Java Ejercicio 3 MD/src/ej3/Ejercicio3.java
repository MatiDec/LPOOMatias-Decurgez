package ej3;

import java.util.Scanner;

public class Ejercicio3 {
	public static void main(String[] args)
	{
		int num = 0, primo = 0, x, cont = 0;
		Scanner scanner = new Scanner(System.in);
		do
		{
			cont = 0;
			primo = 0;
			System.out.println("Ingrese un numero: ");
			num = scanner.nextInt();
			for(x=1;x<=num;x++)
			{
				if(num%x==0)
				{
					cont++;
				}
			}
			if(cont==2)
			{
				primo = 1;
			}
		}while(num < 100 || primo!=1);
		System.out.println("El numero "+num+" es mayor a 100 y es primo");
	}
}
