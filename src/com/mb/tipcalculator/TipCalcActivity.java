package com.mb.tipcalculator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalcActivity extends Activity {
	
	EditText etAmount;
	EditText etTip;
	EditText etPartySize;
	
	TextView tvCalculatedTipValue;
	TextView tvTotalBillValue;
	TextView tvPerPersonSummaryValue;
	
	Button btnSaveTip;
	
	private static final String VTIP_FILENAME = "tip.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calc);
		
		etAmount = (EditText) findViewById(R.id.etAmount);
		etTip = (EditText) findViewById(R.id.etTip);
		etPartySize = (EditText) findViewById(R.id.etPartySize);
		
		tvCalculatedTipValue = (TextView) findViewById(R.id.tvCalculatedTipValue);
		tvTotalBillValue = (TextView) findViewById(R.id.tvTotalBillValue);
		tvPerPersonSummaryValue = (TextView) findViewById(R.id.tvPerPersonSummaryValue);
		
		double initTipValue = initTipFromFile();
		etTip.setText(String.valueOf(initTipValue));
		
		setupTextChangeListeners();
	}
	
	private void setupTextChangeListeners() {
		etAmount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				doCalculationsAndUpdateResults();
			}
		});
		
		etTip.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				doCalculationsAndUpdateResults();
			}
		});
		
		etPartySize.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				doCalculationsAndUpdateResults();
			}
		});
	}
	
	private double initTipFromFile() {
		File vTipDir = getFilesDir();
		File vTipFile = new File(vTipDir, VTIP_FILENAME);
		
		try {
			List<String> lines = FileUtils.readLines(vTipFile);
			String tipStr = lines.get(0);
			if (tipStr != null && !tipStr.isEmpty()) {
				return Double.valueOf(tipStr);
			}
		} catch (Exception e) {
			// TODO: At least log the error
			e.printStackTrace();
			return 0.0;
		}
		return 0.0;
	}
	
	public void saveTipValueToFile(View v) {
		File vTipDir = getFilesDir();
		File vTipFile = new File(vTipDir, VTIP_FILENAME);
		
		try {
			List<String> lines = new ArrayList<String>();
			double tipValue = Double.valueOf(etTip.getText().toString());
			lines.add(String.valueOf(tipValue));
			FileUtils.writeLines(vTipFile, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void doCalculationsAndUpdateResults() {
		double totalTipAmount = 0.0;
		double totalBillAmount = 0.0;
		double perPersonAmount = 0.0;

		try {
			double amount = Double.valueOf(etAmount.getText().toString());
			double tipPercent = Double.valueOf(etTip.getText().toString());
			double personCount = Double.valueOf(etPartySize.getText().toString());
			
			totalTipAmount = (amount * tipPercent) / 100;
			totalBillAmount = amount + totalTipAmount;
			perPersonAmount = totalBillAmount / personCount;

			tvCalculatedTipValue.setText(String.valueOf(totalTipAmount));
			tvTotalBillValue.setText(String.valueOf(totalBillAmount));
			tvPerPersonSummaryValue.setText(String.valueOf(perPersonAmount));
			
		} catch (Exception e) {
			resetFinalCalculations();
		}
	}
	
	private void resetFinalCalculations() {
		tvCalculatedTipValue.setText(R.string.zero_label);
		tvTotalBillValue.setText(R.string.zero_label);
		tvPerPersonSummaryValue.setText(R.string.zero_label);
	}
	
}
