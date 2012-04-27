package com.motorola.android.extdispservice;

interface IExtDispService {
  boolean isSupported(in int paramInt);
  boolean isDetectionEnabled(in int paramInt);
  boolean isConnected(in int paramInt);
  boolean isDisplayEnabled(in int paramInt);
  boolean enableDetection(in int paramInt, in IBinder paramIBinder);
  void disableDetection(in int paramInt);
  boolean enableDisplay(in int paramInt, in IBinder paramIBinder, in String paramString);
  void disableDisplay(in int paramInt);
  boolean enableStandardOverride(in int paramInt1, in IBinder paramIBinder, in int paramInt2);
  void disableStandardOverride(in int paramInt);
  boolean setRouting(in String paramString1, in String paramString2);
  boolean setRoutingEx(in String paramString1, in String paramString2, in IBinder paramIBinder);
  boolean setGfxAlign(in int paramInt, in String paramString);
  int[] getResolution(in int paramInt);
  int[] getResolutions(in int paramInt, boolean paramBoolean);
  int getRecommendedResolution(in int paramInt);
  byte[] getEdid(in int paramInt);
  boolean enableSolInFront(in int paramInt, in IBinder paramIBinder);
  void disableSolInFront(in int paramInt);
  boolean getSolInFront();
}
