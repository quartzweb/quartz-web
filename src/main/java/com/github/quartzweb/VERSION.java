package com.github.quartzweb;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class VERSION {
    public final static int MajorVersion    = 1;
    public final static int MinorVersion    = 1;
    public final static int RevisionVersion = 1;

    public static String getVersionNumber() {
        return VERSION.MajorVersion + "." + VERSION.MinorVersion + "." + VERSION.RevisionVersion;
    }
}
