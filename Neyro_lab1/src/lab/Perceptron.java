package lab;

import java.util.Vector;

public class Perceptron {
    // количество входов сеть
    // рисунок 16*16=256
    private int count_x = 64 * 64;

    // количество нейронов
    // количество цифр = 10
    private int count_neurons = 10;

    private Vector<Neuron> neurons = null;

    public Perceptron() {
	this.neurons = new Vector<Neuron>();

	for (int i = 0; i < this.count_neurons; i++) {
	    this.neurons.add(new Neuron(this.count_x));
	}
    }

    /**
     * Распознавание образа
     * 
     * @param x
     *            - входной вектор
     * @return выходной образ
     */
    public Vector<Double> recognize(Vector<Double> x) {
	Vector<Double> y = new Vector<Double>();

	for (int i = 0; i < neurons.size(); i++) {
	    y.add(neurons.get(i).fActivation(x));
	}

	return y;
    }

    public Vector<Neuron> getNeurons() {
	return neurons;
    }
}
