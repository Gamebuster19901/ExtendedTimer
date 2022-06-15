package com.gamebuster19901.speedsplit;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class PausingTimer implements Timed {

	private RealTimer parent;
	
	public ArrayList<RealTimer> pauses = new ArrayList<RealTimer>();
	
	public PausingTimer(RealTimer parent) {
		this.parent = parent;
	}
	
	@Override
	public Instant getStart() {
		return parent.getStart();
	}

	@Override
	public Instant getEnd() {
		return parent.getEnd();
	}

	@Override
	public boolean hasStart() {
		return parent.hasStart();
	}

	@Override
	public boolean hasEnd() {
		return parent.hasEnd();
	}
	
	public RealTimer getLastTimer() {
		if(pauses.isEmpty()) {
			return null;
		}
		return pauses.get(pauses.size() - 1);
	}
	
	public void pause() {
		if(getStart() == null || getEnd() != null || getLastTimer() != null && getLastTimer().getEnd() == null) {
			throw new IllegalStateException();
		}
		RealTimer timer = new RealTimer(Instant.now().toEpochMilli());
		pauses.add(timer);
	}
	
	public void unpause() {
		getLastTimer().stop();
	}
	
	public void undo() {
		RealTimer lastTimer = getLastTimer();
		if(lastTimer == null) {
			throw new IllegalStateException();
		}
		if(lastTimer.hasEnd()) {
			lastTimer.undo();
		}
		else {
			pauses.remove(lastTimer);
		}
	}
	
	@Override
	public Duration getDuration() {
		if(hasStart()) {
			Duration duration = Timed.super.getDuration();
			for(RealTimer timer : pauses) {
				if(timer.hasEnd()) {
					duration = duration.minus(timer.getDuration());
				}
				else {
					duration = duration.minus(TimeUtils.since(timer.getStart()));
				}
			}
			return duration;
		}
		return Duration.ZERO;
	}

	@Override
	public String getFormattedText() {
		return TimeUtils.toReadableTimerDuration(getDuration(), parent.getDuration().toHours() > 0);
	}
	
}
