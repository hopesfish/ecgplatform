package com.ainia.ecgApi.dto.health;

import java.io.Serializable;
import java.util.List;

public class ProcessData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6404969545195542815L;
	public List<Float> floatArrayList_ecg_1;
	public List<Float> floatArrayList_ecg_2;
	public List<Float> floatArrayList_ecg_3;
	
	public ProcessData(List<Float> floatArrayList_ecg_1,
			List<Float> floatArrayList_ecg_2, List<Float> floatArrayList_ecg_3) {
		super();
		this.floatArrayList_ecg_1 = floatArrayList_ecg_1;
		this.floatArrayList_ecg_2 = floatArrayList_ecg_2;
		this.floatArrayList_ecg_3 = floatArrayList_ecg_3;
	}
	
	public Float[] getEcg1() {
		if (this.floatArrayList_ecg_1 == null) {
			return null;
		}
		Float[] datas = new Float[this.floatArrayList_ecg_1.size()];
		int i = 0;
		for (Float f : this.floatArrayList_ecg_1) {
			datas[i++] = f;
		}
		return datas;
	}
	
	public Float[] getEcg2() {
		if (this.floatArrayList_ecg_2 == null) {
			return null;
		}
		Float[] datas = new Float[this.floatArrayList_ecg_2.size()];
		int i = 0;
		for (Float f : this.floatArrayList_ecg_2) {
			datas[i++] = f;
		}
		return datas;
	}
	
	public Float[] getEcg3() {
		if (this.floatArrayList_ecg_3 == null) {
			return null;
		}
		Float[] datas = new Float[this.floatArrayList_ecg_3.size()];
		int i = 0;
		for (Float f : this.floatArrayList_ecg_3) {
			datas[i++] = f;
		}
		return datas;
	}


	@Override
	public String toString() {
		return "ProcessData [floatArrayList_ecg_1=" + floatArrayList_ecg_1
				+ ", floatArrayList_ecg_2=" + floatArrayList_ecg_2
				+ ", floatArrayList_ecg_3=" + floatArrayList_ecg_3 + "]";
	}
	

}
