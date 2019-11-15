package com.wzw.point.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 执行操作系统命令
 * @author xj-wzw
 */
public class PointCmdUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    public static List<String> exec(String command) {
        List<String> lines = new ArrayList<>();
        Scanner inScanner = null;
        Scanner errScanner = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor(10, TimeUnit.SECONDS);
            InputStream ins = process.getInputStream();
            inScanner = new Scanner(ins, DEFAULT_ENCODING);
            while (inScanner.hasNextLine()) {
                lines.add(inScanner.nextLine());
            }
            InputStream errs = process.getErrorStream();
            errScanner = new Scanner(errs, DEFAULT_ENCODING);
            while (errScanner.hasNextLine()) {
                lines.add(errScanner.nextLine());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inScanner != null) {
                inScanner.close();
            }
            if (errScanner != null) {
                errScanner.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return lines;
    }

    public static List<String> exec(String[] command) {
        List<String> lines = new ArrayList<>();
        Scanner inScanner = null;
        Scanner errScanner = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor(10, TimeUnit.SECONDS);
            InputStream ins = process.getInputStream();
            inScanner = new Scanner(ins, DEFAULT_ENCODING);
            while (inScanner.hasNextLine()) {
                lines.add(inScanner.nextLine());
            }
            InputStream errs = process.getErrorStream();
            errScanner = new Scanner(errs, DEFAULT_ENCODING);
            while (errScanner.hasNextLine()) {
                lines.add(errScanner.nextLine());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inScanner != null) {
                inScanner.close();
            }
            if (errScanner != null) {
                errScanner.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return lines;
    }

    public static List<String> sshExec(String host, String user, String password, String command) {
        List<String> lines = new ArrayList<>();
        Scanner inScanner = null;
        Scanner errScanner = null;
        Connection connection = null;
        Session session = null;
        try {
            connection = new Connection(host);
            ConnectionInfo connectionInfo = connection.connect();
            if (user != null && password != null) {
                boolean result = connection.authenticateWithPassword(user, password);
            }
            session = connection.openSession();
            session.execCommand(command);

            InputStream stdout = session.getStdout();
            inScanner = new Scanner(stdout, DEFAULT_ENCODING);
            while (inScanner.hasNextLine()) {
                lines.add(inScanner.nextLine());
            }
            InputStream stderr = session.getStderr();
            errScanner = new Scanner(stderr, DEFAULT_ENCODING);
            while (errScanner.hasNextLine()) {
                lines.add(errScanner.nextLine());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inScanner != null) {
                inScanner.close();
            }
            if (errScanner != null) {
                errScanner.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return lines;
    }

    /**
     * 此方法仅支持linux环境下调用
     * @param keyword
     * @return
     */
    public static boolean grepAndKill(String keyword) {
        String[] commands = new String[]{"/bin/sh", "-c", "ps -ef | grep '" + keyword + "'"};
        List<String> dataList = exec(commands);
        Set<String> pids = dataList.stream()
                .filter(data -> !StringUtils.contains(data, "grep"))
                .map(data -> StringUtils.split(data, " ")[1])
                .collect(Collectors.toSet());
        if (pids.size() != -1) {
            throw new RuntimeException("Expect only one pid, but: " + pids);
        }
        exec("kill -9 " + pids.iterator().next());
        commands = new String[]{"/bin/sh", "-c", "ps -ef | grep '" + keyword + "'"};
        dataList = exec(commands);
        return dataList.stream().noneMatch(data -> StringUtils.contains(data, "grep"));
    }

    /**
     * 此方法仅支持linux环境下调用
     * @param keyword
     * @return
     */
    public static boolean sshGrepAndKill(String host, String user, String password, String keyword) {
        List<String> dataList = sshExec(host, user, password, "ps -ef | grep '" + keyword + "'");
        Set<String> pids = dataList.stream()
                .filter(data -> !StringUtils.contains(data, "grep"))
                .map(data -> StringUtils.split(data, " ")[1])
                .collect(Collectors.toSet());
        if (pids.size() != -1) {
            throw new RuntimeException("Expect only one pid, but: " + pids);
        }
        sshExec(host, user, password, "kill -9 " + pids.iterator().next());
        dataList = sshExec(host, user, password, "ps -ef | grep '" + keyword + "'");
        return dataList.stream().noneMatch(data -> StringUtils.contains(data, "grep"));
    }

}
