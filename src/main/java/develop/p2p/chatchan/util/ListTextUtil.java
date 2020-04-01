package develop.p2p.chatchan.util;

import java.io.*;
import java.util.ArrayList;

public class ListTextUtil
{
    private String fileName;
    private File file;
    private ArrayList<String> list = new ArrayList<>();
    public ListTextUtil(String fileName)
    {
        this.fileName = fileName;
        file = new File(fileName);
    }

    public boolean exists()
    {
        return this.file.exists();
    }

    public String getFileName()
    {
        return fileName;
    }

    public void add(String value)
    {
        list.add(value);
    }

    public ArrayList<String> getList()
    {
        if (!file.exists())
            return list;
        ArrayList<String> lists = getListFromFile();
        list.addAll(lists);
        return list;
    }

    public boolean save() throws IOException
    {
        if (!file.exists())
            file.createNewFile();
        try (FileWriter writer = new FileWriter(file))
        {
            for (String string : list)
                writer.write(string + "\r\n");
            return true;
        }
        catch (Exception e)
        {
            ErrorStop.stop("[SYSTEM] Failed to Save List files.");
            return false;
        }
    }

    public void add(ArrayList<String> value)
    {
        list.addAll(value);
    }


    public int size()
    {
        return this.list.size();
    }

    public boolean saveDefaultList() throws IOException
    {
        if (!this.exists())
            return true;
        return this.save();
    }

    public ArrayList<String> getListFromFile()
    {
        try (FileReader reader = new FileReader(file); BufferedReader read = new BufferedReader(reader))
        {
            String tmp;
            ArrayList<String> lists = new ArrayList<>();
            while ((tmp = read.readLine()) != null)
                lists.add(tmp);
            return lists;
        }
        catch(Exception e)
        {
            return new ArrayList<>();
        }
    }

}
