package com.destroystokyo.paper.antixray;

public class DataBitsWriter {

	private byte[] dataBits;
	private int bitsPerValue;
	private long mask;
	private int longInDataBitsIndex;
	private int bitInLongIndex;
	private long current;
	private boolean dirty;

	public void setDataBits(byte[] dataBits) {
		this.dataBits = dataBits;
	}

	public void setBitsPerValue(int bitsPerValue) {
		this.bitsPerValue = bitsPerValue;
		mask = (1 << bitsPerValue) - 1;
	}

	public void setIndex(int index) {
		this.longInDataBitsIndex = index;
		bitInLongIndex = 0;
		init();
	}

	private void init() {
		if (dataBits.length > longInDataBitsIndex + 7) {
			current = ((((long) dataBits[longInDataBitsIndex]) << 56)
					| (((long) dataBits[longInDataBitsIndex + 1] & 0xff) << 48)
					| (((long) dataBits[longInDataBitsIndex + 2] & 0xff) << 40)
					| (((long) dataBits[longInDataBitsIndex + 3] & 0xff) << 32)
					| (((long) dataBits[longInDataBitsIndex + 4] & 0xff) << 24)
					| (((long) dataBits[longInDataBitsIndex + 5] & 0xff) << 16)
					| (((long) dataBits[longInDataBitsIndex + 6] & 0xff) << 8)
					| (((long) dataBits[longInDataBitsIndex + 7] & 0xff)));
		}

		dirty = false;
	}

	public void finish() {
		if (dirty && dataBits.length > longInDataBitsIndex + 7) {
			dataBits[longInDataBitsIndex] = (byte) (current >> 56 & 0xff);
			dataBits[longInDataBitsIndex + 1] = (byte) (current >> 48 & 0xff);
			dataBits[longInDataBitsIndex + 2] = (byte) (current >> 40 & 0xff);
			dataBits[longInDataBitsIndex + 3] = (byte) (current >> 32 & 0xff);
			dataBits[longInDataBitsIndex + 4] = (byte) (current >> 24 & 0xff);
			dataBits[longInDataBitsIndex + 5] = (byte) (current >> 16 & 0xff);
			dataBits[longInDataBitsIndex + 6] = (byte) (current >> 8 & 0xff);
			dataBits[longInDataBitsIndex + 7] = (byte) (current & 0xff);
		}
	}

	public void write(int value) {
		current = current & ~(mask << bitInLongIndex) | (value & mask) << bitInLongIndex;
		dirty = true;
		bitInLongIndex += bitsPerValue;

		if (bitInLongIndex > 63) {
			finish();
			bitInLongIndex -= 64;
			longInDataBitsIndex += 8;
			init();

			if (bitInLongIndex > 0) {
				current = current & ~(mask >>> bitsPerValue - bitInLongIndex) | (value & mask) >>> bitsPerValue - bitInLongIndex;
				dirty = true;
			}
		}
	}

	public void skip() {
		bitInLongIndex += bitsPerValue;

		if (bitInLongIndex > 63) {
			finish();
			bitInLongIndex -= 64;
			longInDataBitsIndex += 8;
			init();
		}
	}
}
