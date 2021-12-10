package com.taco.suit_lady.util.timing;

public interface Timeable {
	
	<T extends Timeable> T start();
	<T extends Timeable> T reset();
	<T extends Timeable> T stop();

	long getElapsedTime();
//	double getElapsedTime(TimeUnit timeUnit);

	long getStartTime();
//	double getStartTime(TimeUnit timeUnit);
}


/*
 * TODO LIST:
 * [S] Finish implementation.
 * [S] Make sure functionality of all action methods (start, stop, reset, etc) are consistent across all implementations of Timeable.
 */