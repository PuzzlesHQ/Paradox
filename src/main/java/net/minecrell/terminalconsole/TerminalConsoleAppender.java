/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Minecrell <https://github.com/Minecrell>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.minecrell.terminalconsole;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

/**
 * An {@link Appender} that uses the JLine 3.x {@link Terminal} to print messages
 * to the console.
 *
 * <p>The JLine {@link Terminal} extends the regular console output with support
 * for Ansi escape codes on Windows. Additionally, it's {@link LineReader}
 * interface can be used to implement enhanced console input, with an
 * persistent input line, as well as command history and command completion.</p>
 *
 * <p>The {@code TerminalConsole} appender replaces the default {@code Console}
 * appender in your log4j configuration.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code  <TerminalConsole>
 *     <PatternLayout pattern="[%d{HH:mm:ss} %level]: %msg%n"/>
 * </TerminalConsole>}</pre>
 *
 * <p>To use the enhanced console input it is necessary to set the
 * {@link LineReader} using {@link #setReader(LineReader)}. The appender will
 * then automatically redraw the current prompt. When creating the
 * {@link LineReader} it's important to use the {@link Terminal}
 * returned by {@link #getTerminal()}.</p>
 *
 * <p>By default, the JLine {@link Terminal} is enabled when the application
 * is started with an attached terminal session. Usually, this is only the
 * case if the application is started from the command line, not if it gets
 * started by another application.</p>
 *
 * <p>In some cases, it might be possible to support a subset of the features
 * in these unsupported environments (e.g. only ANSI color codes). In these
 * cases, the system properties may be used to override the default behaviour:
 * </p>
 *
 * <ul>
 *     <li>{@link #JLINE_OVERRIDE_PROPERTY} - To enable the extended JLine
 *     input. By default this will also enable the ANSI escape codes.</li>
 *     <li>{@link #ANSI_OVERRIDE_PROPERTY} - To enable the output of ANSI
 *     escape codes. May be used to force the use of ANSI escape codes
 *     if JLine is disabled or to disable them if it is enabled.</li>
 * </ul>
 */
@Plugin(name = TerminalConsoleAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class TerminalConsoleAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "TerminalConsole";

    /**
     * The prefix used for all system properties in TerminalConsoleAppender.
     */
    static final String PROPERTY_PREFIX = "terminal";

    /**
     * System property that allows overriding the default detection of the
     * console to force enable or force disable the use of JLine. In some
     * environments the automatic detection might not work properly.
     *
     * <p>If this system property is not set, or set to an invalid value
     * (neither {@code true} nor {@code false}) then we will attempt
     * to detect the best option automatically.</p>
     */
    public static final String JLINE_OVERRIDE_PROPERTY = PROPERTY_PREFIX + ".jline";

    /**
     * System property that allows overriding the use of ANSI escape codes
     * for console formatting even though running in an unsupported
     * environment. By default, ANSI color codes are only enabled if JLine
     * is enabled. Some systems might be able to handle ANSI escape codes
     * but are not capable of JLine's extended input mechanism.
     *
     * <p>If this system property is not set, or set to an invalid value
     * (neither {@code true} nor {@code false}) then we will attempt
     * to detect the best option automatically.</p>
     */
    public static final String ANSI_OVERRIDE_PROPERTY = PROPERTY_PREFIX + ".ansi";

    private static final @Nullable Boolean ANSI_OVERRIDE = getOptionalBooleanProperty(ANSI_OVERRIDE_PROPERTY);

    /**
     * We grab the standard output {@link PrintStream} early, otherwise we
     * might cause infinite loops later if the application redirects
     * {@link System#out} to Log4J.
     */
    private static final PrintStream stdout = System.out;

    private static boolean initialized;
    private static @Nullable Terminal terminal;
    private static @Nullable LineReader reader;

    /**
     * Returns the {@link Terminal} that is used to print messages to the
     * console. Returns {@code null} in unsupported environments, unless
     * overridden using the {@link #JLINE_OVERRIDE_PROPERTY} system
     * property.
     *
     * @return The terminal, or null if not supported
     * @see TerminalConsoleAppender
     */
    public synchronized static @Nullable Terminal getTerminal() {
        return terminal;
    }

    /**
     * Returns the currently configured {@link LineReader} that is used to
     * read input from the console. May be null if no {@link LineReader}
     * was configured by the environment.
     *
     * @return The current line reader, or null if none
     */
    public synchronized static @Nullable LineReader getReader() {
        return reader;
    }

    /**
     * Sets the {@link LineReader} that is used to read input from the console.
     * Setting the {@link LineReader} will allow the appender to automatically
     * redraw the input line when a new log message is added.
     *
     * <p><b>Note:</b> The specified {@link LineReader} must be created with
     * the terminal returned by {@link #getTerminal()}.</p>
     *
     * @param newReader The new line reader
     */
    public synchronized static void setReader(@Nullable LineReader newReader) {
        if (newReader != null && newReader.getTerminal() != terminal) {
            throw new IllegalArgumentException("Reader was not created with TerminalConsoleAppender.getTerminal()");
        }

        reader = newReader;
    }

    /**
     * Returns whether ANSI escapes codes should be written to the console
     * output.
     *
     * <p>The return value is {@code true} by default if the JLine terminal
     * is enabled and {@code false} otherwise. It may be overridden using
     * the {@link #ANSI_OVERRIDE_PROPERTY} system property.</p>
     *
     * @return true if ANSI escapes codes should be written to the console
     */
    public static boolean isAnsiSupported() {
        if (!initialized)
            initializeTerminal();
        return ANSI_OVERRIDE != null ? ANSI_OVERRIDE : terminal != null;
    }

    /**
     * Constructs a new {@link TerminalConsoleAppender}.
     *
     * @param name The name of the appender
     * @param filter The filter, can be {@code null}
     * @param layout The layout to use
     * @param ignoreExceptions If {@code true} exceptions encountered when
     *     appending events are logged, otherwise they are propagated to the
     *     caller
     */
    protected TerminalConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        if (!initialized)
            initializeTerminal();
    }

    private synchronized static void initializeTerminal() {
        if (!initialized) {
            initialized = true;

            // A system property can be used to override our automatic detection
            @Nullable Boolean jlineOverride = getOptionalBooleanProperty(JLINE_OVERRIDE_PROPERTY);

            // By default, we disable JLine if there is no terminal attached
            // (e.g. if the program output is redirected to a file or if it's
            // started by some kind of control panel)

            // The same applies to IDEs, they usually provide only a very basic
            // console implementation without support for ANSI escape codes
            // (used for colors) or characters like \r.

            // There are two exceptions:
            //  1. IntelliJ IDEA supports colors and control characters
            //     (We try to detect it using an additional JAR it adds to the classpath)
            //  2. The system property forces the use of JLine.
            boolean dumb = jlineOverride == Boolean.TRUE || System.getProperty("java.class.path").contains("idea_rt.jar");

            if (jlineOverride != Boolean.FALSE) {
                try {
                    terminal = TerminalBuilder.builder().dumb(dumb).build();
                } catch (IllegalStateException e) {
                    // Unless disabled using one of the exceptions above,
                    // JLine throws an exception before creating a dumb terminal
                    // Dumb terminals are used if there is no real terminal attached
                    // to the application.

                    if (LOGGER.isDebugEnabled()) {
                        // Log with stacktrace
                        LOGGER.warn("Advanced terminal features are not available in this environment", e);
                    } else {
                        LOGGER.warn("Advanced terminal features are not available in this environment");
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to initialize terminal. Falling back to standard console", e);
                }
            }
        }
    }

    @Override
    public void append(LogEvent event) {
        print(getLayout().toSerializable(event).toString());
    }

    public synchronized static void print(String text) {
        if (terminal != null) {
            if (reader != null) {
                // Draw the prompt line again if a reader is available
                reader.printAbove(text);
            } else {
                terminal.writer().print(text);
                terminal.writer().flush();
            }
        } else {
            stdout.print(text);
        }
    }

    /**
     * Closes the JLine {@link Terminal} (if available) and restores the original
     * terminal settings.
     *
     * @throws IOException If an I/O error occurs
     */
    public synchronized static void close() throws IOException {
        if (initialized) {
            initialized = false;
            reader = null;
            if (terminal != null) {
                try {
                    terminal.close();
                } finally {
                    terminal = null;
                }
            }
        }
    }

    /**
     * Creates a new {@link TerminalConsoleAppender}.
     *
     * @param name The name of the appender
     * @param filter The filter, can be {@code null}
     * @param layout The layout, can be {@code null}
     * @param ignoreExceptions If {@code true} exceptions encountered when
     *     appending events are logged, otherwise they are propagated to the
     *     caller
     * @return The new appender
     */
    @PluginFactory
    public static TerminalConsoleAppender createAppender(
            @Required(message = "No name provided for TerminalConsoleAppender") @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") @Nullable Layout<? extends Serializable> layout,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) boolean ignoreExceptions) {

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new TerminalConsoleAppender(name, filter, layout, ignoreExceptions);
    }

    private static @Nullable Boolean getOptionalBooleanProperty(String name) {
        String value = PropertiesUtil.getProperties().getStringProperty(name);
        if (value == null) {
            return null;
        }

        if (value.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        } else if (value.equalsIgnoreCase("false"))  {
            return Boolean.FALSE;
        } else {
            LOGGER.warn("Invalid value for boolean input property '{}': {}", name, value);
            return null;
        }
    }

}
