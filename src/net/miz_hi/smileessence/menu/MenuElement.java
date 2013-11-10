package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.command.ICommand;

import java.util.ArrayList;
import java.util.List;

public class MenuElement
{

    private String name;
    private List<MenuElement> children = new ArrayList<MenuElement>();
    private ICommand command;

    public MenuElement(String name)
    {
        this.name = name;
    }

    public MenuElement(ICommand command)
    {
        this.name = command.getName();
        this.command = command;
    }

    public String getName()
    {
        return name;
    }

    public boolean isParent()
    {
        return children.size() > 0;
    }

    public List<MenuElement> getChildren()
    {
        return children;
    }

    public void addChild(MenuElement element)
    {
        children.add(element);
    }

    public ICommand getCommand()
    {
        return command;
    }


}
