package bsu.rfe.java.group7.lab3.Lashkin.varB1;



import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
// (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
// стога сена - таблица
    private String needle = null;
    private DecimalFormat formatter =
            (DecimalFormat)NumberFormat.getInstance();
    // Дополнительный метод для проверки, четности целой части
    private boolean Whole_Part_Even(String number) {
        String[] str = number.split("\\.");
        int num = Integer.parseInt(str[0]);
        if (num %2 == 0) {
            return true;
        }

        return false;
    }
    public GornerTableCellRenderer() {
// Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
// Не использовать группировку (т.е. не отделять тысячи
// ни запятыми, ни пробелами), т.е. показывать число как "1000",
// а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
// Установить в качестве разделителя дробной части точку, а не
// запятую. По умолчанию, в региональных настройках
// Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble =
                formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
// Разместить надпись внутри панели
        panel.add(label);
// Установить выравнивание надписи по левому краю панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {
// Преобразовать type в строку с помощью форматировщика
        String formattedType = formatter.format(value);
// Установить текст надписи равным строковому представлению числа
        label.setText(formattedType);
       
        if (col==1 && needle!=null && needle.equals(formattedType)) {
// Номер столбца = 1 (т.е. второй столбец) + иголка не null
// (значит что-то ищем) +
// значение иголки совпадает со значением ячейки таблицы -
// окрасить задний фон панели в зеленый цвет
            panel.setBackground(Color.GREEN);
            // если целая часть четная, то окрашиваем в желтый
        } else if (Whole_Part_Even(formattedType)) {
            panel.setBackground(Color.YELLOW);
        } else {
// Иначе - в обычный синий
            panel.setBackground(Color.BLUE);
        }
        return panel;
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }

}
