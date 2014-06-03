package examples;


import java.io.IOException;
import java.io.RandomAccessFile;

public class ConsoleSave {
    RandomAccessFile file;//переменная для доступа к файлу из программы
    int[] number;//числовые поля записей в картотеке
    String[] stroka;//строковые поля записей в картотеке

    public static void main(String args[]) {
        ConsoleSave prog = new ConsoleSave();
    }

    ConsoleSave() {
        String[][] str = {
                {"апельсин", "груша", "яблоко"},
                {"виноград", "земляника", "клюква", "малина", "облепиха", "слива", "черешня"},
                {"берёза", "дуб", "ель", "ива", "клён", "липа", "ольха", "сосна", "тополь", "ясень"}
        };
        for (int i = 0; i < str.length; i++) {//поочерёдно для всех картотек
            //начнём с выделения памяти
            number = new int[str[i].length];
            stroka = new String[str[i].length];
            //память выделена - можно заполнять массивы
            for (int j = 0; j < str[i].length; j++) {//инициализация очередной картотеки
                number[j] = i * 10 + j;
                stroka[j] = str[i][j];
            }
            //все данные подготовлены
            save();//собственно сохранение
        }
    }

    void save() {
        String filename = "data" + number.length + ".txt";//имя файла картотеки
        try {
            file = new RandomAccessFile(filename, "rw");//попытка открыть файл
        } catch (Throwable e) {
            // Здесь нужно сообщить об ошибке открытия файла
            // но в этом примере у нас ошибок не ожидается
            // поэтому и обработку для них писать не будем
        }
        try {//файл был открыт - попробуем записать в него
            file.writeInt(number.length);//записываем число элементов картотеки
            for (int i = 0; i < number.length; i++) {
                file.writeInt(number[i]);//записываем число
                file.writeUTF(new String(stroka[i].getBytes("UTF-8"), "UTF-8"));//записываем строку
            }
            file.close();//всё записали - надо закрыть файл
        } catch (IOException e) {
            // Здесь нужно сообщить об ошибке записи
            // но в этом примере у нас ошибок не ожидается
            // поэтому и обработку для них писать не будем
        }
        System.out.println("Save file " + filename);//рапорт о проделанной работе
    }
}