package com.gamebuster19901.speedsplit.api.internal;

import com.gamebuster19901.cLanguage.Signed;
import com.gamebuster19901.cLanguage.Unsigned;
import com.gamebuster19901.speedsplit.api.linux.LinuxMemoryMap;
import com.gamebuster19901.speedsplit.api.linux.LinuxMemoryMap.LinuxMemoryMapEntry;

public sealed abstract class MemoryMap<M extends MemoryMap.MemoryMapEntry> permits LinuxMemoryMap {

	public abstract boolean contains(long address);
	
	public abstract M[] getMemoryMapEntries();
	
	public abstract M getMapEntry(long address);
	
	public static sealed interface MemoryMapEntry permits LinuxMemoryMapEntry {
		
		public @Signed @Unsigned long getStart();
		
		public @Signed @Unsigned long getEnd();
		
		public boolean contains(long address);
		
	}
	
}
