package com.ainia.ecgApi.service.health;

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


	@Override
	public String toString() {
		return "ProcessData [floatArrayList_ecg_1=" + floatArrayList_ecg_1
				+ ", floatArrayList_ecg_2=" + floatArrayList_ecg_2
				+ ", floatArrayList_ecg_3=" + floatArrayList_ecg_3 + "]";
	}
	

}
