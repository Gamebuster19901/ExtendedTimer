package com.gamebuster19901.speedsplit.api.linux;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.cLanguage.Unsigned;
import com.gamebuster19901.speedsplit.api.internal.MemoryMap;
import com.gamebuster19901.speedsplit.api.internal.MemoryMap.MemoryMapEntry;

public final class LinuxMemoryMap extends MemoryMap<LinuxMemoryMap.LinuxMemoryMapEntry> {

	private static final Pattern MapEntryPattern = Pattern.compile("(?<start>[0-9a-f]+)-(?<end>[0-9a-f]+) (?<perms>.{4}) (?<offset>[0-9a-f]+) (?<devMaj>[0-9a-f]+):(?<devMin>[0-9a-f]+) (?<node>[0-9]+) +(?<path>.*)");
	
	private LinkedHashSet<LinuxMemoryMapEntry> entries = new LinkedHashSet<>();
	
	public LinuxMemoryMap(LinuxProcess process) throws IOException {
		List<String> maps = Files.readAllLines(process.getMemoryMapPath());
		for(String entry : maps) {
			Matcher matcher = MapEntryPattern.matcher(entry);
			if(matcher.matches()) {
				entries.add(new LinuxMemoryMapEntry(matcher));
			}
		}
	}
	
	public boolean contains(long address) {
		for(LinuxMemoryMapEntry entry : entries) {
			if(entry.contains(address)) {
				return true;
			}
		}
		return false;
	}
	
	public LinuxMemoryMapEntry[] getMemoryMapEntries() {
		return entries.toArray(new LinuxMemoryMapEntry[]{});
	}
	
	public LinuxMemoryMapEntry getMapEntry(long address) {
		for(LinuxMemoryMapEntry entry : entries) {
			if(entry.contains(address)) {
				return entry;
			}
		}
		return null;
	}
	
	public static final class LinuxMemoryMapEntry implements MemoryMapEntry {
		
		private @Unsigned long regionStart;
		private @Unsigned long regionEnd;
		private String perms;
		private long offset;
		private long deviceMajor;
		private long deviceMinor;
		private long inode;
		private String pathName;
		
		private LinuxMemoryMapEntry(Matcher matcher) {
			regionStart = Long.parseUnsignedLong(matcher.group("start"), 16);
			regionEnd = Long.parseUnsignedLong(matcher.group("end"), 16);
			perms = matcher.group("perms");
			offset = Long.parseLong(matcher.group("offset"), 16);
			deviceMajor = Long.parseLong(matcher.group("devMaj"), 16);
			deviceMinor = Long.parseLong(matcher.group("devMin"), 16);
			inode = Long.parseLong(matcher.group("node"), 10);
			pathName = matcher.group("path");
		}
		
		public @Unsigned long getStart() {
			return regionStart;
		}
		
		public @Unsigned long getEnd() {
			return regionEnd;
		}
		
		public boolean contains(long address) {
			if(address < 0) {
				return false;
			}
			return address >= regionStart && address < regionEnd;
		}
		
		public String getPerms() {
			return perms;
		}
		
		public long offset() {
			return offset;
		}
		
		public long getDeviceMajor() {
			return deviceMajor;
		}
		
		public long getDeviceMinor() {
			return deviceMinor;
		}
		
		public long getInode() {
			return inode;
		}
		
		public String getPath() {
			return pathName;
		}
	}
	
}
