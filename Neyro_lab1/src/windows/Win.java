package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lab.Perceptron;

public class Win extends JFrame
{
	private static final long	serialVersionUID	= 1L;

	public final static int		WIDTH				= 300;
	public final static int		HEIGHT				= 600;

	private File				selectedFile		= null;

	private Perceptron			web					= new Perceptron();
	private Vector<Double>		in_x				= null;

	private int					techer_count		= 5;

	// private main = null;

	public Win()
	{
		/*
		 * Определяем размер окна
		 * 
		 * WIDTH - ширина окна
		 * HEIGHT - высота окна
		 */
		setSize(WIDTH, HEIGHT);

		// Определяем положение окна на рабочем столе - выставляем по центру
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);

		// Определяем заголовок окна
		setTitle("Нейрокомпьютеры - Лаб 1 - Демьянов К.А. ЭВМ-92");

		// принудительное завершение работы программы при закрытие окна
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().add(mainPanel());

		teacher();
		
		setVisible(true);

		validate();
	}

	/*
	 * Создание основной панели
	 * 
	 * панели вкладок
	 */
	private JPanel mainPanel()
	{

		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Панель кнопок
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		mainPanel.add(buttonsPanel, BorderLayout.NORTH);

		// Взять изображение
		JButton addButton = new JButton("Выбор изображения");
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Вызов диалога
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnValue = fileChooser.showOpenDialog(new JLabel());

				// Получение выбранного файла
				if (returnValue != JFileChooser.CANCEL_OPTION)
				{
					selectedFile = fileChooser.getSelectedFile();

					in_x = abs(selectedFile);
					// web.getNeurons().get(0).newWeigth(in_x);

					// обновление формы
					getContentPane().removeAll();
					getContentPane().add(mainPanel());
					validate();
				}
			}
		});
		buttonsPanel.add(addButton, null);

		// Кнопка обучения сети
		JButton techButton = new JButton("Учить");
		techButton.addActionListener(new ActionListener()
		{
			/**
			 * Обучение сети
			 */
			public void actionPerformed(ActionEvent e)
			{
				for (int number = 0; number < 10; number++)
				{
					// цикл цифр
					// number - обучаемая цифра
					System.out.println("Число: " + number);

					for (int i = 0; i < 12; i++)
					{
						// цикл обучения

						// обучаемый файл
						File f = new File("data/" + Integer.toString(number) + "." + Integer.toString(i) + ".jpg");

						if (f.isFile())
						{
							// количество обучений одного значения
							int z = techer_count;

							while (z-- > 0)
							{
								// файл учителя(эталон)
								File fEtalon = new File("data/" + Integer.toString(number) + ".1.jpg");

								// обучение
								web.getNeurons().get(number).changeWeigths(abs(f), abs(fEtalon));
							}
						}
					}
				}
				System.out.println("Обучили.");
			}
		});
		//buttonsPanel.add(techButton, null);

		// Поиск цифры по входному изображению
		JButton actButton = new JButton("Проверить");
		actButton.addActionListener(new ActionListener()
		{
			/**
			 * вывод числа, изображённого на рисунке
			 */
			public void actionPerformed(ActionEvent e)
			{
				Vector<Double> y = web.recognize(in_x);

				int number = 0;
				Double y_number = y.get(0);
				String text = "0: " + y.get(0) + "\r\n";
				System.out.println();
				System.out.println("0: " + y.get(0));

				for (int i = 1; i < y.size(); i++)
				{

					System.out.println(i + ": " + y.get(i));
					text += i + ": " + y.get(i) + "\r\n";
					if (y_number < y.get(i))
					{
						y_number = y.get(i);
						number = i;
					}
				}
				
				System.out.println();
				
				if(y_number > 0)
				{
					System.out.println("Число: " + number + "  Значение y = " + y_number);
					text += "Число: " + number + "  Значение y = " + y_number;
				}
				else
				{
					System.out.println("Число не определено");
					text += "Число не определено";
				}
				JOptionPane.showMessageDialog(new JFrame(), text);
			}
		});
		buttonsPanel.add(actButton, null);

		if (selectedFile != null)
		{
			// вывод изображения на форму
			mainPanel.add(new JLabel(new ImageIcon(createBufferedImage(selectedFile))), BorderLayout.CENTER);
			this.setSize(WIDTH, HEIGHT);
		}
		else
		{
			// скрытие области под изображение
			mainPanel.add(new JLabel(""), BorderLayout.CENTER);
			this.setSize(WIDTH, HEIGHT - 435);
		}

		return mainPanel;
	}

	/**
	 * Подготовка изображение для вывода на панель.
	 * Увеличение изображения в 16 раз
	 * 
	 * @param file
	 *            - файл изображения
	 * @return изображение
	 */
	private BufferedImage createBufferedImage(File file)
	{
		BufferedImage image = null;

		try
		{
			image = ImageIO.read(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		int scale = 4;

		int sizeX = image.getWidth();
		int sizeY = image.getHeight();

		BufferedImage result = new BufferedImage(sizeX * scale, sizeY * scale, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = result.createGraphics();
		g.scale(scale, scale);
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return result;
	}

	/**
	 * Формирование вектора входного образа
	 * 
	 * @return вектор входного образа
	 */
	private Vector<Double> abs(File file)
	{
		BufferedImage img = null;

		Vector<Double> new_x = new Vector<Double>();

		try
		{
			if (file != null)
			{
				img = ImageIO.read(file);

				for (int y = 0; y < img.getHeight(); y++)
				{
					for (int x = 0; x < img.getWidth(); x++)
					{
						Double red = 0.299 * (double) (new Color(img.getRGB(x, y)).getRed() / 255.0);
						Double green = 0.587 * (double) (new Color(img.getRGB(x, y)).getGreen() / 255.0);
						Double blue = 0.114 * (double) (new Color(img.getRGB(x, y)).getBlue() / 255.0);

						Double result = (new BigDecimal(red + green + blue).setScale(1, RoundingMode.HALF_UP)).doubleValue();

						new_x.add(result);
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return new_x;
	}
	
	/**
	 * Обучние сети
	 */
	private void teacher()
	{
		for (int number = 0; number < 10; number++)
		{
			// цикл цифр
			// number - обучаемая цифра
			System.out.println("Число: " + number);

			for (int i = 0; i < 12; i++)
			{
				// цикл обучения

				// обучаемый файл
				File f = new File("data/" + Integer.toString(number) + "." + Integer.toString(i) + ".jpg");

				if (f.isFile())
				{
					// количество обучений одного значения
					int z = techer_count;

					while (z-- > 0)
					{
						// файл учителя(эталон)
						File fEtalon = new File("data/" + Integer.toString(number) + ".1.jpg");

						// обучение
						web.getNeurons().get(number).changeWeigths(abs(f), abs(fEtalon));
					}
				}
			}
		}
		System.out.println("Обучили.");
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		new Win();
	}
}
