package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

public class NotePad extends JFrame implements ActionListener {

    private static final long serialVersionUID = 7882570594466471533L;

    private TextArea text = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);

    private File file;

    // menu bar
    private MenuBar menuBar = new MenuBar();

    // first menu bar item "File"
    private Menu fileMenu = new Menu("File");
    // "File" menu items
    private MenuItem newFile = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
    private MenuItem openFile = new MenuItem("Open...", new MenuShortcut(KeyEvent.VK_O));
    private MenuItem saveFile = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
    private MenuItem saveAsFile = new MenuItem("Save As...", new MenuShortcut(KeyEvent.VK_S, true));
    private MenuItem exitFile = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_F4));


    public NotePad() {
        super("New File");

        add(text);
        setMenuBar(menuBar);

        newFile.addActionListener(this);
        fileMenu.add(newFile);
        openFile.addActionListener(this);
        fileMenu.add(openFile);
        saveFile.addActionListener(this);
        fileMenu.add(saveFile);
        saveAsFile.addActionListener(this);
        fileMenu.add(saveAsFile);
        exitFile.addActionListener(this);
        fileMenu.add(exitFile);

        menuBar.add(fileMenu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(600, 400));
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object menuItem = e.getSource();
        if (menuItem == newFile) {
            newFile();
        } else if (menuItem == openFile) {
            openFile();
        } else if (menuItem == saveFile) {
            if (file != null) {
                saveFile();
            } else {
                saveAsFile();
            }
        } else if (menuItem == saveAsFile) {
            saveAsFile();
        } else if (menuItem == exitFile) {
            System.exit(0);
        }

    }

    // creates new file
    public void newFile() {
        text.setText("");
        setTitle("New File");
        file = null;
    }

    // opens existing file
    public void openFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                file = fc.getSelectedFile();
                BufferedReader in = new BufferedReader(new FileReader(file));
                String t = "";
                String ls = System.getProperty("line.separator");
                while ((t = in.readLine()) != null) {
                    text.append(t);
                    text.append(ls);
                }
                setTitle(file.getName());
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // saves file
    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(text.getText());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // saves file with new name
    public void saveAsFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                file = fc.getSelectedFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(text.getText());
                setTitle(file.getName());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new NotePad().setVisible(true);
            }

        });

    }

}