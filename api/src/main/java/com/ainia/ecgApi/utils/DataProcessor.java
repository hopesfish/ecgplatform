package com.ainia.ecgApi.utils;

import java.util.ArrayList;
import java.util.List;

import com.ainia.ecgApi.dto.health.DataRecord;
import com.ainia.ecgApi.dto.health.HealthInfo;

public class DataProcessor {
	private static final int BREATHING_TYPE = 0x21; // 呼吸类型，这个版本有可能被去掉，但解析代码要保留
	private static final int ECG1_TYPE = 0x22; // 心电1类型
	private static final int ECG2_TYPE = 0x23; // 心电2类型
	private static final int ECG3_TYPE = 0x24; // 心电3类型
	private static final int INFO_TYPE = 0x25; // 数值 类型
	private static final int SECTION_LENGTH = 45;

	private float[] daolian_i;
	private float[] daolian_ii;
	private float[] daolian_iii;
	private float[] daolian_avr;
	private float[] daolian_avl;
	private float[] daolian_avf;
	private float[] daolian_v;
	private HealthInfo healthInfo;
	
	private byte[] oxygenData = new byte[12000];
	private int oxygenDataLength = 0;
	
	private float maxDaolian = 0.0f;

	private boolean snerror = false;

	// Map<Integer, DataRecord> _records = new HashMap<Integer, DataRecord>();
	List<DataRecord> _records = new ArrayList<DataRecord>();
	DataRecord _curRecord = null;
	int _sn = -1;

	/**
	 * Processing data
	 * 
	 * @param data
	 *            data in one package
	 * @param len
	 *            data length
	 * @throws Exception
	 */
	public void process(byte[] data, int len) throws DataException {
		splitData(data, len);
		// mergeECGData();

		generate_daolian_data();
	}

	private void splitData(byte[] data, int len) throws DataException {
		int i = 0;
		try {
			while (i < len) {
				if ((data[i] & 0xff) == 0xaa && (data[i + 1] & 0xff) == 0xaa
						&& (data[i + 2] & 0xff) == 0xaa) {
					int sn = data[i + 3] & 0xff;
					int type = data[i + 4];
					if (type != INFO_TYPE) {
						if (sn != _sn) {
							if (_sn == -1 || checkSN(sn, _sn)) {
								_sn = sn;
								_curRecord = new DataRecord();
								_curRecord.serialNumber = sn;
								_records.add(_curRecord);
							} else {
								snerror = true;
								//throw new DataException("SN不连续!");
							}
						}
						i = createSectionRecord(data, i);
					} else {
						i = createHealthInfo(data, i);
					}
				} else {
//					throw new DataException("帧长度不对!");
					//hack for frame length error!!!
					i++;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
//			throw e;
			
			//hack for data length error!!!
			DataRecord dr = _records.get(_records.size() - 1);
			if (dr.ecg1 == null || dr.ecg2 == null || dr.ecg3 == null) {
				_records.remove(dr);
			}
		}
	}

	private boolean checkSN(int sn, int presn) {
		if ((presn < 255 && sn - presn == 1) || (presn == 255 && sn == 0)) {
			return true;
		} else {
			return false;
		}
	}

	// private void mergeECGData() {
	// int allECGDataLength = 10 * 3 * _records.size();
	// ecg = new float[allECGDataLength];
	//
	// for (Map.Entry<Integer, DataRecord> entry : _records.entrySet()) {
	// DataRecord record = entry.getValue();
	// if (record.breathing != null && record.ecg1 != null // 应该改为ecg1，ecg2，ecg3
	// // 都不能为NULL，新版本去掉呼吸帧
	// && record.ecg2 != null && record.ecg3 != null) {
	// for (int i = 0; i < record.ecg1.length; i += 4) { //
	// 这部分不对：应该是ecg1，ecg2，ecg3
	// // 要分别存，分别画图，不能叠加在ecg一个数组中。
	// ecg[ecgLength++] = bytes4ToFloat(record.ecg1, i);
	// }
	// for (int i = 0; i < record.ecg2.length; i += 4) {
	// ecg[ecgLength++] = bytes4ToFloat(record.ecg2, i);
	// }
	// for (int i = 0; i < record.ecg3.length; i += 4) {
	// ecg[ecgLength++] = bytes4ToFloat(record.ecg3, i);
	// }
	// System.out.printf("%f\n", ecg[ecgLength - 1]);
	// }
	// }
	// }

	private void generate_daolian_data() {
		int length = 0;
		int dataLength = 10 * _records.size();
		daolian_i = new float[dataLength];
		daolian_ii = new float[dataLength];
		daolian_iii = new float[dataLength];
		daolian_avr = new float[dataLength];
		daolian_avl = new float[dataLength];
		daolian_avf = new float[dataLength];
		daolian_v = new float[dataLength];

		for (DataRecord record : _records) {
			if (record.ecg1 != null && record.ecg2 != null
					&& record.ecg3 != null) {
				for (int i = 0; i < 40; i += 4) {
					float ecg1 = bytes4ToFloat(record.ecg1, i);
					float ecg2 = bytes4ToFloat(record.ecg2, i);
					float ecg3 = bytes4ToFloat(record.ecg3, i);

					// if (ecg1 < -50) {
					// System.out.printf("record %x ecg1 %d = %f\n",
					// record.serialNumber, i, ecg1);
					// printBytes(record.ecg1);
					// }

					daolian_i[length] = ecg1;
					daolian_ii[length] = ecg2;
					daolian_iii[length] = ecg1 + ecg2;
					daolian_avr[length] = (ecg1 + ecg2) / (-2);
					daolian_avl[length] = ecg1 - ecg2 / 2;
					daolian_avf[length] = ecg2 - ecg1 / 2;
					daolian_v[length] = ecg3;
					
					float max_i_ii = Math.max(Math.abs(ecg1), Math.abs(ecg2));
					float max = Math.max(Math.abs(max_i_ii), Math.abs(ecg1+ecg2));
					if (max > maxDaolian) {
						maxDaolian = max;
					}
					
					length++;
				}
			}
		}
	}

	// private void printBytes(byte[] bytes) {
	// for (byte b : bytes) {
	// System.out.printf("0x%x ", b);
	// }
	// System.out.println();
	// }

	private int createSectionRecord(byte[] data, int start) {
		int type = data[start + 4];
		int sectionLen = 0;
		switch (type & 0xff) {
		case BREATHING_TYPE:
		case ECG1_TYPE:
		case ECG2_TYPE:
		case ECG3_TYPE:
			sectionLen = 40;
			break;
		}

		byte[] section = new byte[sectionLen];
		copyBytes(section, data, start + 5, sectionLen);
		DataRecord dr = _curRecord;

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
		}

		return start + 5 + sectionLen;
	}

	private int createHealthInfo(byte[] data, int start) {		
		HealthInfo hi = new HealthInfo();
		this.healthInfo = hi;
		
		int i = start + 5;
		hi.temperature = data[i] + data[i+1]/100f;
		hi.pulserate = data[i+2];
		hi.oxygen = data[i+3];
		//hi.oxygenChart = new byte[24];
		
		copyOxygenData(data, i+4, 24);

		hi.heartrate = data[i+28];
		hi.sbp = data[i+29];
		hi.dbp = data[i+30];
		hi.ptt = data[i+31];
		
		return start + SECTION_LENGTH;
	}
	
	private void copyOxygenData(byte[] data, int start, int length) {
		copyBytes(this.oxygenData, this.oxygenDataLength, data, start, length);
		this.oxygenDataLength += length;
	}

	private static void copyBytes(byte[] section, byte[] data, int start,
			int length) {
		for (int i = 0; i < length; i++) {
			section[i] = data[i + start];
		}
	}
	
	private static void copyBytes(byte[] dest, int destStart, byte[] orig, int origStart, int length) {
		while(length > 0) {
			dest[destStart++] = orig[origStart++];
			length --;
		}
	}

	/*
	 * private static float[] byteArray2FloatArray(byte[] bytes) { int len =
	 * bytes.length / 4; float[] floats = new float[len]; for (int i = 0; i <
	 * len; i++) { floats[i] = bytes4ToFloat(bytes, i * 4); }
	 * 
	 * return floats; }
	 */

	public static float bytes4ToFloat(byte[] b, int index) { // 16进制浮点值转换，此处代码可能转的有问题。
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

	public float[] getDaolian_i() {
		return daolian_i;
	}

	public float[] getDaolian_ii() {
		return daolian_ii;
	}

	public float[] getDaolian_iii() {
		return daolian_iii;
	}

	public float[] getDaolian_avr() {
		return daolian_avr;
	}

	public float[] getDaolian_avl() {
		return daolian_avl;
	}

	public float[] getDaolian_avf() {
		return daolian_avf;
	}

	public float[] getDaolian_v() {
		return daolian_v;
	}
	
	public HealthInfo getHealthInfo() {
		return this.healthInfo;
	}
	
	public float getMaxDaolian() {
		return this.maxDaolian;
	}
	
	public byte[] getOxygenData() {
		return this.oxygenData;
	}
	
	public int getOxygenDataLen() {
		return this.oxygenDataLength;
	}

	public boolean isSnerror() {
		return snerror;
	}

}
