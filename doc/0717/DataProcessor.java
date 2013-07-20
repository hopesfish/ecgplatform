package com.lcworld.healthbutler.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lcworld.healthbutler.monitor.bean.ProcessData;

public class DataProcessor {
	private static final int BREATHING_TYPE = 0x21;
	private static final int ECG1_TYPE = 0x22;
	private static final int ECG2_TYPE = 0x23;
	private static final int ECG3_TYPE = 0x24;
	private static final int OTHERS_TYPE = 0x25;
	private Map<Integer, DataRecord> dataRecordMap = new HashMap<Integer, DataRecord>();

	public ProcessData process(byte[] data, int len) {
		
		splitData(data, len);
		
		List<Float> floatArrayList_ecg_1 = new ArrayList<Float>();
		List<Float> floatArrayList_ecg_2 = new ArrayList<Float>();
		List<Float> floatArrayList_ecg_3 = new ArrayList<Float>();
		for (Map.Entry<Integer, DataRecord> entry : dataRecordMap.entrySet()) {
			DataRecord record = entry.getValue();
			if (record.ecg1 != null && record.ecg2 != null && record.ecg3 != null) {
				System.out.println("正常，都有数据：" + record.toString());
				float[] floats_ecg1 = byteArray2FloatArray(record.ecg1);
				for (int i = 0; i < floats_ecg1.length; i++) {
					floatArrayList_ecg_1.add(floats_ecg1[i]);
				}
				float[] floats_ecg2 = byteArray2FloatArray(record.ecg2);
				for (int i = 0; i < floats_ecg2.length; i++) {
					floatArrayList_ecg_2.add(floats_ecg2[i]);
				}
				float[] floats_ecg3 = byteArray2FloatArray(record.ecg3);
				for (int i = 0; i < floats_ecg3.length; i++) {
					floatArrayList_ecg_3.add(floats_ecg3[i]);
				}
			}else{
				System.out.println("不正常，丢失数据：" + record.toString());
			}
		}
		
		return new ProcessData(floatArrayList_ecg_1,floatArrayList_ecg_2,floatArrayList_ecg_3);
		
	}

	private void splitData(byte[] data, int len) {
		int i = 0;
		while (i < len - 5) {
			if ((data[i] & 0xff) == 0xaa && (data[i + 1] & 0xff) == 0xaa && (data[i + 2] & 0xff) == 0xaa) {
				i = createSectionRecord(data, i);
			}
			i++;
		}
	}

	private int createSectionRecord(byte[] data, int start) {
		int serialNumber = data[start + 3];
		int type = data[start + 4];
		int sectionLen = 0;
		switch (type & 0xff) {
		case BREATHING_TYPE:
		case ECG1_TYPE:
		case ECG2_TYPE:
		case ECG3_TYPE:
			sectionLen = 40;
			break;
		case OTHERS_TYPE:
			sectionLen = 40;
			break;
		}

		byte[] section = new byte[sectionLen];
		copyBytes(section, data, start + 5, sectionLen);
		DataRecord dr = dataRecordMap.get(serialNumber);
		if (dr == null) {
			dr = new DataRecord();
			dr.serialNumber = serialNumber;
			dataRecordMap.put(serialNumber, dr);
		}

		switch (type & 0xff) {
		case BREATHING_TYPE:
			dr.breathing = section;
			break;
		case ECG1_TYPE:
			dr.ecg1 = section;
			break;
		case ECG2_TYPE:
			dr.ecg2 = section;
			break;
		case ECG3_TYPE:
			dr.ecg3 = section;
			break;
		case OTHERS_TYPE:
			dr.others = section;
			break;
		}

		return start + 5 + sectionLen - 1;
	}

	private void copyBytes(byte[] section, byte[] data, int start,int length) {	
		
		System.out.println("length:"+ length);
		System.out.println("section[].length:"+ section.length);
		System.out.println("data[].length:"+ data.length);
		for (int i = 0; i < length; i++) {
			if((i + start) < data.length){
				section[i] = data[i + start];
			}
		}
	}

	private static float[] byteArray2FloatArray(byte[] bytes) {
		int len = bytes.length / 4;
		float[] floats = new float[len];
		for (int i = 0; i < len; i++) {
			floats[i] = bytes4ToFloat(bytes, i * 4);
		}

		return floats;
	}

	private static float bytes4ToFloat(byte[] b, int index) {
		int l;
		l = b[index + 3];
		l &= 0xff;
		l |= ((long) b[index + 2] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 1] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 0] << 24);
		return Float.intBitsToFloat(l);
	}

	private void checkRecord() {
		System.out.printf("Record total count = %d\n", dataRecordMap.size());
		for (Map.Entry<Integer, DataRecord> entry : dataRecordMap.entrySet()) {
			DataRecord record = entry.getValue();
			System.out.printf("Checking record %x\n",
					record.serialNumber & 0xff);
			if (record.breathing == null) {
				System.out.printf("SEQ:%x has no breathing data\n",
						record.serialNumber & 0xff);
			} else if (record.ecg1 == null) {
				System.out.printf("SEQ:%x has no ecg1 data\n",
						record.serialNumber & 0xff);
			} else if (record.ecg2 == null) {
				System.out.printf("SEQ:%x has no ecg2 data\n",
						record.serialNumber & 0xff);
			} else if (record.ecg3 == null) {
				System.out.printf("SEQ:%x has no ecg3 data\n",
						record.serialNumber & 0xff);
			}
		}
	}

	public static void main(String[] argv) throws IOException {
		DataProcessor dp = new DataProcessor();
		FileInputStream fis = new FileInputStream("./datample2");
		byte[] data = new byte[1000000];
		int len = fis.read(data);
		dp.process(data, len);
	}
}
