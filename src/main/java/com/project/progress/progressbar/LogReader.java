/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.progress.progressbar;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

/**
 *
 * @author shiyam
 */
public class LogReader extends PrintStream {

    final StringBuilder buf;
    final PrintStream underlying;

    LogReader(StringBuilder sb, OutputStream os, PrintStream ul) {
        super(os);
        this.buf = sb;
        this.underlying = ul;
    }

    public static LogReader create(PrintStream toLog) {
        try {
            final StringBuilder sb = new StringBuilder();
            Field f = FilterOutputStream.class.getDeclaredField("out");
            f.setAccessible(true);
            OutputStream psout = (OutputStream) f.get(toLog);
            return new LogReader(sb, new FilterOutputStream(psout) {
                public void write(int b) throws IOException {
                    super.write(b);
                    sb.append((char) b);
                }
            }, toLog);
        } catch (NoSuchFieldException shouldNotHappen) {
        } catch (IllegalArgumentException shouldNotHappen) {
        } catch (IllegalAccessException shouldNotHappen) {
        }
        return null;
    }
}


