package com.ainia.ecgApi.dto.health;

import java.util.Arrays;

public class DataRecord {
	public int serialNumber; // serial number of record
	public byte[] breathing; // 40 bytes breathing data
	public byte[] ecg1; // ecg1
	public byte[] ecg2; // ecg2
	public byte[] ecg3; // ecg3
	public byte[] others; // other data
	@Override
	public String toString() {
		return "DataRecord [serialNumber=" + serialNumber + ", breathing="
				+ Arrays.toString(breathing) + ", ecg1="
				+ Arrays.toString(ecg1) + ", ecg2=" + Arrays.toString(ecg2)
				+ ", ecg3=" + Arrays.toString(ecg3) + ", others="
				+ Arrays.toString(others) + "]";
	}
	
	
}
