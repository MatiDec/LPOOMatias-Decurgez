package ej5;

import java.util.Scanner;

public class Ejercicio5 {
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		char opcion = 0, general = 0, operacion = 0;
		double num1 = 0, num2 = 0, resultado = 0;
		do
		{
			System.out.println("Ingrese el numero 1: ");
			num1 = scanner.nextDouble();
			System.out.println("Ingrese el numero 2: ");
			num2 = scanner.nextDouble();
			do
			{
				menu();
				operacion = scanner.next().charAt(0);
				switch (operacion)
				{
					case '+':
						resultado = num1 + num2;
						System.out.println(resultado);
						System.out.println("Desea continuar operando? (s/n): ");
						opcion = scanner.next().charAt(0);
					break;
					case '-':
						resultado = num1 - num2;
						System.out.println(resultado);
						System.out.println("Desea continuar operando? (s/n): ");
						opcion = scanner.next().charAt(0);
					break;
					case '*':
						resultado = num1 * num2;
						System.out.println(resultado);
						System.out.println("Desea continuar operando? (s/n): ");
						opcion = scanner.next().charAt(0);
					break;
					case '/':
						if(num2 == 0)
						{
							System.out.println("Error division por 0");
							break;
						}
						resultado = num1 / num2;
						System.out.println(resultado);
						System.out.println("Desea continuar operando? (s/n): ");
						opcion = scanner.next().charAt(0);
					break;
				}
			}while(opcion!='n');
			System.out.println("Desea cambiar los numeros y seguir el programa? (s/n): ");
			general = scanner.next().charAt(0);
		}while(general!='n');
		System.out.println("Programa finalizado.");
		scanner.close();
	}
	
	public static void menu()
	{
		System.out.println("\nQue calculo va a realizar? (+, -, *, /): ");
	}
}
