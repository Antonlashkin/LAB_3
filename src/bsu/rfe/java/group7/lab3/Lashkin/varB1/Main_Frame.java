package bsu.rfe.java.group7.lab3.Lashkin.varB1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

    @SuppressWarnings("serial")
    public class Main_Frame extends JFrame {
        private static final int WIDTH = 700;
        private static final int HEIGHT = 500;
        private Double[] coefficients;
        private JFileChooser fileChooser = null;

        private JMenuItem saveToTextMenuItem;
        private JMenuItem saveToGraphicsMenuItem;
        private JMenuItem searchValueMenuItem;
        private JMenuItem showInfoAboutProgramItem;

        private JTextField textFieldFrom;
        private JTextField textFieldTo;
        private JTextField textFieldStep;
        private Box hBoxResult;

        private GornerTableCellRenderer renderer = new
                GornerTableCellRenderer();

        private GornerTableModel data;

        public Main_Frame(Double[] coefficients) {
            super("Табулирование многочлена на отрезке по схеме Горнера");
            this.coefficients = coefficients;
            setSize(WIDTH, HEIGHT);
            Toolkit kit = Toolkit.getDefaultToolkit();
            setLocation((kit.getScreenSize().width - WIDTH) / 2,
                    (kit.getScreenSize().height - HEIGHT) / 2);
            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);

            JMenu fileMenu = new JMenu("Файл");
            menuBar.add(fileMenu);
            JMenu tableMenu = new JMenu("Таблица");
            menuBar.add(tableMenu);
            JMenu referenceMenu = new JMenu("Справка");
            menuBar.add(referenceMenu);
            Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
                public void actionPerformed(ActionEvent event) {
                    if (fileChooser == null) {
                        // Если экземпляр диалогового окна "Открыть файл" ещѐ не создан,
                        // то создать его
                        fileChooser = new JFileChooser();
                        // и инициализировать текущей директорией
                        fileChooser.setCurrentDirectory(new File("."));
                    }
                    // Показать диалоговое окно
                    if (fileChooser.showSaveDialog(Main_Frame.this) ==
                            JFileChooser.APPROVE_OPTION)
                        // Если результат его показа успешный,
                        // сохранить данные в текстовый файл
                        saveToTextFile(fileChooser.getSelectedFile());
                }
            };
            // Добавить соответствующий пункт подменю в меню "Файл"
            saveToTextMenuItem = fileMenu.add(saveToTextAction);
            // По умолчанию пункт меню является недоступным (данных еще нет)
            saveToTextMenuItem.setEnabled(false);
            // Создать новое "действие" по сохранению в текстовый файл
            Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
                public void actionPerformed(ActionEvent event) {
                    if (fileChooser == null) {
                        // Если экземпляр диалогового окна
                        // "Открыть файл" еще не создан,
                        // то создать его
                        fileChooser = new JFileChooser();
                        // и инициализировать текущей директорией
                        fileChooser.setCurrentDirectory(new File("."));
                    }
                    if (fileChooser.showSaveDialog(Main_Frame.this) ==
                            JFileChooser.APPROVE_OPTION) ;
                    // Если результат его показа успешный,
                    // сохранить данные в двоичный файл
                    saveToGraphicsFile(
                            fileChooser.getSelectedFile());
                }
            };
            // Добавить соответствующий пункт подменю в меню "Файл"
            saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
            // По умолчанию пункт меню является недоступным(данных еще нет)
            saveToGraphicsMenuItem.setEnabled(false);
            // Создать новое действие по поиску значений многочлена
            Action searchValueAction = new AbstractAction("Найти значение многочлена") {
                public void actionPerformed(ActionEvent event) {
                    String value = JOptionPane.showInputDialog(Main_Frame.this, "Введите значение для поиска",
                            "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                    // Установить введенное значение в качестве иголки
                    renderer.setNeedle(value);
                    getContentPane().repaint();
                }
            };
            // Добавить действие в меню "Таблица"
            searchValueMenuItem = tableMenu.add(searchValueAction);
            // По умолчанию пункт меню является недоступным (данных ещѐ нет)
            searchValueMenuItem.setEnabled(false);
            // Создать новое действие показа информации о программе
            Action showInfoProgramAction = new AbstractAction("О программе") {
                public void actionPerformed(ActionEvent event) {
                    JOptionPane.showMessageDialog(Main_Frame.this, "Лашкин, 7 группа");
                }
            };
            showInfoAboutProgramItem = referenceMenu.add(showInfoProgramAction);
            showInfoAboutProgramItem.setEnabled(true);

            JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
            textFieldFrom = new JTextField("0.0", 10);
            textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
            JLabel labelForTo = new JLabel("до:");
            textFieldTo = new JTextField("1.0", 10);
            textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
            JLabel labelForStep = new JLabel("с шагом:");
            textFieldStep = new JTextField("0.1", 10);
            textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

            Box hboxRange = Box.createHorizontalBox();
            hboxRange.setBorder(BorderFactory.createBevelBorder(1));
            hboxRange.add(Box.createHorizontalGlue());
            hboxRange.add(labelForFrom);
            hboxRange.add(Box.createHorizontalStrut(10));
            hboxRange.add(textFieldFrom);
            hboxRange.add(Box.createHorizontalStrut(20));
            hboxRange.add(labelForTo);
            hboxRange.add(Box.createHorizontalStrut(10));
            hboxRange.add(textFieldTo);
            hboxRange.add(Box.createHorizontalStrut(20));
            hboxRange.add(labelForStep);
            hboxRange.add(Box.createHorizontalStrut(10));
            hboxRange.add(textFieldStep);
            hboxRange.add(Box.createHorizontalGlue());

            hboxRange.setPreferredSize(new Dimension(
                    new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                    new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
            //...........
            getContentPane().add(hboxRange, BorderLayout.NORTH);


            //Кнопка вычислить
            JButton buttonCalc = new JButton("Вычислить");
            buttonCalc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        Double from = Double.parseDouble(textFieldFrom.getText());
                        Double to = Double.parseDouble(textFieldTo.getText());
                        Double step = Double.parseDouble(textFieldStep.getText());

                        // На основе считанных данных создать новый экземпляр модели таблицы
                        data = new GornerTableModel(from, to, step, Main_Frame.this.coefficients);
                        JTable table = new JTable(data);
                        // Установить в качестве визуализатора ячеек для класса Double разработанный визуализатор
                        table.setDefaultRenderer(Double.class, renderer);
                        table.setRowHeight(30);
                        hBoxResult.removeAll();
                        hBoxResult.add(new JScrollPane(table));
                        getContentPane().validate();

                        saveToTextMenuItem.setEnabled(true);
                        saveToGraphicsMenuItem.setEnabled(true);
                        searchValueMenuItem.setEnabled(true);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Main_Frame.this,
                                "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            // Кнопка "Очистить поля"
            JButton buttonReset = new JButton("Очистить поля");
            buttonReset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    textFieldFrom.setText("0.0");
                    textFieldTo.setText("1.0");
                    textFieldStep.setText("0.1");
                    hBoxResult.removeAll();
                    hBoxResult.add(new JPanel());
                    saveToTextMenuItem.setEnabled(false);
                    saveToGraphicsMenuItem.setEnabled(false);
                    searchValueMenuItem.setEnabled(false);
                    getContentPane().validate();
                }
            });

            Box hboxButtons = Box.createHorizontalBox();
            hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
            hboxButtons.add(Box.createHorizontalGlue());
            hboxButtons.add(buttonCalc);
            hboxButtons.add(Box.createHorizontalStrut(30));
            hboxButtons.add(buttonReset);
            hboxButtons.add(Box.createHorizontalGlue());

            hboxButtons.setPreferredSize(new Dimension(new
                    Double(hboxButtons.getMaximumSize().getWidth()).intValue(), new
                    Double(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));

            //.............
            getContentPane().add(hboxButtons, BorderLayout.SOUTH);

            hBoxResult = Box.createHorizontalBox();
            hBoxResult.add(new JPanel());
            getContentPane().add(hBoxResult, BorderLayout.CENTER);
        }

        protected void saveToGraphicsFile(File selectedFile) {
            try {
                // Создать новый байтовый поток вывода, направленный в указанный файл
                DataOutputStream out = new DataOutputStream(new
                        FileOutputStream(selectedFile));
                // Записать в поток вывода попарно значение X в точке, значение многочлена в точке
                for (int i = 0; i < data.getRowCount(); i++) {
                    out.writeDouble((Double) data.getValueAt(i, 0));
                    out.writeDouble((Double) data.getValueAt(i, 1));
                }
                out.close();
            } catch (Exception e) {
                // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
                // так как мы файл создаѐм, а не открываем для чтения
            }
        }

        protected void saveToTextFile(File selectedFile) {
            try {
                // Создать новый символьный поток вывода, направленный в указанный файл
                PrintStream out = new PrintStream(selectedFile);
                // Записать в поток вывода заголовочные сведения
                out.println("Результаты табулирования многочлена по схеме Горнера");
                out.print("Многочлен: ");
                for (int i = 0; i < coefficients.length; i++) {
                    out.print(coefficients[i] + "*X^" +
                            (coefficients.length - i - 1));
                    if (i != coefficients.length - 1)
                        out.print(" + ");
                }
                out.println("");
                out.println("Интервал от " + data.getFrom() + " до " +
                        data.getTo() + " с шагом " + data.getStep());
                out.println("====================================================");
                // Записать в поток вывода значения в точках
                for (int i = 0; i < data.getRowCount(); i++) {
                    out.println("Значение в точке " + data.getValueAt(i, 0)
                            + " равно " + data.getValueAt(i, 1));
                }
                out.close();
            } catch (FileNotFoundException e) {
                // Исключительную ситуацию "ФайлНеНайден" можно не
                // обрабатывать, так как мы файл создаѐм, а не открываем
            }
        }

        public static void main(String[] args) {
            if (args.length == 0) {
                System.out.println("Невозможно табулировать многочлен, для  которого не задано ни одного коэффициента!");
                System.exit(-1);
            }

            Double[] coefficients = new Double[args.length];
            int i = 0;
            try {
                for (String arg : args) {
                    coefficients[i++] = Double.parseDouble(arg);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка преобразования строки '" +
                        args[i] + "' в число типа Double");
                System.exit(-2);
            }
            Main_Frame frame = new Main_Frame(coefficients);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }
