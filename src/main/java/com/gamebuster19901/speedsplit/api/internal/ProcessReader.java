package com.gamebuster19901.speedsplit.api.internal;

import java.io.IOException;
import java.nio.ByteOrder;

import com.gamebuster19901.cLanguage.Signed;
import com.gamebuster19901.cLanguage.Unsigned;
import com.gamebuster19901.speedsplit.api.linux.LinuxProcessReader;

public abstract sealed class ProcessReader<T extends Process> implements AutoCloseable permits LinuxProcessReader {

	protected final T process;
	
	public ProcessReader(T process) {
		this.process = process;
	}
	
	public final T getProcess() {
		return process;
	}
	
	public abstract void setEndian(ByteOrder order);
	
	public abstract ByteOrder getEndian();
	
	public boolean readBoolean() throws IOException {
		return readByte() >> 3 == 1;
	}
	
	public abstract MemoryMap<?> getMemory();
	
	public abstract boolean contains(long address);
	
	public abstract @Signed byte readByte() throws IOException;
	
	public abstract @Unsigned byte readUByte() throws IOException;
	
	public abstract @Signed short readShort() throws IOException;
	
	public abstract @Unsigned short readUShort() throws IOException;
	
	public abstract @Signed int readInt() throws IOException;
	
	public abstract @Unsigned int readUInt() throws IOException;
	
	public abstract @Signed long readLong() throws IOException;
	
	public abstract @Unsigned long readULong() throws IOException;
	
	public abstract void read(byte[] array) throws IOException;
	
	public abstract float readFloat() throws IOException;
	
	public abstract double readDouble() throws IOException;
	
	public abstract boolean readBoolean(long address) throws IOException;
	
	public abstract @Signed byte readByte(long address) throws IOException;
	
	public abstract @Unsigned byte readUByte(long address) throws IOException;
	
	public abstract @Signed short readShort(long address) throws IOException;
	
	public abstract @Unsigned short readUShort(long address) throws IOException;
	
	public abstract @Signed int readInt(long address) throws IOException;
	
	public abstract @Unsigned int readUInt(long address) throws IOException;
	
	public abstract @Signed long readLong(long address) throws IOException;
	
	public abstract @Unsigned long readULong(long address) throws IOException;
	
	public abstract void read(byte[] array, long address) throws IOException;
	
	public abstract float readFloat(long address) throws IOException;
	
	public abstract double readDouble(long address) throws IOException;
	
}
