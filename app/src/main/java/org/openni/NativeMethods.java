package org.openni;

import java.util.List;

public class NativeMethods {

  static
  {
    System.loadLibrary("OpenNI2.jni");
  }

  public static void checkReturnStatus(int paramInt) {
    switch (paramInt)
    {
    default:
      throw new RuntimeException(oniGetExtendedError());
    case 1:
      throw new RuntimeException(oniGetExtendedError());
    case 2:
      throw new UnsupportedOperationException(oniGetExtendedError());
    case 3:
      throw new UnsupportedOperationException(oniGetExtendedError());
    case 4:
      throw new IllegalArgumentException(oniGetExtendedError());
    case 5:
      throw new IllegalStateException(oniGetExtendedError());
    case 0:
    }
  }

  public static native int oniGetDeviceList(List<DeviceInfo> paramList);
  public static native String oniGetExtendedError();
}