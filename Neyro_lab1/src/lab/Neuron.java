package lab;

import java.util.Random;
import java.util.Vector;

public class Neuron
{
	private Vector<Double>	weigths	= null;

	/**
	 * Создание нейрона, определение первоначальных весов
	 * weigth[i] = -0.5 .. 0.5
	 * 
	 * @param count_x
	 *            количество входов в сеть
	 */
	public Neuron(int count_x)
	{
		Random random = new Random();
		this.weigths = new Vector<Double>();

		for (int i = 0; i < count_x; i++)
		{
			this.weigths.add(((double) (random.nextInt(11) - 5) / 10.0));
		}
	}

	/**
	 * Обучение нейрона, смена весовых коэффицентов
	 * 
	 * @param x
	 *            - входной вектор
	 * @param y
	 *            - желаемый вектор для данного нейрона
	 */
	public void changeWeigths(Vector<Double> x, Vector<Double> y)
	{
		// константа скорости обучения
		Double n = 1.0;

		for (int i = 0; i < weigths.size(); i++)
		{
			// вычисление ошибки выхода нейрона
			Double d = x.get(i) * (y.get(i) - x.get(i));

			// коррекция весовых коэффицентов
			weigths.set(i, weigths.get(i) + n * x.get(i) * d);
		}
	}

	/**
	 * Генерация выходного нейронного сигнала
	 * y = Fa(S);
	 * 
	 * @param x
	 *            - входной вектор
	 * @return функция активация ( Fa(S) )
	 */
	public Double fActivation(Vector<Double> x)
	{
		return 1 / (1 + Math.exp(-(this.sum(x))));
	}

	/**
	 * Блок сумматора <br>
	 * S = x1*w1 + x2*w2 + .. + xn*wn ( n = 256 )
	 * 
	 * @param x
	 *            - входной вектор
	 * @return вектор S
	 */
	private Double sum(Vector<Double> x)
	{
		Double s = 0.0;

		for (int i = 0; i < x.size(); i++)
		{
			s += x.get(i) * this.weigths.get(i);
		}

		return s;
	}
}
