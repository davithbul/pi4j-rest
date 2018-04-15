package org.pi4jrest.common;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class SerializationUtils {

    private final static String BACKUP_DIRECTORY;

    static {
        if (new File(System.getProperty("user.dir") + File.separator + "backup").isDirectory()) {
            BACKUP_DIRECTORY = System.getProperty("user.dir") + File.separator + "backup" + File.separator;
        } else {
            BACKUP_DIRECTORY = System.getProperty("user.dir") + File.separator;
        }
    }

    public static void saveCollection(Collection<?> collection, String name) {
        saveObject(new ArrayList<>(collection), name);
    }

    public static void saveObject(Object object, String name) {
        try {
            FileOutputStream fileOut = new FileOutputStream(BACKUP_DIRECTORY + name + ".save");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> readCollection(Class<T> type, String name) {
        return (Collection<T>) readObject(Collection.class, name);
    }

    public static <T> T safeReadObject(Class<T> type, String name, T defaultValue) {
        try {
            return readObject(type, name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> T readObject(Class<T> type, String name) {
        Object object;
        try {
            FileInputStream fileIn = new FileInputStream(BACKUP_DIRECTORY + name + ".save");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            object = in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            if (type.isAssignableFrom(Collection.class)) {
                return (T) new ArrayList<>();
            } else {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return (T) object;
    }
}
