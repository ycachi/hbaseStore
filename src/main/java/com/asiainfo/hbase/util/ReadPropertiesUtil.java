// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 
// Source File Name:   ReadPropertiesUtil.java

package com.asiainfo.hbase.util;

import java.io.*;
import java.util.Properties;

public class ReadPropertiesUtil extends Properties
{

    private ReadPropertiesUtil(String path)
    {
        InputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream(path));
            load(in);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static synchronized void makeInstance(String path)
    {
        if(instance == null)
            instance = new ReadPropertiesUtil(path);
    }

    public static ReadPropertiesUtil getInstance(String path)
    {
        if(instance != null)
        {
            return instance;
        } else
        {
            makeInstance(path);
            return instance;
        }
    }

    private static final long serialVersionUID = 0x1cad1ef6e51a85f2L;
    private static ReadPropertiesUtil instance;
}
