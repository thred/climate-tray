package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.swing.AdvancedListModel;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class AbstractClimateTrayListController<TYPE extends Copyable<TYPE>> extends
    AbstractClimateTrayController<List<TYPE>, JPanel> implements ListSelectionListener
{

    private final MouseListener mouseListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2)
            {
                edit();
            }
        }
    };

    private final AdvancedListModel<TYPE> listModel = new AdvancedListModel<>();
    private final JButton addButton = SwingUtils.createButton("Add...", (e) -> add());
    private final JButton editButton = SwingUtils.createButton("Edit...", (e) -> edit());
    private final JButton removeButton = SwingUtils.createButton("Remove", (e) -> remove());
    private final JButton upButton = SwingUtils.createButton("Up", (e) -> up());
    private final JButton downButton = SwingUtils.createButton("Down", (e) -> down());

    private JList<TYPE> list;

    public AbstractClimateTrayListController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        setList(createList(listModel));

        JPanel panel = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(2, 6).defaultOutsets(0, 0, 0, 0);

        panel.add(new JScrollPane(list), gbc.span(1, 6).weight(1).fill());

        panel.add(addButton, gbc.next().hFill());
        panel.add(editButton, gbc.next().hFill());
        panel.add(removeButton, gbc.next().hFill());
        panel.add(new JLabel(), gbc.next().weight(0, 1));
        panel.add(upButton, gbc.next().hFill());
        panel.add(downButton, gbc.next().hFill());

        return panel;
    }

    protected JList<TYPE> createList(AdvancedListModel<TYPE> listModel)
    {
        JList<TYPE> list = new JList<>(listModel);

        list.setPreferredSize(new Dimension(256, 64));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return list;
    }

    public JList<TYPE> getList()
    {
        if (list == null)
        {
            setList(createList(listModel));
        }

        return list;
    }

    protected void setList(JList<TYPE> list)
    {
        if (this.list == list)
        {
            return;
        }

        if (this.list != null)
        {
            unmonitor(this.list);

            list.removeListSelectionListener(this);
            list.removeMouseListener(mouseListener);
        }

        this.list = list;

        if (list != null)
        {
            monitor(list);

            list.addListSelectionListener(this);
            list.addMouseListener(mouseListener);
        }
    }

    @Override
    public void refreshView()
    {
        listModel.updateAllElements();

        updateState();
    }

    protected void updateState()
    {
        int selectedIndex = (list != null) ? list.getSelectedIndex() : -1;
        boolean anySelected = selectedIndex >= 0;

        addButton.setEnabled(true);
        editButton.setEnabled(anySelected);
        removeButton.setEnabled(anySelected);
        upButton.setEnabled(anySelected);
        downButton.setEnabled(anySelected);
    }

    @Override
    protected void localPrepare(List<TYPE> model)
    {
        listModel.setList(Copyable.deepCopy(model));
    }

    @Override
    protected void localApply(List<TYPE> model)
    {
        model.clear();

        Copyable.deepCopy(listModel.getList(), model);
    }

    @Override
    protected void localCheck(MessageList messages)
    {
        updateState();
    }

    public void add()
    {
        TYPE element = createInstance();

        if (edit(element))
        {
            listModel.addElement(element);
        }
    }

    protected abstract TYPE createInstance();

    public void edit()
    {
        TYPE element = list.getSelectedValue();

        if (element == null)
        {
            return;
        }

        if (edit(element))
        {
            listModel.updateElement(element);
        }
    }

    protected abstract boolean edit(TYPE element);

    public void remove()
    {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex < 0)
        {
            return;
        }

        listModel.removeElementAt(selectedIndex);

        if (selectedIndex >= listModel.getSize())
        {
            selectedIndex = listModel.getSize() - 1;
        }

        if (selectedIndex >= 0)
        {
            list.setSelectedIndex(selectedIndex);
        }
    }

    public void up()
    {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex < 1)
        {
            return;
        }

        listModel.moveElementAt(selectedIndex, selectedIndex - 1);
        list.setSelectedIndex(selectedIndex - 1);
    }

    public void down()
    {
        int selectedIndex = list.getSelectedIndex();

        if ((selectedIndex < 0) || (selectedIndex >= (listModel.getSize() - 1)))
        {
            return;
        }

        listModel.moveElementAt(selectedIndex, selectedIndex + 1);
        list.setSelectedIndex(selectedIndex + 1);
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
        {
            return;
        }

        updateState();
    }

}
