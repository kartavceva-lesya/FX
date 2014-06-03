package examples;


import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UTFDataFormatException;

public class WindowedLoad extends Frame {
    String filename = "data.txt";//имя файла картотеки
    RandomAccessFile file;//переменная для доступа к файлу из программы
    int limit = 10;//максимальное число записей в картотеке
    int[] number;//числовые поля записей в картотеке
    String[] stroka;//строковые поля записей в картотеке

    int loadError = 0;//признак ошибки чтения файла, если 0 ошибок не было

    public static void main(String[] args) {
        WindowedLoad win = new WindowedLoad();
    }

    WindowedLoad() {
        super("Windowed Application");
        //выделяем память под массивы картотеки
        number = new int[limit];
        stroka = new String[limit];
        loadError = load();//загрузить картотеку из файла
        pack();
        setSize(600, 400);
        show();
    }

    public void paint(Graphics g) {
        if (loadError != 0) {
            g.setColor(Color.RED);
            g.drawString("Внимание! Ошибка чтения файла", 10, 40);
            switch (loadError) {
                case 1:
                    g.drawString("Не удалось открыть файл", 10, 60);
                    break;
                case 2:
                    g.drawString("Кодировка строки не UTF-8", 10, 60);
                    break;
                case 3:
                    g.drawString("Неожиданный конец файла", 10, 60);
                    break;
                case 4:
                    g.drawString("Общая ошибка чтения из файла", 10, 60);
                    break;
            }
        } else {
            g.setColor(Color.GREEN);
            g.drawString("Файл прочитан без ошибок", 10, 40);
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i < number.length; i++) {
            g.drawString("" + number[i], 10, 100 + i * 20);
            if (stroka[i] != null)
                g.drawString(stroka[i], 100, 100 + i * 20);
        }
    }

    int load() {//вот здесь и происходит чтение из файла
        try {
            file = new RandomAccessFile(filename, "r");//открыть только для чтения
        } catch (Throwable e) {
            return 1;//сообщить об ошибке открытия файла и прекратить работу
        }
        try {//файл был открыт - попробуем прочесть его
            int i = 0;
            while (file.getFilePointer() < file.length()) {//файл ещё не исчерпан
                number[i] = file.readInt();
                stroka[i] = file.readUTF();
                ++i;
                if (i >= number.length) break;//если массив заполнен - дальше не читать
            }
            file.close();//закрыть файл
        } catch (UTFDataFormatException e) {
            return 2;//сообщить об ошибке чтения файла и прекратить работу
        } catch (EOFException e) {
            return 3;//сообщить об ошибке чтения файла и прекратить работу
        } catch (IOException e) {
            return 4;//сообщить об ошибке чтения файла и прекратить работу
        }
        return 0;
    }

    public boolean handleEvent(Event event) {
        if (event.id == Event.WINDOW_DESTROY) {
            System.exit(0);
            return true;
        }
        return false;
    }
}