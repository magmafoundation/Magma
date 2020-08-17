/*
 * Magma Server
 * Copyright (C) 2019-2020.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.magmafoundation.magma.log4j;

import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.*;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "highlightMsg", category = PatternConverter.CATEGORY)
@ConverterKeys({"highlightMsg"})
@PerformanceSensitive("allocation")
public class HighlightMsgConverter extends LogEventPatternConverter {

    private static final String ANSI_RESET = "\u001B[39;0m";
    private static final String ANSI_ERROR = getError();
    private static final String ANSI_WARN = getWarn();
    private static final String ANSI_INFO = getInfo();
    private static final String ANSI_FATAL = getFatal();
    private static final String ANSI_TRACE = getTrace();

    private final List<PatternFormatter> formatters;

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to highlight
     */
    protected HighlightMsgConverter(List<PatternFormatter> formatters) {
        super("highlightMsg", null);
        this.formatters = formatters;
    }

    /**
     * Gets a new instance of the {@link HighlightMsgConverter} with the specified options.
     *
     * @param config The current configuration
     * @param options The pattern options
     * @return The new instance
     */
    @Nullable
    public static HighlightMsgConverter newInstance(Configuration config, String[] options) {
        if (options.length != 1) {
            LOGGER.error("Incorrect number of options on highlightMsg. Expected 1 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            LOGGER.error("No pattern supplied on highlightMsg");
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HighlightMsgConverter(formatters);
    }

    public static String getError() {
        return getColor("c", "\u001B[31;1m");
    }

    public static String getWarn() {
        return getColor("e", "\u001B[33;1m");
    }

    public static String getInfo() {
        return getColor("2", "\u001B[32;22m");
    }

    public static String getFatal() {
        return getColor("e", "\u001B[31;1m");
    }

    public static String getTrace() {
        return getColor("e", "\u001B[31;1m");
    }

    private static String getColor(String text, String d) {
        switch (text) {
            case "1":
                text = "\u001B[34;22m";
                break;
            case "2":
                text = "\u001B[32;22m";
                break;
            case "3":
                text = "\u001B[36;22m";
                break;
            case "4":
                text = "\u001B[31;22m";
                break;
            case "5":
                text = "\u001B[35;22m";
                break;
            case "6":
                text = "\u001B[33;22m";
                break;
            case "7":
                text = "\u001B[37;22m";
                break;
            case "8":
                text = "\u001B[30;1m";
                break;
            case "9":
                text = "\u001B[34;1m";
                break;
            case "a":
                text = "\u001B[32;1m";
                break;
            case "b":
                text = "\u001B[36;1m";
                break;
            case "c":
                text = "\u001B[31;1m";
                break;
            case "d":
                text = "\u001B[35;1m";
                break;
            case "e":
                text = "\u001B[33;1m";
                break;
            case "f":
                text = "\u001B[37;1m";
                break;
            case "r":
                text = "\u001B[39;0m";
                break;
            default:
                text = d;
        }
        return text;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        Level level = event.getLevel();
        if (level.isMoreSpecificThan(Level.ERROR)) {
            format(ANSI_ERROR, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.WARN)) {
            format(ANSI_WARN, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.INFO)) {
            format(ANSI_INFO, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.FATAL)) {
            format(ANSI_FATAL, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.TRACE)) {
            format(ANSI_TRACE, event, toAppendTo);
            return;
        }

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++) {
            formatters.get(i).format(event, toAppendTo);
        }
    }

    private void format(String style, LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        toAppendTo.append(style);
        int end = toAppendTo.length();

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++) {
            formatters.get(i).format(event, toAppendTo);
        }

        if (toAppendTo.length() == end) {
            // No content so we don't need to append the ANSI escape code
            toAppendTo.setLength(start);
        } else {
            // Append reset code after the line
            toAppendTo.append(ANSI_RESET);
        }
    }

    @Override
    public boolean handlesThrowable() {
        for (final PatternFormatter formatter : formatters) {
            if (formatter.handlesThrowable()) {
                return true;
            }
        }
        return false;
    }
}
