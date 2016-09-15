package com.m4sg.crm4marketingsunset.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Created by Juan on 26/02/2015.
 */
public class FileIO {

    /** The size of blocking to use */
    //protected static final int BUFFER_LENGTH = 1024;
    protected static final int BUFFER_LENGTH = 16384;

    /** Nobody should need to create an instance; all methods are static */
    private FileIO() {
        // Nothing to do
    }

    // save uploaded file to new location
    public static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        //final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
    }

    /**
     * Copy a file to a directory, given File objects representing the files.
     * @param file File representing the source, must be a single file.
     * @param target File representing the location, may be file or directory.
     * @throws IOException
     */
    public static void copyFile(File file, File target) throws IOException {
        if (!file.exists() || !file.isFile() || !(file.canRead())) {
            throw new IOException(file + " is not a readable file");
        }
        File dest = target;
        if (target.isDirectory()) {
            dest = new File(dest, file.getName());
        }
        InputStream is = null;
        OutputStream os  = null;
        try {
            is = new FileInputStream(file);
            os = new FileOutputStream(dest);
            int count;        // the byte count
            byte[] b = new byte[BUFFER_LENGTH];    // the bytes read from the file
            while ((count = is.read(b)) != -1) {
                os.write(b, 0, count);
            }
        } finally {
            is.close();
            os.close();
        }
    }


    public static void scp (String host,Integer port,String username,String password, String origin,String dest){

        TreeMap treeMap=new TreeMap();
        treeMap.put(0, new String[]{origin, dest});
        scp(host, port, username, password, treeMap);
    }

    public static void scp(String host,Integer port, String user, String password, TreeMap lista) {
        try {
            JSch e = new JSch();
            Session session = e.getSession(user, host, port);
            FileInputStream fis = null;
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            System.out.println("Conectarse a host:" + host);
            session.connect();
            boolean ptimestamp = true;

            for(int x = 0; x < lista.size(); ++x) {
                String[] files = (String[])lista.get(new Integer(x));
                String lfile = files[0];
                String rfile = files[1];
                System.out.println("Copiar" + rfile);
                String command = "scp " + (ptimestamp?"-p":"") + " -t " + rfile;
                Channel channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand(command);
                OutputStream out = channel.getOutputStream();
                InputStream in = channel.getInputStream();
                channel.connect();
                if(checkAck(in) != 0) {
                    System.exit(0);
                }

                File _lfile = new File(lfile);
                if(ptimestamp) {
                    command = "T " + _lfile.lastModified() / 1000L + " 0";
                    command = command + " " + _lfile.lastModified() / 1000L + " 0\n";
                    out.write(command.getBytes());
                    out.flush();
                    if(checkAck(in) != 0) {
                        System.exit(0);
                    }
                }

                long filesize = _lfile.length();
                command = "C0644 " + filesize + " ";
                if(lfile.lastIndexOf(47) > 0) {
                    command = command + lfile.substring(lfile.lastIndexOf(47) + 1);
                } else {
                    command = command + lfile;
                }

                command = command + "\n";
                out.write(command.getBytes());
                out.flush();
                if(checkAck(in) != 0) {
                    System.exit(0);
                }

                fis = new FileInputStream(lfile);
                byte[] buf = new byte[1024];

                while(true) {
                    int len = fis.read(buf, 0, buf.length);
                    if(len <= 0) {
                        fis.close();
                        fis = null;
                        buf[0] = 0;
                        out.write(buf, 0, 1);
                        out.flush();
                        if(checkAck(in) != 0) {
                            System.exit(0);
                        }

                        out.close();
                        channel.disconnect();
                        break;
                    }

                    out.write(buf, 0, len);
                }
            }

            session.disconnect();
            System.out.println("Se finalizo la copia");
        } catch (Exception var23) {
            System.out.println(var23);
        }

    }
    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        if(b == 0) {
            return b;
        } else if(b == -1) {
            return b;
        } else {
            if(b == 1 || b == 2) {
                StringBuffer sb = new StringBuffer();

                int c;
                do {
                    c = in.read();
                    sb.append((char)c);
                } while(c != 10);

                if(b == 1) {
                    System.out.print(sb.toString());
                }

                if(b == 2) {
                    System.out.print(sb.toString());
                }
            }

            return b;
        }
    }


    public static Properties readProperties() throws IOException {

        Properties prop = new Properties();
        InputStream input = null;
        String filename = "general.properties";
        input = new FileInputStream(filename);
        if(input==null){
            System.out.println("Sorry, unable to find " + filename);
            return null;
        }

        //load a properties file from class path, inside static method
        prop.load(input);
        return prop;
    }
}

