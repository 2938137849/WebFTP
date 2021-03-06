package bin.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author bin
 * @version 1.0.0
 */
public class FTPUtil {
  /** 账号 */
  private final static String username;
  /** 密码 */
  private final static String password;
  /** FTP服务器地址 */
  private final static String ip;
  /** 服务器端口号 */
  private final static String port;

  static {
    Properties pp = new Properties();
    try {
      pp.load(FTPUtil.class.getClassLoader().getResourceAsStream("config.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ip = pp.getProperty("FTP.ip");
    port = pp.getProperty("FTP.port");
    username = pp.getProperty("FTP.username");
    password = pp.getProperty("FTP.password");
  }

  public static FTPClient connect() {
    FTPClient ftp = new FTPClient();
    try {
      ftp.connect(ip, Integer.parseInt(port));
      ftp.login(username, password);
      ftp.setControlEncoding("utf-8");
      ftp.setFileType(FTP.BINARY_FILE_TYPE);
      ftp.enterLocalPassiveMode();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ftp;
  }

  public static boolean isOpen(FTPClient client) {
    return client != null && FTPReply.isPositiveCompletion(client.getReplyCode());
  }

  public static boolean changeWorkingDirectory(FTPClient client, String pathName) throws IOException {
    return client.changeWorkingDirectory(new String(pathName.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
  }

  public static boolean retrieveFile(FTPClient client, String remote, OutputStream local)
     throws IOException {
    return client.retrieveFile(new String(remote.getBytes(), FTP.DEFAULT_CONTROL_ENCODING), local);
  }

  public static boolean retrieveFile(FTPClient client, FTPFile remote, OutputStream local)
     throws IOException {
    return client.retrieveFile(new String(remote.getName().getBytes(), FTP.DEFAULT_CONTROL_ENCODING), local);
  }

  public static void close(FTPClient client) {
    if (client != null && client.isConnected()) {
      try {
        client.logout();
        client.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private FTPUtil() {
  }
}

