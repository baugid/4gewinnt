package control;

import utils.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecureClassLoader extends URLClassLoader {
    private Map<Integer, String> strings = new HashMap<>();
    private Set<Integer> classes = new HashSet<>();
    private int indexCorrection = 0; // for correcting indexes to constant pools with long and double entries

    public SecureClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = super.loadClass(name);
        if (User.class.isAssignableFrom(loadedClass)) {
            classes = new HashSet<>();
            strings = new HashMap<>();
            indexCorrection = 0;
            try {
                getReferencedClassesFromClass(loadedClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Integer index : classes) {
                if (strings.get(index).matches("\\[*java/lang/reflect/.+"))
                    return null;
            }
        }
        return loadedClass;
    }

    private void saveStringFromUtf8Entry(int index, DataInputStream stream) throws IOException {
        strings.put(index + indexCorrection, readString(stream));
    }

    private void getReferencedClassesFromClass(Class<?> c) throws IOException {
        saveReferencedClassesFromStream(c.getResourceAsStream(c.getSimpleName() + ".class"));
    }

    private void saveReferencedClassesFromStream(InputStream stream) throws IOException {
        DataInputStream dataStream = new DataInputStream(stream);
        skipHeader(dataStream);
        saveReferencedClassesFromConstantPool(dataStream);
    }

    private void skipHeader(DataInputStream dataInputStream) throws IOException {
        readU4(dataInputStream); // magic byte
        readU2(dataInputStream); // minor version
        readU2(dataInputStream); // major version
    }

    private void saveReferencedClassesFromConstantPool(DataInputStream stream) throws IOException {
        int poolSize = readU2(stream);
        for (int n = 1; n < poolSize - indexCorrection; n++)
            savePoolEntryIfIsClassReference(n, stream);
    }

    private void savePoolEntryIfIsClassReference(int index, DataInputStream stream) throws IOException {
        int tag = readU1(stream);
        switch (tag) {
            case 1: // Utf8
                saveStringFromUtf8Entry(index, stream);
                break;
            case 7: // Class
                saveClassEntry(stream);
                break;
            case 8: // String
            case 16: // MethodType
                readU2(stream);
                break;
            case 3: // Integer
            case 4: // Float
                readU4(stream);
                break;
            case 5: // Long
            case 6: // Double
                readU4(stream);
                readU4(stream);
                indexCorrection++;
                break;
            case 9: // Fieldref
            case 10: // Methodref
            case 11: // InterfaceMethodref
            case 12: // NameAndType
            case 18: // InvokeDynmaic
                readU2(stream);
                readU2(stream);
                break;
            case 15: // MethodHandle
                readU1(stream);
                readU2(stream);
                break;
        }
    }

    private void saveClassEntry(DataInputStream stream) throws IOException {
        classes.add(readU2(stream));
    }

    private String readString(DataInputStream stream) throws IOException {
        return stream.readUTF();
    }

    private int readU1(DataInputStream stream) throws IOException {
        return stream.readUnsignedByte();
    }

    private int readU2(DataInputStream stream) throws IOException {
        return stream.readUnsignedShort();
    }

    private int readU4(DataInputStream stream) throws IOException {
        return stream.readInt();
    }
}
