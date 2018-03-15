package com.teraleon.chess3d.util;

public class Coord {
	public final int x;
	public final int y;
	public final int z;

	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Coord))
			return false;

		Coord that = (Coord) o;
		return (this.x == that.x && this.y == that.y && this.z == that.z);
	}
}