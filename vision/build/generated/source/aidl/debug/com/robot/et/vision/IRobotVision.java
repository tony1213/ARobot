/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\git\\ARobot\\vision\\src\\main\\aidl\\com\\robot\\et\\vision\\IRobotVision.aidl
 */
package com.robot.et.vision;
// Declare any non-default types here with import statements

public interface IRobotVision extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.robot.et.vision.IRobotVision
{
private static final java.lang.String DESCRIPTOR = "com.robot.et.vision.IRobotVision";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.robot.et.vision.IRobotVision interface,
 * generating a proxy if needed.
 */
public static com.robot.et.vision.IRobotVision asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.robot.et.vision.IRobotVision))) {
return ((com.robot.et.vision.IRobotVision)iin);
}
return new com.robot.et.vision.IRobotVision.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_visionInit:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.visionInit();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_visionUninit:
{
data.enforceInterface(DESCRIPTOR);
this.visionUninit();
reply.writeNoException();
return true;
}
case TRANSACTION_objLearnStartLearn:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.objLearnStartLearn(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_objLearnStartRecog:
{
data.enforceInterface(DESCRIPTOR);
this.objLearnStartRecog();
reply.writeNoException();
return true;
}
case TRANSACTION_testCallback:
{
data.enforceInterface(DESCRIPTOR);
this.testCallback();
reply.writeNoException();
return true;
}
case TRANSACTION_bodyDetectGetPos:
{
data.enforceInterface(DESCRIPTOR);
this.bodyDetectGetPos();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.robot.et.vision.IRobotVision
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int visionInit() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_visionInit, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 视觉反初始化

@Override public void visionUninit() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_visionUninit, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 开始学习

@Override public void objLearnStartLearn(java.lang.String str) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(str);
mRemote.transact(Stub.TRANSACTION_objLearnStartLearn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 开始识别

@Override public void objLearnStartRecog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_objLearnStartRecog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 测试回调

@Override public void testCallback() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_testCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 人体检测

@Override public void bodyDetectGetPos() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_bodyDetectGetPos, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_visionInit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_visionUninit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_objLearnStartLearn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_objLearnStartRecog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_testCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_bodyDetectGetPos = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public int visionInit() throws android.os.RemoteException;
// 视觉反初始化

public void visionUninit() throws android.os.RemoteException;
// 开始学习

public void objLearnStartLearn(java.lang.String str) throws android.os.RemoteException;
// 开始识别

public void objLearnStartRecog() throws android.os.RemoteException;
// 测试回调

public void testCallback() throws android.os.RemoteException;
// 人体检测

public void bodyDetectGetPos() throws android.os.RemoteException;
}
