import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import model.DataRecord;

/**
 * Class for processing call process() to process data.
 * 
 * @author yuccai
 * 
 */
public class DataProcessor {
	private static final int BREATHING_TYPE = 0x21; // �������ͣ�����汾�п��ܱ�ȥ��������������Ҫ����
	private static final int ECG1_TYPE = 0x22; // �ĵ�1����
	private static final int ECG2_TYPE = 0x23; // �ĵ�2����
	private static final int ECG3_TYPE = 0x24; // �ĵ�3����
	private static final int OTHERS_TYPE = 0x25; // ��ֵ ����

	private float[] daolian_i;
	private float[] daolian_ii;
	private float[] daolian_iii;
	private float[] daolian_avr;
	private float[] daolian_avl;
	private float[] daolian_avf;
	private float[] daolian_v;

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
		outputECGChart();
	}

	private void splitData(byte[] data, int len) throws DataException { // ����֡ͷ ��������������֡ͷ����0xAA 0xAA
													// 0xAA �����ֽῪͷ��
		int i = 0;
		while (i < len) {
			if ((data[i] & 0xff) == 0xaa && (data[i + 1] & 0xff) == 0xaa
					&& (data[i + 2] & 0xff) == 0xaa) {
				int sn = data[i + 3] & 0xff;
				int type = data[i + 4];
				if (type != OTHERS_TYPE) {
					if (sn != _sn) {
						if (_sn == -1 || checkSN(sn, _sn)) {
							_sn = sn;
							_curRecord = new DataRecord();
							_curRecord.serialNumber = sn;
							_records.add(_curRecord);
						} else {
							throw new DataException("SN������!");
						}
					}
					i = createSectionRecord(data, i);
				} else {
					// omit other data
					i += 44;
				}
			}
			i++;
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
	// if (record.breathing != null && record.ecg1 != null // Ӧ�ø�Ϊecg1��ecg2��ecg3
	// // ������ΪNULL���°汾ȥ������֡
	// && record.ecg2 != null && record.ecg3 != null) {
	// for (int i = 0; i < record.ecg1.length; i += 4) { //
	// �ⲿ�ֲ��ԣ�Ӧ����ecg1��ecg2��ecg3
	// // Ҫ�ֱ�棬�ֱ�ͼ�����ܵ�����ecgһ�������С�
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

//					if (ecg1 < -50) {
//						System.out.printf("record %x ecg1 %d = %f\n",
//							record.serialNumber, i, ecg1);
//						printBytes(record.ecg1);
//					}

					daolian_i[length] = ecg1;
					daolian_ii[length] = ecg2;
					daolian_iii[length] = ecg1 + ecg2;
					daolian_avr[length] = (ecg1 + ecg2) / (-2);
					daolian_avl[length] = ecg1 - ecg2 / 2;
					daolian_avf[length] = ecg2 - ecg1 / 2;
					daolian_v[length] = ecg3;
					length++;
				}
			}
		}
	}
	
//	private void printBytes(byte[] bytes) {
//		for (byte b : bytes) {
//			System.out.printf("0x%x ", b);
//		}
//		System.out.println();
//	}

	private void outputECGChart() {
		ECGChart chart = new ECGChart();
		chart.createChart(daolian_i, 0, daolian_i.length,
				"./output/daolian_i.jpg", 0.1f);
		chart.createChart(daolian_ii, 0, daolian_ii.length,
				"./output/daolian_ii.jpg", 0.1f);
		chart.createChart(daolian_iii, 0, daolian_iii.length,
				"./output/daolian_iii.jpg", 0.1f);
		chart.createChart(daolian_avr, 0, daolian_avr.length,
				"./output/daolian_avr.jpg", 0.1f);
		chart.createChart(daolian_avl, 0, daolian_avl.length,
				"./output/daolian_avl.jpg", 0.1f);
		chart.createChart(daolian_avf, 0, daolian_avf.length,
				"./output/daolian_avf.jpg", 0.1f);
		chart.createChart(daolian_v, 0, daolian_v.length,
				"./output/daolian_v.jpg", 5.0f);
	}

	private int createSectionRecord(byte[] data, int start) {
		int type = data[start + 4];
		int sectionLen = 0;
		switch (type & 0xff) { // ecg1��ecg2��ecg3��breath
								// ÿ֡ʱ45�ֽڣ�5�ֽ�֡ͷ��40�ֽ����ݣ�4�ֽڸ���ֵ����10������
		case BREATHING_TYPE:
		case ECG1_TYPE:
		case ECG2_TYPE:
		case ECG3_TYPE:
			sectionLen = 40;
			break;
		case OTHERS_TYPE: // ��ֵ֡����30�ֽڣ�5�ֽ�֡ͷ��40�ֽ�����
			sectionLen = 25;
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
		// case OTHERS_TYPE:
		// dr.others = section;
		// break;
		}

		return start + 5 + sectionLen - 1;
	}

	private static void copyBytes(byte[] section, byte[] data, int start,
			int length) {
		for (int i = 0; i < length; i++) {
			section[i] = data[i + start];
		}
	}

	/*
	 * private static float[] byteArray2FloatArray(byte[] bytes) { int len =
	 * bytes.length / 4; float[] floats = new float[len]; for (int i = 0; i <
	 * len; i++) { floats[i] = bytes4ToFloat(bytes, i * 4); }
	 * 
	 * return floats; }
	 */

	public static float bytes4ToFloat(byte[] b, int index) { // 16���Ƹ���ֵת�����˴��������ת�������⡣
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

	public static void main(String[] argv) throws DataException, IOException {
		DataProcessor dp = new DataProcessor();
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream("./data/sample2");
		byte[] data = new byte[1000000];
		int len = fis.read(data);
		dp.process(data, len);
	}
}
