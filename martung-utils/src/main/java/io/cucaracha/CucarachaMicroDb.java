package io.cucaracha;

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
import java.util.function.Supplier;

/**
 * Cucaracha - micro Key-Value store in 200 rows of code
 *
 * @author mhoms 2018, 2019, 2022, 2024
 */
public class CucarachaMicroDb {

    public static final String SEQUENCE_PREFIX = "__seq.";

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
                throw new RuntimeException("error creating database file: " + file, e);
            }
        }
    }

    /* ===== LOCK & TRANSACTION ===== */

    public void beginTransaction() {
        if (p != null) {
            throw new RuntimeException("yet in active transaction: " + file);
        }
        lock.writeLock().lock();
        loadProperties();
    }

    public void commit() {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        saveProperties();
        p = null;
        lock.writeLock().unlock();
    }

    public void rollback() {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        p = null;
        lock.writeLock().unlock();
    }

    public void executeInTransaction(Runnable r) {
        beginTransaction();
        try {
            r.run();
            commit();
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    public void executeInTransactionAsReadOnly(Runnable r) {
        beginTransaction();
        try {
            r.run();
        } finally {
            rollback();
        }
    }

    public <T> T executeInTransactionWithReturn(Supplier<T> s) {
        beginTransaction();
        try {
            var r = s.get();
            commit();
            return r;
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    public <T> T executeInTransactionAsReadOnlyWithReturn(Supplier<T> s) {
        beginTransaction();
        try {
            return s.get();
        } finally {
            rollback();
        }
    }

    /* ===== C R U D ===== */

    public boolean exist(String key) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        return p.containsKey(key);
    }

    public String get(String key) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        if (!p.containsKey(key)) {
            throw new IllegalArgumentException("not found: " + key + "; " + file);
        }
        return p.getProperty(key);
    }

    public Map<String, String> find(Predicate<String> keyPredicate, Predicate<String> valuePredicate) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
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

    public void remove(String key) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        if (!p.containsKey(key)) {
            throw new IllegalArgumentException("not found: " + key + "; " + file);
        }
        p.remove(key);
    }

    public void put(String key, String value) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        String v = Objects.requireNonNullElse(value, "");
        p.setProperty(key, v);
    }

    public void putAll(Properties p) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        this.p.putAll(p);
    }

    public long getSequenceNextValue(String seqName) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        String seqPropName = SEQUENCE_PREFIX + seqName;
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
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        String seqPropName = SEQUENCE_PREFIX + seqName;
        long id;
        if (p.containsKey(seqPropName)) {
            id = Long.parseLong(p.getProperty(seqPropName));
        } else {
            id = -1;
        }
        return id;
    }

    public void removeSequence(String seqName) {
        if (p == null) {
            throw new RuntimeException("not in active transaction: " + file);
        }
        String seqPropName = SEQUENCE_PREFIX + seqName;
        p.remove(seqPropName);
    }

    /* ===== PERSISTENCE ===== */

    public Properties getCurrentProperties() {
        return p;
    }

    public void removeDatabase() {
        if (p != null) {
            throw new RuntimeException("cannot remove database in active transaction: " + file);
        }
        this.file.delete();
    }

    public void recreateDatabase() {
        removeDatabase();
        createFileIfNotExists();
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