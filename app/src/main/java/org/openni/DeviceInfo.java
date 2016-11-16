package org.openni;

public class DeviceInfo {
  private final String mName;
  private final String mUri;
  private final int mUsbProductId;
  private final int mUsbVendorId;
  private final String mVendor;

  public DeviceInfo(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2) {
    this.mUri = paramString1;
    this.mVendor = paramString2;
    this.mName = paramString3;
    this.mUsbVendorId = paramInt1;
    this.mUsbProductId = paramInt2;
  }

  public final String getName()
  {
    return this.mName;
  }

  public final String getUri()
  {
    return this.mUri;
  }

  public int getUsbProductId()
  {
    return this.mUsbProductId;
  }

  public int getUsbVendorId()
  {
    return this.mUsbVendorId;
  }

  public final String getVendor()
  {
    return this.mVendor;
  }
}