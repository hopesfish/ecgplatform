package com.ainia.ecgApi.core.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.TestCase;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class PdfTest {

	public void testTable () throws DocumentException, BadElementException, MalformedURLException, IOException {
		Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
		
		PdfWriter writer = PdfWriter.getInstance(doc, 
								new FileOutputStream(new File("c:/test.pdf")));
		doc.open();
		PdfContentByte canvas = writer.getDirectContent();

		
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", true);
		//BaseFont bfChinese = BaseFont.createFont(PdfTest.class.getResource("/") + "/fonts/simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 
	        Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);  
		

	        
		Paragraph title = new Paragraph("体检病例报告",  new Font(bfChinese, 30, Font.BOLD));
		title.setAlignment(1);
		Chapter chapter = new Chapter(title , 1);
		chapter.setNumberDepth(0);
		//Section section = chapter.addSection(title);
		
		PdfPTable t = new PdfPTable(2);
		t.setTotalWidth(180);
		t.setSpacingBefore(80);
		//t.setSpacingAfter(25);
		t.setHorizontalAlignment(1);
		
		PdfPCell name = new PdfPCell(new Phrase("姓名 " , fontChinese));
		name.setBorderWidth(0);
		t.addCell(name);
		PdfPCell nameValue = new PdfPCell(new Phrase("测试" , fontChinese));
		nameValue.setBorderWidth(0);
		nameValue.setHorizontalAlignment(1);
		nameValue.setBorderWidthBottom(1);
		t.addCell(nameValue);
		
		PdfPCell sex = new PdfPCell(new Phrase("性别 " , fontChinese));
		sex.setBorderWidth(0);
		t.addCell(sex);
		PdfPCell sexValue = new PdfPCell(new Phrase("男" , fontChinese));
		sexValue.setBorderWidth(0);
		sexValue.setHorizontalAlignment(1);
		sexValue.setBorderWidthBottom(1);
		t.addCell(sexValue);
		
		PdfPCell age = new PdfPCell(new Phrase("年龄 " , fontChinese));
		age.setBorderWidth(0);
		t.addCell(age);
		PdfPCell ageValue = new PdfPCell(new Phrase("23"));
		ageValue.setBorderWidth(0);
		ageValue.setHorizontalAlignment(1);
		ageValue.setBorderWidthBottom(1);
		t.addCell(ageValue);
		
		PdfPCell date = new PdfPCell(new Phrase("监测日期  " , fontChinese));
		date.setBorderWidth(0);
		t.addCell(date);
		PdfPCell dateValue = new PdfPCell(new Phrase("2013-06-31"));
		dateValue.setBorderWidth(0);
		dateValue.setHorizontalAlignment(1);
		dateValue.setBorderWidthBottom(1);
		t.addCell(dateValue);
		

		doc.add(chapter);
		
		
		  // complete the table
	    t.completeRow();
	    // write the table to an absolute position
	    t.writeSelectedRows(0, -1, 300, t.getTotalHeight() + 60, canvas);
	    
	    
	    //----------------- page 2
		Paragraph userInfo = new Paragraph("个人信息",  new Font(bfChinese, 30, Font.BOLD));
		Chapter chapter2 = new Chapter(userInfo , 1);
		chapter2.setNumberDepth(0);
		
		PdfPTable infoTable = new PdfPTable(4);
		infoTable.setSpacingBefore(20);
		infoTable.setHorizontalAlignment(1);
		infoTable.addCell(name);
		infoTable.addCell(nameValue);
		

		PdfPCell phone = new PdfPCell(new Phrase("电话  " , fontChinese));
		phone.setBorderWidth(0);
		infoTable.addCell(phone);
		PdfPCell phoneValue = new PdfPCell(new Phrase("13028930211"));
		phoneValue.setBorderWidth(0);
		phoneValue.setHorizontalAlignment(1);
		phoneValue.setBorderWidthBottom(1);
		infoTable.addCell(phoneValue);
		
		PdfPCell badHabits = new PdfPCell(new Phrase("不良嗜好  " , fontChinese));
		badHabits.setBorderWidth(0);
		infoTable.addCell(badHabits);
		PdfPCell badHabitsValue = new PdfPCell(new Phrase("抽烟、喝酒"));
		badHabitsValue.setColspan(3);
		badHabitsValue.setBorderWidth(0);
		badHabitsValue.setHorizontalAlignment(1);
		badHabitsValue.setBorderWidthBottom(1);
		infoTable.addCell(badHabitsValue);
		
		
		PdfPCell anamnesis = new PdfPCell(new Phrase("既往病史  " , fontChinese));
		anamnesis.setBorderWidth(0);
		infoTable.addCell(anamnesis);
		PdfPCell anamnesisValue = new PdfPCell(new Phrase("无"));
		anamnesisValue.setColspan(3);
		anamnesisValue.setBorderWidth(0);
		anamnesisValue.setHorizontalAlignment(1);
		anamnesisValue.setBorderWidthBottom(1);
		infoTable.addCell(anamnesisValue);

		doc.add(chapter2);
		doc.add(infoTable);
		doc.add(new Paragraph("报告回复 ",  new Font(bfChinese, 12, Font.BOLD)));
		
		Paragraph reply = new Paragraph("asdsadsadsad" +
				"asdasdasd" +
				"asdasd" +
				"asdsa" +
				"asd  asdasdasd" +
				"sadasdsadsadddddddddddddddddddddddddddddddddddddddddddd" +
				"asssssssssssssssssssssss",  new Font(bfChinese, 12, Font.NORMAL));
		doc.add(reply);
		
		//------------------------------------------------------------------------------page3
		Paragraph result = new Paragraph("各项检查结果如下: ",  new Font(bfChinese, 30, Font.BOLD));
		Chapter chapter3 = new Chapter(result , 1);
		chapter3.setNumberDepth(0);
		
		chapter3.add(new Paragraph("一般项目 ",  new Font(bfChinese, 18 , Font.BOLD)));
		
		PdfPTable resultTable1 = new PdfPTable(2);
		resultTable1.setSpacingBefore(20);
		resultTable1.setHorizontalAlignment(1);
		
		PdfPCell itemName = new PdfPCell(new Phrase("项目名称  " , fontChinese));
		itemName.setBorderWidth(0);
		itemName.setHorizontalAlignment(1);
		itemName.setBorderWidthBottom(1);
		resultTable1.addCell(itemName);
		PdfPCell itemValue = new PdfPCell(new Phrase("项目结果  " , fontChinese));
		itemValue.setBorderWidth(0);
		itemValue.setHorizontalAlignment(1);
		itemValue.setBorderWidthBottom(1);
		resultTable1.addCell(itemValue);
		
		PdfPCell bloodLow = new PdfPCell(new Phrase("收缩压  " , fontChinese));
		bloodLow.setBorderWidth(0);
		bloodLow.setHorizontalAlignment(1);
		resultTable1.addCell(bloodLow);
		PdfPCell bloodLowValue = new PdfPCell(new Phrase("80" , fontChinese));
		bloodLowValue.setBorderWidth(0);
		bloodLowValue.setHorizontalAlignment(1);
		resultTable1.addCell(bloodLowValue);
		
		
		PdfPCell bloodHigh = new PdfPCell(new Phrase("舒张压  " , fontChinese));
		bloodHigh.setBorderWidth(0);
		bloodHigh.setHorizontalAlignment(1);
		resultTable1.addCell(bloodHigh);
		PdfPCell bloodHighValue = new PdfPCell(new Phrase("120" , fontChinese));
		bloodHighValue.setBorderWidth(0);
		bloodHighValue.setHorizontalAlignment(1);
		resultTable1.addCell(bloodHighValue);
		
		PdfPCell bodyTemp = new PdfPCell(new Phrase("体温  " , fontChinese));
		bodyTemp.setBorderWidth(0);
		bodyTemp.setHorizontalAlignment(1);
		resultTable1.addCell(bodyTemp);
		PdfPCell bodyTempValue = new PdfPCell(new Phrase("37.5" , fontChinese));
		bodyTempValue.setBorderWidth(0);
		bodyTempValue.setHorizontalAlignment(1);
		resultTable1.addCell(bodyTempValue);
		
		chapter3.add(resultTable1);
		
		chapter3.add(new Paragraph("内科 ",  new Font(bfChinese, 18 , Font.BOLD)));
		
		PdfPTable resultTable2 = new PdfPTable(2);
		resultTable2.setSpacingBefore(20);
		resultTable2.setHorizontalAlignment(1);
		
		resultTable2.addCell(itemName);
		resultTable2.addCell(itemValue);
		
		PdfPCell heartRhythm = new PdfPCell(new Phrase("心率  " , fontChinese));
		heartRhythm.setBorderWidth(0);
		heartRhythm.setHorizontalAlignment(1);
		resultTable2.addCell(heartRhythm);
		PdfPCell heartRhythmValue = new PdfPCell(new Phrase("80" , fontChinese));
		heartRhythmValue.setBorderWidth(0);
		heartRhythmValue.setHorizontalAlignment(1);
		resultTable2.addCell(bodyTempValue);
		
		PdfPCell bloodOxygen = new PdfPCell(new Phrase("血氧  " , fontChinese));
		bloodOxygen.setBorderWidth(0);
		bloodOxygen.setHorizontalAlignment(1);
		resultTable2.addCell(bloodOxygen);
		PdfPCell bloodOxygenValue = new PdfPCell(new Phrase("80" , fontChinese));
		bloodOxygenValue.setBorderWidth(0);
		bloodOxygenValue.setHorizontalAlignment(1);
		resultTable2.addCell(bloodOxygenValue);
		chapter3.add(resultTable2);
		
		doc.add(chapter3);

		
//		section.add(t);
//		
//		Image image = Image.getInstance("d:/ecg7.jpg");
//		image.scalePercent(80, 10);
//		
//		section.add(image);
		
		
		doc.close();
		
		
		
	}
}
