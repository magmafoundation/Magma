package org.magmafoundation.magma.utils;

        import org.apache.logging.log4j.Level;
        import org.apache.logging.log4j.Logger;
        import org.apache.logging.log4j.Marker;
        import org.apache.logging.log4j.message.EntryMessage;
        import org.apache.logging.log4j.message.Message;
        import org.apache.logging.log4j.message.MessageFactory;
        import org.apache.logging.log4j.util.MessageSupplier;
        import org.apache.logging.log4j.util.Supplier;
        import org.magmafoundation.magma.configuration.MagmaConfig;

public class MagmaFMLLogInjector implements Logger {

        public static Logger log;

        private MagmaConfig internal = new MagmaConfig(true);

        public MagmaFMLLogInjector(Logger log) {
            MagmaFMLLogInjector.log = log;
        }

        @Override
        public void catching(Level level, Throwable t) {
            log.catching(level, t);
        }

        @Override
        public void catching(Throwable t) {
            log.catching(t);
        }

        @Override
        public void debug(Marker marker, Message msg) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, msg);
        }

        @Override
        public void debug(Marker marker, Message msg, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, msg, t);
        }

        @Override
        public void debug(Marker marker, MessageSupplier msgSupplier) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, msgSupplier);
        }

        @Override
        public void debug(Marker marker, MessageSupplier msgSupplier, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, msgSupplier, t);
        }

        @Override
        public void debug(Marker marker, CharSequence message) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message);
        }

        @Override
        public void debug(Marker marker, CharSequence message, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, t);
        }

        @Override
        public void debug(Marker marker, Object message) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message);
        }

        @Override
        public void debug(Marker marker, Object message, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, t);
        }

        @Override
        public void debug(Marker marker, String message) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message);
        }

        @Override
        public void debug(Marker marker, String message, Object... params) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, params);
        }

        @Override
        public void debug(Marker marker, String message, Supplier<?>... paramSuppliers) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, paramSuppliers);
        }

        @Override
        public void debug(Marker marker, String message, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, t);
        }

        @Override
        public void debug(Marker marker, Supplier<?> msgSupplier) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, msgSupplier);
        }

        @Override
        public void debug(Marker marker, Supplier<?> msgSupplier, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, msgSupplier, t);
        }

        @Override
        public void debug(Message msg) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msg);
        }

        @Override
        public void debug(Message msg, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msg, t);
        }

        @Override
        public void debug(MessageSupplier msgSupplier) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msgSupplier);
        }

        @Override
        public void debug(MessageSupplier msgSupplier, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msgSupplier, t);
        }

        @Override
        public void debug(CharSequence message) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message);
        }

        @Override
        public void debug(CharSequence message, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, t);
        }

        @Override
        public void debug(Object message) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message);
        }

        @Override
        public void debug(Object message, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, t);
        }

        @Override
        public void debug(String msg) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msg);
        }

        @Override
        public void debug(String msg, Object... params) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msg, params);
        }

        @Override
        public void debug(String message, Supplier<?>... paramSuppliers) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, paramSuppliers);
        }

        @Override
        public void debug(String message, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, t);
        }

        @Override
        public void debug(Supplier<?> msgSupplier) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msgSupplier);
        }

        @Override
        public void debug(Supplier<?> msgSupplier, Throwable t) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(msgSupplier, t);
        }

        @Override
        public void debug(Marker marker, String message, Object p0) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3, p4);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        @Override
        public void debug(String message, Object p0) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0);
        }

        @Override
        public void debug(String message, Object p0, Object p1) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3, p4);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3, p4, p6);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3, p4, p6, p7);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3, p4, p6, p7, p8);
        }

        @Override
        public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            if (!internal.debugPrintFML.getValues()) return;
            log.debug(message, p0, p1, p2, p3, p4, p6, p7, p8, p9);
        }


        /**
         *
         * End of debug Methods
         *
         */

        @Override
        public void entry() {
            log.entry();
        }

        @Override
        public void entry(Object... params) {
            log.entry(params);
        }

        @Override
        public void error(Marker marker, Message msg) {
            log.error(marker, msg);
        }

        @Override
        public void error(Marker marker, Message msg, Throwable t) {
            log.error(marker, msg, t);
        }

        @Override
        public void error(Marker marker, MessageSupplier msgSupplier) {
            log.error(marker, msgSupplier);
        }

        @Override
        public void error(Marker marker, MessageSupplier msgSupplier, Throwable t) {
            log.error(marker, msgSupplier, t);
        }

        @Override
        public void error(Marker marker, CharSequence message) {
            log.error(marker, message);
        }

        @Override
        public void error(Marker marker, CharSequence message, Throwable t) {
            log.error(marker, message, t);
        }

        @Override
        public void error(Marker marker, Object message) {
            log.error(marker, message);
        }

        @Override
        public void error(Marker marker, Object message, Throwable t) {
            log.error(marker, message, t);
        }

        @Override
        public void error(Marker marker, String message) {
            log.error(marker, message);
        }

        @Override
        public void error(Marker marker, String message, Object... params) {
            log.error(marker, message, params);
        }

        @Override
        public void error(Marker marker, String message, Supplier<?>... paramSuppliers) {
            log.error(marker, message, paramSuppliers);
        }

        @Override
        public void error(Marker marker, String message, Throwable t) {
            log.error(marker, message, t);
        }

        @Override
        public void error(Marker marker, Supplier<?> msgSupplier) {
            log.error(marker, msgSupplier);
        }

        @Override
        public void error(Marker marker, Supplier<?> msgSupplier, Throwable t) {
            log.error(marker, msgSupplier, t);
        }

        @Override
        public void error(Message msg) {
            log.error(msg);
        }

        @Override
        public void error(Message msg, Throwable t) {
            log.error(msg, t);
        }

        @Override
        public void error(MessageSupplier msgSupplier) {
            log.error(msgSupplier);
        }

        @Override
        public void error(MessageSupplier msgSupplier, Throwable t) {
            log.error(msgSupplier, t);
        }

        @Override
        public void error(CharSequence message) {
            log.error(message);
        }

        @Override
        public void error(CharSequence message, Throwable t) {
            log.error(message, t);
        }

        @Override
        public void error(Object message) {
            log.error(message);
        }

        @Override
        public void error(Object message, Throwable t) {
            log.error(message, t);
        }

        @Override
        public void error(String message) {
            log.error(message);
        }

        @Override
        public void error(String message, Object... params) {
            log.error(message, params);
        }

        @Override
        public void error(String message, Supplier<?>... paramSuppliers) {
            log.error(message, paramSuppliers);
        }

        @Override
        public void error(String message, Throwable t) {
            log.error(message, t);
        }

        @Override
        public void error(Supplier<?> msgSupplier) {
            log.error(msgSupplier);
        }

        @Override
        public void error(Supplier<?> msgSupplier, Throwable t) {
            log.error(msgSupplier, t);
        }

        @Override
        public void error(Marker marker, String message, Object p0) {
            log.error(marker, message, p0);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1) {
            log.error(marker, message, p0, p1);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
            log.error(marker, message, p0, p1, p2);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            log.error(marker, message, p0, p1, p2, p3);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.error(marker, message, p0, p1, p2, p3, p4);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.error(marker, message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.error(marker, message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        @Override
        public void error(String message, Object p0) {
            log.error(message, p0);
        }

        @Override
        public void error(String message, Object p0, Object p1) {
            log.error(message, p0, p1);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2) {
            log.error(message, p0, p1, p2);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3) {
            log.error(message, p0, p1, p2, p3);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.error(message, p0, p1, p2, p3, p4);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.error(message, p0, p1,p2,  p3, p4, p5);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.error(message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.error(message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }


        /**
         *
         * End of error Methods
         *
         */


        @Override
        public void exit() {
            log.exit();
        }

        @Override
        public <R> R exit(R result) {
            return log.exit(result);
        }

        @Override
        public void fatal(Marker marker, Message msg) {
            log.fatal(marker, msg);
        }

        @Override
        public void fatal(Marker marker, Message msg, Throwable t) {
            log.fatal(marker, msg, t);
        }

        @Override
        public void fatal(Marker marker, MessageSupplier msgSupplier) {
            log.fatal(marker, msgSupplier);
        }

        @Override
        public void fatal(Marker marker, MessageSupplier msgSupplier, Throwable t) {
            log.fatal(marker, msgSupplier, t);
        }

        @Override
        public void fatal(Marker marker, CharSequence message) {
            log.fatal(marker, message);
        }

        @Override
        public void fatal(Marker marker, CharSequence message, Throwable t) {
            log.fatal(marker, message, t);
        }

        @Override
        public void fatal(Marker marker, Object message) {
            log.fatal(marker, message);
        }

        @Override
        public void fatal(Marker marker, Object message, Throwable t) {
            log.fatal(marker, message, t);
        }

        @Override
        public void fatal(Marker marker, String message) {
            log.fatal(marker, message);
        }

        @Override
        public void fatal(Marker marker, String message, Object... params) {
            log.fatal(marker, message, params);
        }

        @Override
        public void fatal(Marker marker, String message, Supplier<?>... paramSuppliers) {
            log.fatal(marker, message, paramSuppliers);
        }

        @Override
        public void fatal(Marker marker, String message, Throwable t) {
            log.fatal(marker, message, t);
        }

        @Override
        public void fatal(Marker marker, Supplier<?> msgSupplier) {
            log.fatal(marker, msgSupplier);
        }

        @Override
        public void fatal(Marker marker, Supplier<?> msgSupplier, Throwable t) {
            log.fatal(marker, msgSupplier, t);
        }

        @Override
        public void fatal(Message msg) {
            log.fatal(msg);
        }

        @Override
        public void fatal(Message msg, Throwable t) {
            log.fatal(msg, t);
        }

        @Override
        public void fatal(MessageSupplier msgSupplier) {
            log.fatal(msgSupplier);
        }

        @Override
        public void fatal(MessageSupplier msgSupplier, Throwable t) {
            log.fatal(msgSupplier, t);
        }

        @Override
        public void fatal(CharSequence message) {
            log.fatal(message);
        }

        @Override
        public void fatal(CharSequence message, Throwable t) {
            log.fatal(message, t);
        }

        @Override
        public void fatal(Object message) {
            log.fatal(message);
        }

        @Override
        public void fatal(Object message, Throwable t) {
            log.fatal(message, t);
        }

        @Override
        public void fatal(String message) {
            log.fatal(message);
        }

        @Override
        public void fatal(String message, Object... params) {
            log.fatal(message, params);
        }

        @Override
        public void fatal(String message, Supplier<?>... paramSuppliers) {
            log.fatal(message, paramSuppliers);
        }

        @Override
        public void fatal(String message, Throwable t) {
            log.fatal(message, t);
        }

        @Override
        public void fatal(Supplier<?> msgSupplier) {
            log.fatal(msgSupplier);
        }

        @Override
        public void fatal(Supplier<?> msgSupplier, Throwable t) {
            log.fatal(msgSupplier, t);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0) {
            log.fatal(marker, message, p0);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1) {
            log.fatal(marker, message, p0, p1);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
            log.fatal(marker, message, p0, p1, p2);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            log.fatal(marker, message, p0, p1, p3);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.fatal(marker, message, p0, p1, p3, p4);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.fatal(marker, message, p0, p1, p3, p4, p5);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.fatal(marker, message, p0, p1, p3, p4, p5, p6);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.fatal(marker, message, p0, p1, p3, p4, p5, p6, p7);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.fatal(marker, message, p0, p1, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.fatal(marker, message, p0, p1, p3, p4, p5, p6, p7, p8, p9);
        }

        @Override
        public void fatal(String message, Object p0) {
            log.fatal(message, p0);
        }

        @Override
        public void fatal(String message, Object p0, Object p1) {
            log.fatal(message, p0, p1);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2) {
            log.fatal(message, p0, p1, p2);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
            log.fatal(message, p0, p1, p2, p3);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.fatal(message, p0, p1, p2, p3, p4);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.fatal(message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.fatal(message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        /**
         *
         * End of fatal methods
         *
         */

        @Override
        public Level getLevel() {
            return null;
        }

        @Override
        public <MF extends MessageFactory> MF getMessageFactory() {
            return log.getMessageFactory();
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public void info(Marker marker, Message msg) {

        }

        @Override
        public void info(Marker marker, Message msg, Throwable t) {

        }

        @Override
        public void info(Marker marker, MessageSupplier msgSupplier) {

        }

        @Override
        public void info(Marker marker, MessageSupplier msgSupplier, Throwable t) {

        }

        @Override
        public void info(Marker marker, CharSequence message) {

        }

        @Override
        public void info(Marker marker, CharSequence message, Throwable t) {

        }

        @Override
        public void info(Marker marker, Object message) {

        }

        @Override
        public void info(Marker marker, Object message, Throwable t) {

        }

        @Override
        public void info(Marker marker, String message) {

        }

        @Override
        public void info(Marker marker, String message, Object... params) {

        }

        @Override
        public void info(Marker marker, String message, Supplier<?>... paramSuppliers) {

        }

        @Override
        public void info(Marker marker, String message, Throwable t) {

        }

        @Override
        public void info(Marker marker, Supplier<?> msgSupplier) {

        }

        @Override
        public void info(Marker marker, Supplier<?> msgSupplier, Throwable t) {

        }

        @Override
        public void info(Message msg) {

        }

        @Override
        public void info(Message msg, Throwable t) {

        }

        @Override
        public void info(MessageSupplier msgSupplier) {

        }

        @Override
        public void info(MessageSupplier msgSupplier, Throwable t) {

        }

        @Override
        public void info(CharSequence message) {

        }

        @Override
        public void info(CharSequence message, Throwable t) {

        }

        @Override
        public void info(Object message) {

        }

        @Override
        public void info(Object message, Throwable t) {

        }

        @Override
        public void info(String message) {

        }

        @Override
        public void info(String message, Object... params) {

        }

        @Override
        public void info(String message, Supplier<?>... paramSuppliers) {

        }

        @Override
        public void info(String message, Throwable t) {

        }

        @Override
        public void info(Supplier<?> msgSupplier) {

        }

        @Override
        public void info(Supplier<?> msgSupplier, Throwable t) {

        }

        @Override
        public void info(Marker marker, String message, Object p0) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {

        }

        @Override
        public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {

        }

        @Override
        public void info(String message, Object p0) {

        }

        @Override
        public void info(String message, Object p0, Object p1) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {

        }

        @Override
        public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {

        }

        /**
         *
         * End of info Methods
         *
         */

        @Override
        public boolean isDebugEnabled() {
            return log.isDebugEnabled();
        }

        @Override
        public boolean isDebugEnabled(Marker marker) {
            return log.isDebugEnabled(marker);
        }

        @Override
        public boolean isEnabled(Level level) {
            return log.isEnabled(level);
        }

        @Override
        public boolean isEnabled(Level level, Marker marker) {
            return log.isEnabled(level, marker);
        }

        @Override
        public boolean isErrorEnabled() {
            return log.isErrorEnabled();
        }

        @Override
        public boolean isErrorEnabled(Marker marker) {
            return log.isErrorEnabled(marker);
        }

        @Override
        public boolean isFatalEnabled() {
            return log.isFatalEnabled();
        }

        @Override
        public boolean isFatalEnabled(Marker marker) {
            return log.isFatalEnabled(marker);
        }

        @Override
        public boolean isInfoEnabled() {
            return log.isInfoEnabled();
        }

        @Override
        public boolean isInfoEnabled(Marker marker) {
            return log.isInfoEnabled(marker);
        }

        @Override
        public boolean isTraceEnabled() {
            return log.isTraceEnabled();
        }

        @Override
        public boolean isTraceEnabled(Marker marker) {
            return log.isTraceEnabled(marker);
        }

        @Override
        public boolean isWarnEnabled() {
            return log.isWarnEnabled();
        }

        @Override
        public boolean isWarnEnabled(Marker marker) {
            return log.isWarnEnabled(marker);
        }

        /**
         *
         * End of IS Methods
         *
         */

        @Override
        public void log(Level level, Marker marker, Message msg) {
            log.log(level, marker, msg);
        }

        @Override
        public void log(Level level, Marker marker, Message msg, Throwable t) {
            log.log(level, marker, msg, t);
        }

        @Override
        public void log(Level level, Marker marker, MessageSupplier msgSupplier) {
            log.log(level, marker, msgSupplier);
        }

        @Override
        public void log(Level level, Marker marker, MessageSupplier msgSupplier, Throwable t) {
            log.log(level, marker, msgSupplier, t);
        }

        @Override
        public void log(Level level, Marker marker, CharSequence message) {
            log.log(level, marker, message);
        }

        @Override
        public void log(Level level, Marker marker, CharSequence message, Throwable t) {
            log.log(level, marker, message, t);
        }

        @Override
        public void log(Level level, Marker marker, Object message) {
            log.log(level, marker, message);
        }

        @Override
        public void log(Level level, Marker marker, Object message, Throwable t) {
            log.log(level, marker, message, t);
        }

        @Override
        public void log(Level level, Marker marker, String message) {
            log.log(level, marker, message);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object... params) {
            log.log(level, marker, message, params);
        }

        @Override
        public void log(Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
            log.log(level, marker, message, paramSuppliers);
        }

        @Override
        public void log(Level level, Marker marker, String message, Throwable t) {
            log.log(level, marker, message, t);
        }

        @Override
        public void log(Level level, Marker marker, Supplier<?> msgSupplier) {
            log.log(level, marker, msgSupplier);
        }

        @Override
        public void log(Level level, Marker marker, Supplier<?> msgSupplier, Throwable t) {
            log.log(level, marker, msgSupplier, t);
        }

        @Override
        public void log(Level level, Message msg) {
            log.log(level, msg);
        }

        @Override
        public void log(Level level, Message msg, Throwable t) {
            log.log(level, msg, t);
        }

        @Override
        public void log(Level level, MessageSupplier msgSupplier) {
            log.log(level, msgSupplier);
        }

        @Override
        public void log(Level level, MessageSupplier msgSupplier, Throwable t) {
            log.log(level, msgSupplier, t);
        }

        @Override
        public void log(Level level, CharSequence message) {
            log.log(level, message);
        }

        @Override
        public void log(Level level, CharSequence message, Throwable t) {
            log.log(level, message, t);
        }

        @Override
        public void log(Level level, Object message) {
            log.log(level, message);
        }

        @Override
        public void log(Level level, Object message, Throwable t) {
            log.log(level, message, t);
        }

        @Override
        public void log(Level level, String message) {
            log.log(level, message);
        }

        @Override
        public void log(Level level, String message, Object... params) {
            log.log(level, message, params);
        }

        @Override
        public void log(Level level, String message, Supplier<?>... paramSuppliers) {
            log.log(level, message, paramSuppliers);
        }

        @Override
        public void log(Level level, String message, Throwable t) {
            log.log(level, message, t);
        }

        @Override
        public void log(Level level, Supplier<?> msgSupplier) {
            log.log(level, msgSupplier);
        }

        @Override
        public void log(Level level, Supplier<?> msgSupplier, Throwable t) {
            log.log(level, msgSupplier, t);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0) {
            log.log(level, marker, message, p0);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1) {
            log.log(level, marker, message, p0, p1);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
            log.log(level, marker, message, p0, p1, p2);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            log.log(level, marker, message, p0, p1, p2, p3);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.log(level, marker, message, p0, p1, p2, p3, p4);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.log(level, marker, message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.log(level, marker, message, p0, p1, p2, p3, p4, p5, p7);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.log(level, marker, message, p0, p1, p2, p3, p4, p5, p7, p8);
        }

        @Override
        public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.log(level, marker, message, p0, p1, p2, p3, p4, p5, p7, p8, p9);
        }

        @Override
        public void log(Level level, String message, Object p0) {
            log.log(level, message, p0);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1) {
            log.log(level, message, p0, p1);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2) {
            log.log(level, message, p0, p1, p2);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
            log.log(level, message, p0, p1, p2, p3);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.log(level, message, p0, p1, p2, p3, p4);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.log(level, message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.log(level, message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.log(level, message, p0, p1, p2, p3, p4, p5, p7);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.log(level, message, p0, p1, p2, p3, p4, p5, p7, p8);
        }

        @Override
        public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.log(level, message, p0, p1, p2, p3, p4, p5, p7, p8, p9);
        }

        /**
         *
         * End of log Methods
         *
         */

        @Override
        public void printf(Level level, Marker marker, String format, Object... params) {
            log.printf(level, marker, format, params);
        }

        @Override
        public void printf(Level level, String format, Object... params) {
            log.printf(level, format, params);
        }

        @Override
        public <T extends Throwable> T throwing(Level level, T t) {
            return log.throwing(level, t);
        }

        @Override
        public <T extends Throwable> T throwing(T t) {
            return log.throwing(t);
        }

        @Override
        public void trace(Marker marker, Message msg) {
            log.trace(marker, msg);
        }

        @Override
        public void trace(Marker marker, Message msg, Throwable t) {
            log.trace(marker, msg, t);
        }

        @Override
        public void trace(Marker marker, MessageSupplier msgSupplier) {
            log.trace(marker, msgSupplier);
        }

        @Override
        public void trace(Marker marker, MessageSupplier msgSupplier, Throwable t) {
            log.trace(marker, msgSupplier, t);
        }

        @Override
        public void trace(Marker marker, CharSequence message) {
            log.trace(marker, message);
        }

        @Override
        public void trace(Marker marker, CharSequence message, Throwable t) {
            log.trace(marker, message, t);
        }

        @Override
        public void trace(Marker marker, Object message) {
            log.trace(marker, message);
        }

        @Override
        public void trace(Marker marker, Object message, Throwable t) {
            log.trace(marker, message, t);
        }

        @Override
        public void trace(Marker marker, String message) {
            log.trace(marker, message);
        }

        @Override
        public void trace(Marker marker, String message, Object... params) {
            log.trace(marker, message, params);
        }

        @Override
        public void trace(Marker marker, String message, Supplier<?>... paramSuppliers) {
            log.trace(marker, message, paramSuppliers);
        }

        @Override
        public void trace(Marker marker, String message, Throwable t) {
            log.trace(marker, message, t);
        }

        @Override
        public void trace(Marker marker, Supplier<?> msgSupplier) {
            log.trace(marker, msgSupplier);
        }

        @Override
        public void trace(Marker marker, Supplier<?> msgSupplier, Throwable t) {
            log.trace(marker, msgSupplier, t);
        }

        @Override
        public void trace(Message msg) {
            log.trace(msg);
        }

        @Override
        public void trace(Message msg, Throwable t) {
            log.trace(msg, t);
        }

        @Override
        public void trace(MessageSupplier msgSupplier) {
            log.trace(msgSupplier);
        }

        @Override
        public void trace(MessageSupplier msgSupplier, Throwable t) {
            log.trace(msgSupplier, t);
        }

        @Override
        public void trace(CharSequence message) {
            log.trace(message);
        }

        @Override
        public void trace(CharSequence message, Throwable t) {
            log.trace(message, t);
        }

        @Override
        public void trace(Object message) {
            log.trace(message);
        }

        @Override
        public void trace(Object message, Throwable t) {
            log.trace(message, t);
        }

        @Override
        public void trace(String message) {
            log.trace(message);
        }

        @Override
        public void trace(String message, Object... params) {
            log.trace(message, params);
        }

        @Override
        public void trace(String message, Supplier<?>... paramSuppliers) {
            log.trace(message, paramSuppliers);
        }

        @Override
        public void trace(String message, Throwable t) {
            log.trace(message, t);
        }

        @Override
        public void trace(Supplier<?> msgSupplier) {
            log.trace(msgSupplier);
        }

        @Override
        public void trace(Supplier<?> msgSupplier, Throwable t) {
            log.trace(msgSupplier, t);
        }

        @Override
        public void trace(Marker marker, String message, Object p0) {
            log.trace(marker, message, p0);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1) {
            log.trace(marker, message, p0, p1);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
            log.trace(marker, message, p0, p1, p2);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            log.trace(marker, message, p0, p1, p2, p3);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.trace(marker, message, p0, p1, p2, p3, p4);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.trace(marker, message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.trace(marker, message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        @Override
        public void trace(String message, Object p0) {
            log.trace(message, p0);
        }

        @Override
        public void trace(String message, Object p0, Object p1) {
            log.trace(message, p0, p1);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2) {
            log.trace(message, p0, p1, p2);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
            log.trace(message, p0, p1, p2, p3);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            log.trace(message, p0, p1, p2, p3, p4);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            log.trace(message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            log.trace(message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            log.trace(message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            log.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            log.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        @Override
        public EntryMessage traceEntry() {
            return log.traceEntry();
        }

        @Override
        public EntryMessage traceEntry(String format, Object... params) {
            return log.traceEntry(format, params);
        }

        @Override
        public EntryMessage traceEntry(Supplier<?>... paramSuppliers) {
            return log.traceEntry(paramSuppliers);
        }

        @Override
        public EntryMessage traceEntry(String format, Supplier<?>... paramSuppliers) {
            return log.traceEntry(format, paramSuppliers);
        }

        @Override
        public EntryMessage traceEntry(Message message) {
            return log.traceEntry(message);
        }

        @Override
        public void traceExit() {
            log.traceExit();
        }

        @Override
        public <R> R traceExit(R result) {
            return log.traceExit(result);
        }

        @Override
        public <R> R traceExit(String format, R result) {
            return log.traceExit(format, result);
        }

        @Override
        public void traceExit(EntryMessage message) {
            log.traceExit(message);
        }

        @Override
        public <R> R traceExit(EntryMessage message, R result) {
            return log.traceExit(message, result);
        }

        @Override
        public <R> R traceExit(Message message, R result) {
            return log.traceExit(message, result);
        }

        /**
         *
         * End of trace Methods
         *
         */

        @Override
        public void warn(Marker marker, Message msg) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, msg);
        }

        @Override
        public void warn(Marker marker, Message msg, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, msg, t);
        }

        @Override
        public void warn(Marker marker, MessageSupplier msgSupplier) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, msgSupplier);
        }

        @Override
        public void warn(Marker marker, MessageSupplier msgSupplier, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, msgSupplier, t);
        }

        @Override
        public void warn(Marker marker, CharSequence message) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message);
        }

        @Override
        public void warn(Marker marker, CharSequence message, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, t);
        }

        @Override
        public void warn(Marker marker, Object message) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message);
        }

        @Override
        public void warn(Marker marker, Object message, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, t);
        }

        @Override
        public void warn(Marker marker, String message) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message);
        }

        @Override
        public void warn(Marker marker, String message, Object... params) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, params);
        }

        @Override
        public void warn(Marker marker, String message, Supplier<?>... paramSuppliers) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, paramSuppliers);
        }

        @Override
        public void warn(Marker marker, String message, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, t);
        }

        @Override
        public void warn(Marker marker, Supplier<?> msgSupplier) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, msgSupplier);
        }

        @Override
        public void warn(Marker marker, Supplier<?> msgSupplier, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, msgSupplier, t);
        }

        @Override
        public void warn(Message msg) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(msg);
        }

        @Override
        public void warn(Message msg, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(msg, t);
        }

        @Override
        public void warn(MessageSupplier msgSupplier) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(msgSupplier);
        }

        @Override
        public void warn(MessageSupplier msgSupplier, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(msgSupplier, t);
        }

        @Override
        public void warn(CharSequence message) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message);
        }

        @Override
        public void warn(CharSequence message, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, t);
        }

        @Override
        public void warn(Object message) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message);
        }

        @Override
        public void warn(Object message, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, t);
        }

        @Override
        public void warn(String msg) {
            if (!internal.warnPrintFML.getValues()) return;
            this.warn(msg);
        }

        @Override
        public void warn(String msg, Object... params) {
            if (!internal.warnPrintFML.getValues()) return;
            this.warn(msg, params);
        }

        @Override
        public void warn(String message, Supplier<?>... paramSuppliers) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, paramSuppliers);
        }

        @Override
        public void warn(String message, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, t);
        }

        @Override
        public void warn(Supplier<?> msgSupplier) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(msgSupplier);
        }

        @Override
        public void warn(Supplier<?> msgSupplier, Throwable t) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(msgSupplier, t);
        }

        @Override
        public void warn(Marker marker, String message, Object p0) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3, p4);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

        @Override
        public void warn(String message, Object p0) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0);
        }

        @Override
        public void warn(String message, Object p0, Object p1) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3, p4);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3, p4, p5);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3, p4, p5, p6);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3, p4, p5, p6, p7);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }

        @Override
        public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            if (!internal.warnPrintFML.getValues()) return;
            log.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }

    }