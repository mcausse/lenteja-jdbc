package org.homs.cucaracha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * Cucaracha - micro Key-Value Database in 200 rows of code
 *
 * @author mhoms 2018, 2019, 2022
 */
public class CucarachaMicroDb {

//    public static void main(String[] args) {
//
//        CucarachaMicroDb db = new CucarachaMicroDb(new File("cucaracha-test.properties"));
//
//        db.beginTransaction();
//        db.put("database", toList("cmp", "fpe"));
//        db.commit();
//    }
//
//    static String toList(String... s) {
//        StringBuilder r = new StringBuilder();
//        for (int i = 0; i < s.length; i++) {
//            if (i > 0) {
//                r.append(",");
//            }
//            r.append(s[i]);
//        }
//        return r.toString();
//    }

    //////////////////////////////////////////////////////////////

    final File file;
    Properties p;

    final ReadWriteLock lock = new ReentrantReadWriteLock();

    public CucarachaMicroDb(File file) {
        super();
        this.file = file;
        this.p = null;
        createFileIfNotExists();
    }

    protected void createFileIfNotExists() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* ===== LOCK & TRANSACTION ===== */

    public void beginTransaction() {
        if (p != null) {
            throw new RuntimeException("yet in active transaction");
        }
        lock.writeLock().lock();
        loadProperties();
    }

    public void commit() {
        if (p == null) {
            throw new RuntimeException("not in active transaction");
        }
        saveProperties();
        p = null;
        lock.writeLock().unlock();
    }

    public void rollback() {
        if (p == null) {
            throw new RuntimeException("not in active transaction");
        }
        p = null;
        lock.writeLock().unlock();
    }

    /* ===== C R U D ===== */

    public boolean exist(String key) {
        if (p == null) {
            throw new RuntimeException("not in active transaction");
        }
        return p.containsKey(key);
    }

    public String get(String key) {
        if (!p.containsKey(key)) {
            throw new IllegalArgumentException("not found: " + key);
        }
        return p.getProperty(key);
    }

    public Map<String, String> find(Predicate<String> keyPredicate, Predicate<String> valuePredicate) {
        Map<String, String> r = new LinkedHashMap<>();
        for (Object okey : p.keySet()) {
            String key = (String) okey;
            if (keyPredicate == null || keyPredicate.test(key)) {
                String value = p.getProperty(key);
                if (valuePredicate == null || valuePredicate.test(value)) {
                    r.put(key, value);
                }
            }
        }
        return r;
    }

    public Map<Long, String> find(String keyPrefix, Predicate<String> valuePredicate) {
        Map<Long, String> r = new LinkedHashMap<>();
        for (Object okey : p.keySet()) {
            String key = (String) okey;
            if (keyPrefix == null || key.startsWith(keyPrefix)) {
                String value = p.getProperty(key);
                if (valuePredicate == null || valuePredicate.test(value)) {
                    long id = Long.parseLong(key.substring(keyPrefix.length()));
                    r.put(id, value);
                }
            }
        }
        return r;
    }

    public void remove(String key) {
        if (!p.containsKey(key)) {
            throw new IllegalArgumentException("not found: " + key);
        }
        p.remove(key);
    }

    public void put(String key, String value) {
        String v = Objects.requireNonNullElse(value, "");
        p.setProperty(key, v);
    }

    public void putAll(Properties p) {
        this.p.putAll(p);
    }

    public long getSequenceNextValue(String seqName) {
        String seqPropName = "seq." + seqName;
        long id;
        if (p.containsKey(seqPropName)) {
            id = Long.parseLong(p.getProperty(seqPropName));
            id++;
            p.setProperty(seqPropName, String.valueOf(id));
        } else {
            id = 0;
            p.setProperty(seqPropName, String.valueOf(id));
        }
        return id;
    }

    public long getSequenceCurrValue(String seqName) {
        String seqPropName = "seq." + seqName;
        long id;
        if (p.containsKey(seqPropName)) {
            id = Long.parseLong(p.getProperty(seqPropName));
        } else {
            id = -1;
        }
        return id;
    }

    public void removeSequence(String seqName) {
        String seqPropName = "seq." + seqName;
        p.remove(seqPropName);
    }
    /* ===== PERSISTENCE ===== */

    public Properties getCurrentProperties() {
        return p;
    }

    public void removeDatabase() {
        if (p != null) {
            throw new RuntimeException("cannot remove in active transaction");
        }
        this.file.delete();
        //createFileIfNotExists();
    }

    protected void saveProperties() {
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(file);
            p.store(fo, "");
            fo.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void loadProperties() {
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            p = new Properties();
            p.load(fi);
            fi.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}