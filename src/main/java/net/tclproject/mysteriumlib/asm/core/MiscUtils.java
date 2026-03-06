package net.tclproject.mysteriumlib.asm.core;

import org.apache.commons.io.*;
import java.io.*;
import org.objectweb.asm.*;
import java.util.*;
import java.util.logging.*;

public class MiscUtils
{
    public static void generateMethodsDictionary() throws Exception {
        final List<String> lines = (List<String>)FileUtils.readLines(new File("methods.csv"));
        lines.remove(0);
        final HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (final String str : lines) {
            final String[] splitted = str.split(",");
            final int first = splitted[0].indexOf(95);
            final int second = splitted[0].indexOf(95, first + 1);
            final int id = Integer.valueOf(splitted[0].substring(first + 1, second));
            map.put(id, splitted[1]);
        }
        final DataOutputStream out = new DataOutputStream(new FileOutputStream("methods.bin"));
        out.writeInt(map.size());
        for (final Map.Entry<Integer, String> entry : map.entrySet()) {
            out.writeInt(entry.getKey());
            out.writeUTF(entry.getValue());
        }
        out.close();
    }

    public class SafeCommonSuperClassWriter extends ClassWriter
    {
        private final MetaReader metaReader;

        public SafeCommonSuperClassWriter(final MetaReader metaReader, final int flags) {
            super(flags);
            this.metaReader = metaReader;
        }

        protected String getCommonSuperClass(final String type1, final String type2) {
            ArrayList<String> superClasses1;
            ArrayList<String> superClasses2;
            int size;
            int i;
            for (superClasses1 = this.metaReader.getSuperClasses(type1), superClasses2 = this.metaReader.getSuperClasses(type2), size = Math.min(superClasses1.size(), superClasses2.size()), i = 0; i < size && superClasses1.get(i).equals(superClasses2.get(i)); ++i) {}
            if (i == 0) {
                return "java/lang/Object";
            }
            return superClasses1.get(i - 1);
        }
    }

    public class SystemLogHelper implements LogHelper
    {
        @Override
        public void debug(final String message) {
            System.out.println("[DEBUG] " + message);
        }

        @Override
        public void warning(final String message) {
            System.out.println("[WARNING] " + message);
        }

        @Override
        public void severe(final String message) {
            System.out.println("[SEVERE] " + message);
        }

        @Override
        public void severe(final String message, final Throwable cause) {
            this.severe(message);
            cause.printStackTrace();
        }

        @Override
        public void info(final String message) {
            System.out.println("[INFORMATION] " + message);
        }

        @Override
        public void fatal(final String message) {
            System.out.println("[---------------[!!!FATAL!!!]---------------]");
            System.out.println(message);
            System.out.println("[---------------[!!!FATAL!!!]---------------]");
        }

        @Override
        public void fatal(final String message, final Throwable cause) {
            this.fatal(message);
            cause.printStackTrace();
        }
    }

    public class MinecraftLogHelper implements LogHelper
    {
        private Logger logger;

        public MinecraftLogHelper(final Logger logger) {
            this.logger = logger;
        }

        @Override
        public void debug(final String message) {
            this.logger.log(Level.FINE, message);
        }

        public void detailed(final String message) {
            this.logger.log(Level.FINEST, message);
        }

        public void configInfo(final String message) {
            this.logger.log(Level.CONFIG, message);
        }

        @Override
        public void warning(final String message) {
            this.logger.log(Level.WARNING, message);
        }

        @Override
        public void severe(final String message) {
            this.logger.log(Level.SEVERE, message);
        }

        @Override
        public void severe(final String message, final Throwable cause) {
            this.logger.log(Level.SEVERE, message, cause);
        }

        @Override
        public void info(final String message) {
            this.logger.log(Level.INFO, message);
        }

        @Override
        public void fatal(final String message) {
            this.logger.log(Level.SEVERE, "[---------------[!!!FATAL!!!]---------------]");
            this.logger.log(Level.SEVERE, message);
            this.logger.log(Level.SEVERE, "[---------------[!!!FATAL!!!]---------------]");
        }

        @Override
        public void fatal(final String message, final Throwable cause) {
            this.logger.log(Level.SEVERE, "[---------------[!!!FATAL!!!]---------------]");
            this.logger.log(Level.SEVERE, message, cause);
            this.logger.log(Level.SEVERE, "[---------------[!!!FATAL!!!]---------------]");
        }
    }

    public interface LogHelper
    {
        void debug(final String p0);

        void info(final String p0);

        void warning(final String p0);

        void severe(final String p0);

        void severe(final String p0, final Throwable p1);

        void fatal(final String p0);

        void fatal(final String p0, final Throwable p1);
    }
}
