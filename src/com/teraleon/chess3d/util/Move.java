package com.teraleon.chess3d.util;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Move {

	private final Coord offset;

	private final BiPredicate<Context, Move> filter;
	private final BiConsumer<Context, Move> action;

	public Move(Coord off, BiPredicate<Context, Move> filt, BiConsumer<Context, Move> act) {
		this.offset = off;
		this.filter = filt;
		this.action = act;
	}

	public Coord getOffset() {
		return offset;
	}

	public BiPredicate<Context, Move> getFilter() {
		return filter;
	}

	public BiConsumer<Context, Move> getAction() {
		return action;
	}

}
