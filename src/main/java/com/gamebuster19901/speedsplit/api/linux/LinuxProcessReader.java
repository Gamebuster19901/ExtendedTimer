package com.gamebuster19901.speedsplit.api.linux;

import java.io.IOError;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;

import com.gamebuster19901.cLanguage.Unsigned;
import com.gamebuster19901.speedsplit.api.internal.ProcessReader;
import com.gamebuster19901.speedsplit.api.linux.LinuxMemoryMap.LinuxMemoryMapEntry;

public final class LinuxProcessReader extends ProcessReader<LinuxProcess> {

	private final LinuxMemoryMap memory;
	
	private final SeekableByteChannel channel;
	
	private final ByteBuffer byteBuffer = ByteBuffer.allocate(1); //single byte
	private final ByteBuffer shortBuffer = ByteBuffer.allocate(2); //short
	private final ByteBuffer intBuffer = ByteBuffer.allocate(4); //int
	private final ByteBuffer longBuffer = ByteBuffer.allocate(8); //long
	private ByteBuffer resizableBuffer = ByteBuffer.allocate(4); //int size default
	
	private final ByteBuffer[] buffers = new ByteBuffer[] {byteBuffer, shortBuffer, intBuffer, longBuffer, resizableBuffer};
	
	
	public LinuxProcessReader(LinuxProcess process) {
		super(process);
		if(process.exists()) {
			try {
				memory = new LinuxMemoryMap(process);
				channel = Files.newByteChannel(process.getMemoryPath());
			} catch (Throwable t) {
				System.err.println(process.getProcessID());
				throw new IOError(t);
			}
		}
		else {
			memory = null;
			channel = null;
		}
	}
	
	public LinuxMemoryMap getMemory() {
		return memory;
	}

	@Override
	public void setEndian(ByteOrder order) {
		for(ByteBuffer buffer : buffers) {
			buffer.order(order);
		}
	}
	
	@Override
	public ByteOrder getEndian() {
		return buffers[0].order();
	}
	

	@Override
	public boolean contains(long address) {
		return memory.contains(address);
	}
	
	private void seek(long address) throws IOException {
		System.out.println("Seeking to " + Long.toHexString(address));
		if(address < 0) {
			throw new IOException("Cannot seek to memory address 0x" + Long.toHexString(address) + ": address exceeds maximum signed long");
		}
		if(!contains(address)) {
			throw new IOException("Cannot seek to memory address 0x" + Long.toHexString(address) + ": address not in the process map!");
		}
		channel.position(address);
	}

	@Override
	public byte readByte() throws IOException {
		try {
			channel.read(byteBuffer);
			byteBuffer.position(0);
			byte ret = byteBuffer.get();
			byteBuffer.clear();
			return ret;
		}
		catch(IOException e) {
			for(LinuxMemoryMapEntry entry: memory.getMemoryMapEntries()) {
				if(entry.contains(channel.position())) {
					System.err.println(process.getProcessID() + " " + Long.toHexString(entry.getStart()) + " " + Long.toHexString(entry.getEnd()));
				}
			}
			throw e;
		}
	}

	@Override
	public @Unsigned byte readUByte() throws IOException {
		return readByte();
	}

	@Override
	public short readShort() throws IOException {
		channel.read(shortBuffer);
		shortBuffer.position(0);
		short ret = shortBuffer.getShort();
		shortBuffer.clear();
		return ret;
	}

	@Override
	public @Unsigned short readUShort() throws IOException {
		return readShort();
	}

	@Override
	public int readInt() throws IOException {
		channel.read(intBuffer);
		intBuffer.position(0);
		int ret = intBuffer.getInt();
		intBuffer.clear();
		return ret;
	}

	@Override
	public @Unsigned int readUInt() throws IOException {
		return readInt();
	}

	@Override
	public long readLong() throws IOException {
		channel.read(longBuffer);
		longBuffer.position(0);
		long ret = longBuffer.getLong();
		longBuffer.clear();
		return ret;
	}

	@Override
	public @Unsigned long readULong() throws IOException {
		return readLong();
	}

	@Override
	public void read(byte[] array) throws IOException {
		resizableBuffer = ByteBuffer.allocateDirect(array.length).order(getEndian());
		resizableBuffer.position(0);
		channel.read(resizableBuffer);
		resizableBuffer.clear();
	}

	@Override
	public float readFloat() throws IOException {
		channel.read(intBuffer);
		intBuffer.position(0);
		float ret = intBuffer.getFloat();
		intBuffer.clear();
		return ret;
	}

	@Override
	public double readDouble() throws IOException {
		channel.read(longBuffer);
		longBuffer.position(0);
		double ret = longBuffer.getDouble();
		longBuffer.clear();
		return ret;
	}
	
	@Override
	public boolean readBoolean(long address) throws IOException {
		seek(address);
		return readBoolean();
	}

	@Override
	public byte readByte(long address) throws IOException {
		seek(address);
		return readByte();
	}

	@Override
	public byte readUByte(long address) throws IOException {
		seek(address);
		return readUByte();
	}

	@Override
	public short readShort(long address) throws IOException {
		seek(address);
		return readShort();
	}

	@Override
	public short readUShort(long address) throws IOException {
		seek(address);
		return readUShort();
	}

	@Override
	public int readInt(long address) throws IOException {
		seek(address);
		return readInt();
	}

	@Override
	public int readUInt(long address) throws IOException {
		seek(address);
		return readUInt();
	}

	@Override
	public long readLong(long address) throws IOException {
		seek(address);
		return readLong();
	}

	@Override
	public long readULong(long address) throws IOException {
		seek(address);
		return readULong();
	}

	@Override
	public void read(byte[] array, long address) throws IOException {
		seek(address);
		read(array);
	}

	@Override
	public float readFloat(long address) throws IOException {
		seek(address);
		return readFloat();
	}

	@Override
	public double readDouble(long address) throws IOException {
		seek(address);
		return readDouble();
	}
	
	@Deprecated
	public long search(String find, long startAddress) throws IOException {
		byte[] search = new byte[find.length()];
		if(startAddress < 1) {
			startAddress = memory.getMemoryMapEntries()[0].getStart();
		}
		if(contains(startAddress)) {
			read(search, startAddress);
			if(new String(search).equals(find)) {
				return startAddress;
			}
		}
		else {
			return -2;
		}
		return -1;
	}

	@Override
	public void close() throws Exception {
		if(channel != null) {
			channel.close();
		}
	}

}
