package ej2;

public class Ejercicio2 {
	public static void main(String[] args)
	{
		int num = 50, cont = 0, x;
		do
		{
			cont = 0;
			for(x=1;x<=num;x++)
			{
				if(num%x==0)
				{
					cont++;
				}
			}
			if(cont==2)
			{
				System.out.println("primo\n");
			}
			else
			{
				System.out.print(num + ": ");
                for (x = 1; x <= num; x++)
                {
                    if (num % x == 0) {
                        System.out.print(x + " ");
                    }
                }
                System.out.println("\n");
			}
			num++;
		}while(num<=100);
	}
}
