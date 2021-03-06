
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Properties;

import javax.swing.JFrame;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.apache.log4j.Logger;
public class GUI extends JFrame{
    private static final Logger logGUI = Logger.getLogger(String.valueOf(GUI.class));
    //календарь
    UtilDateModel model1 = new UtilDateModel();
    UtilDateModel model2 = new UtilDateModel();
    Properties p = new Properties();

    JDatePanelImpl datePanel1 = new JDatePanelImpl(model1,p);
    JDatePanelImpl datePanel2 = new JDatePanelImpl(model2,p);
    JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
    JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());


    //массивы
    Object headers[];
    Object data[][];

    FilterException exept = new FilterException();
    //кнопки на панели
    private JButton save;
    private JButton open;
    private JButton add;
    private JButton edit;
    private JButton del;

    //строка сортировки
    private JComboBox sorting;
    private JButton up,down;
    private JLabel sortingLabel;


    //поиск
    private JComboBox domain;
    private JTextField field;//строка поиска
    private JButton fieldButton;//кнопка поиска
    private JLabel searchLabel;

    JTextArea textArea;//поле с текстом

    private JToolBar toolBar;
    Box  bar;

    private JScrollPane scrolltable, scrolltext;
    //таблица
    private JTable tableBase;
    private DefaultTableModel model;

    //контейнеры для верхней панели
    Box boxButon, boxFilter, boxSorting, boxReport;

    // Объявляем меню файл
    JMenuBar menuBar;
    JMenu reportMenu;
    JMenuItem htmlReportTable, pdfReportTable,newMenuIt,extMenuIt;

    //обработка событий
    iHandler ihandler = new iHandler();
    Mouse Ms = new Mouse();
    List Ls = new List();



    //панель для отчета
    JCheckBox checkName;
    JCheckBox checkCar;
    JCheckBox checkpass;
    JCheckBox checkDriverLic;
    JCheckBox checkNum;
    JCheckBox checkdate;



    public GUI()
    {
        super("База данных ГИБДД");
        logGUI.info("Start app");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        //setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //подготовка меню
        menuBar = new JMenuBar();
        reportMenu = new JMenu("Отчеты");
        pdfReportTable = new JMenuItem("Сохранить таблицу в pdf");
        htmlReportTable = new JMenuItem("Сохранить таблицу в html");
        newMenuIt = new JMenuItem("Новое");
        extMenuIt = new JMenuItem("Выйти");


        //создание иконок

        save = new JButton(new ImageIcon("./ico/save.png"));
        open = new JButton(new ImageIcon("./ico/open.png"));
        add = new JButton(new ImageIcon("./ico/add.png"));
        edit = new JButton(new ImageIcon("./ico/edit.png"));
        del = new JButton(new ImageIcon("./ico/dell.png"));

        //настройка подсказок для кнопок
        save.setToolTipText("Сохранить базу");
        open.setToolTipText("Открыть базу");
        add.setToolTipText("Добавить карточку");
        edit.setToolTipText("Изменить карточку");
        del.setToolTipText("Удалить карточку");

        // Изменяем размер кнопок
        save.setPreferredSize(new Dimension(32,32));
        open.setPreferredSize(new Dimension(32,32));
        add.setPreferredSize(new Dimension(32,32));
        edit.setPreferredSize(new Dimension(32,32));
        del.setPreferredSize(new Dimension(32,32));

        // Создаём меню
        reportMenu.add(newMenuIt);
        reportMenu.add(pdfReportTable);
        reportMenu.add(htmlReportTable);
        reportMenu.addSeparator();
        reportMenu.add(extMenuIt);

        //Загружаем меню
        menuBar.add(reportMenu);
        menuBar.add(Box.createHorizontalGlue());
        setJMenuBar(menuBar);

        //добавление кнопок на панель инструментов
        boxButon = Box.createHorizontalBox();
        boxButon.add(Box.createHorizontalStrut(3));
        boxButon.add(save);
        boxButon.add(Box.createHorizontalStrut(3));
        boxButon.add(open);
        boxButon.add(Box.createHorizontalStrut(3));
        boxButon.add(add);
        boxButon.add(Box.createHorizontalStrut(3));
        boxButon.add(edit);
        boxButon.add(Box.createHorizontalStrut(3));
        boxButon.add(del);

        //создание таблици с данными
        headers = new Object[]{"ФИО","Паспор","Водительске права","Марка машины","Гос Номер", "Дата последнего тех осмотра"};
        data = new Object[0][0];
        model = new DefaultTableModel(data,headers);
        tableBase = new JTable(model);
        scrolltable = new JScrollPane(tableBase);

        //текстовое поле
        textArea = new JTextArea(20,50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrolltext = new JScrollPane(textArea);

        //Подготовка компонентов поиска
        domain = new JComboBox(new String[]{"...","ФИО","Паспорт","Водительские права","Марка машины","Гос номер", "Дата последенго тех осмотра"});
        field = new JTextField();
        fieldButton = new JButton("Поиск");
        searchLabel = new JLabel("Поиск по:");

        //Подготовка компонентов сортировки
        sorting = new JComboBox(new String[]{"...","ФИО","Марка машины","Гос номер", "Дата последенго тех осмотра"});
        up = new JButton(new ImageIcon("./ico/up.png"));
        down = new JButton(new ImageIcon("./ico/down.png"));
        up.setToolTipText("сортировать в поряде возрастания");
        down.setToolTipText("сортировать в порядке убывания");
        up.setPreferredSize(new Dimension(32,32));
        down.setPreferredSize(new Dimension(32,32));
        sortingLabel = new JLabel("Сортировка по:");

        //подготовка панели отчета
        checkdate = new JCheckBox("Дата тех осмотра");
        checkCar = new JCheckBox("Марка Машины");
        checkpass = new JCheckBox("Паспорт");
        checkDriverLic= new JCheckBox("Водительские права");
        checkName = new JCheckBox("Фио");
        checkNum = new JCheckBox("Гос. номер");


        //добавление компонетов на панель отчета
        boxReport= Box.createHorizontalBox();
        boxReport.add(Box.createHorizontalStrut(30));
        boxReport.add(new JLabel("Параметры формирования отчета:"));
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(checkName);
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(checkpass);
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(checkDriverLic);
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(checkCar);
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(checkNum);
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(checkdate);
        boxReport.add(new JSeparator(SwingConstants.VERTICAL));
        boxReport.add( new JLabel("От:"));
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(datePicker1);
        boxReport.add(new JSeparator(SwingConstants.VERTICAL));
        boxReport.add( new JLabel("До:"));
        boxReport.add(Box.createHorizontalStrut(8));
        boxReport.add(datePicker2);
        boxReport.add(new JSeparator(SwingConstants.VERTICAL));

        //добавление компонентов на панель поиска
        boxFilter = Box.createHorizontalBox();
        boxFilter.add(searchLabel);
        boxFilter.add(Box.createHorizontalStrut(8));
        boxFilter.add(domain);
        boxFilter.add(Box.createHorizontalStrut(6));
        boxFilter.add(field);
        boxFilter.add(Box.createHorizontalStrut(6));
        boxFilter.add(fieldButton);


        //добавление компонентов на панель сортировки
        boxSorting = Box.createHorizontalBox();
        boxSorting.add(sortingLabel);
        boxSorting.add(Box.createHorizontalStrut(6));
        boxSorting.add(sorting);
        boxSorting.add(Box.createHorizontalStrut(3));
        boxSorting.add(up);
        boxSorting.add(Box.createHorizontalStrut(3));
        boxSorting.add(down);
        boxSorting.add(Box.createHorizontalStrut(3));


        //размещение объектов на панели инструменов
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(boxButon,BorderLayout.WEST);
        toolBar.add(Box.createHorizontalStrut(12));
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(Box.createHorizontalStrut(12));
        toolBar.add(boxFilter);
        toolBar.add(Box.createHorizontalStrut(12));
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(Box.createHorizontalStrut(12));
        toolBar.add(boxSorting,BorderLayout.WEST);

        bar= Box.createVerticalBox();
        bar.add(toolBar);
        bar.add(boxReport);

        //размещение панели инструментов
        getContentPane().add(bar,BorderLayout.NORTH);
        //размещение таблици с данными
        getContentPane().add(scrolltable,BorderLayout.CENTER);

        //размещение текстового поля
        getContentPane().add(scrolltext,BorderLayout.EAST);

        //getContentPane().add(boxReport,BorderLayout.SOUTH);

        toolBar.setFloatable(false);

        //прослушивание кнопок
        up.addActionListener(ihandler);
        down.addActionListener(ihandler);
        fieldButton.addActionListener(ihandler);
        domain.addActionListener(ihandler);
        sorting.addActionListener(ihandler);
        extMenuIt.addActionListener(ihandler);
        save.addActionListener(ihandler);
        open.addActionListener(ihandler);
        add.addActionListener(ihandler);
        edit.addActionListener(ihandler);
        pdfReportTable.addActionListener(ihandler);
        htmlReportTable.addActionListener(ihandler);
        field.addMouseListener(Ms);

        ListSelectionModel selModel = tableBase.getSelectionModel();
        selModel.addListSelectionListener(Ls);
        logGUI.info("Finish app");
    }


   /* private void CheckName(JTextField bName) throws MyException, NullPointerException
    {
        String Sname = bName.getText();
        if(Sname.contains("Введите данные"))
            throw new MyException();
        if (Sname.length()==0)
            throw new NullPointerException();
    }*/


    private void Checkfilter(JTextField field, JComboBox domain) throws FilterException, DomainException
    {
        if(domain.getSelectedIndex() == 0)throw new DomainException();
        if (field.getText().length() ==0 ) throw new FilterException();
        if(field.getText().equals("Введите данные")) throw new FilterException();
    }

    private void CheckDomain(JComboBox domain) throws DomainException
    {
        if(domain.getSelectedIndex() == 0)throw new DomainException();
    }

    public class Mouse implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(field.getText().contains("Введите данные"))
            {
                field.setText("");
                logGUI.warn("search_field != Введите данные");
                logGUI.debug("Перезагрузите компьютер");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    public class iHandler implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == htmlReportTable)
            {
                try
                {
                    htmlSave htmlSave = new htmlSave("Сохранение таблици В HTML",model);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if(e.getSource() == pdfReportTable)
            {
                try {
                    pdfSave pdfSave = new pdfSave("Сохранение таблици В PDF",model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if(e.getSource() == add)
            {
                model.addRow(new String[]{});
            }

            if(e.getSource() == save)
            {

                try {
                    XmlSave xmlSave =new XmlSave("Сохранение данных", model);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if(e.getSource() == open)
            {

                try {
                    XmlOpen xmlOpen = new XmlOpen("Загрузка данных",model,tableBase);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            if(e.getSource()==edit)
            {

                Tthread tthread1 = new Tthread(1,model,tableBase);
                Tthread tthread2 = new Tthread(2,model);
                Tthread tthread3 = new Tthread(3,model);
                Tthread tthread4 = new Tthread(4,model);
                tthread1.run();
                tthread2.run();
                tthread3.run();
                tthread4.run();

            }
            if(e.getSource() == domain)
            {
                try {
                    CheckDomain(domain);
                    JOptionPane.showMessageDialog(getContentPane(), domain.getSelectedItem());
                    field.setText("Введите данные");
                }
                catch(NullPointerException ex){
                    JOptionPane.showMessageDialog(getContentPane(),ex.toString());

                }
                catch (DomainException domainException)
                {
                    JOptionPane.showMessageDialog(null,domainException.getMessage());
                }

            }

            if(e.getSource() == sorting)
            {
                try
                {
                    CheckDomain(sorting);
                    JOptionPane.showMessageDialog(getContentPane(), sorting.getSelectedItem());
                }
                catch(NullPointerException ex)
                {
                    JOptionPane.showMessageDialog(getContentPane(),ex.toString());

                }
                catch (DomainException domainException)
                {
                    JOptionPane.showMessageDialog(null,domainException.getMessage());
                }
            }
            if(e.getSource() == fieldButton)
            {
                try{
                    Checkfilter(field,domain);
                    JOptionPane.showMessageDialog (getContentPane(), "поиск:"+field.getText());
                }

                catch(NullPointerException ex){
                    JOptionPane.showMessageDialog(getContentPane(),ex.toString());
                    }
                catch (FilterException | DomainException filterException)
                {
                    JOptionPane.showMessageDialog(null,filterException.getMessage());
                }

                //JOptionPane.showMessageDialog (getContentPane(), "поиск:"+field.getText());



            }
            if(e.getSource() == up)
            {
                try
                {
                    CheckDomain(sorting);
                    JOptionPane.showMessageDialog (getContentPane(), "up");
                }
                catch(NullPointerException ex)
                {
                    JOptionPane.showMessageDialog(getContentPane(),ex.toString());

                }
                catch (DomainException sortex)
                {
                    JOptionPane.showMessageDialog(null,sortex.getMessage());
                }
            }
            if(e.getSource() == down)
            {
                try
                {
                    CheckDomain(sorting);
                    JOptionPane.showMessageDialog (getContentPane(), "down");
                }
                catch(NullPointerException ex)
                {
                    JOptionPane.showMessageDialog(getContentPane(),ex.toString());

                }
                catch (DomainException sortex)
                {
                    JOptionPane.showMessageDialog(null,sortex.getMessage());
                }
            }
        }
    }

    public class List implements ListSelectionListener, ActionListener
    {
        public void valueChanged(ListSelectionEvent e) {


            ListSelectionModel lsm = (ListSelectionModel)e.getSource();

            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();

            textArea.setText("");
            if (lsm.isSelectionEmpty()) {
                textArea.append(" <none>");
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        textArea.append(" " + i);
                    }
                }
            }

        }


        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}


/*
import java.util.List;
import java.util.stream.Collectors;

List<Sample> list = new ArrayList<Sample>();
List<Sample> result = list.stream()
    .filter(a -> Objects.equals(a.value3, "three"))
    .collect(Collectors.toList());
 */

