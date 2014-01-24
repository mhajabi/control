package com.example.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private ImageView[] imgs;
	private final int[] signal;
	private final String[] address;
	private final String[] security;
	private final char c;
	public CustomArrayAdapter(Context context, int layout, String[] values,
			String[] Address) {
		super(context, layout, values);
		this.context = context;
		this.values = values;
		this.signal = null;
		this.security =null;
		this.address = Address;
		imgs = new ImageView[values.length];
		this.c = 'b';
		

	}
	public CustomArrayAdapter(Context context, int layout, String[] values,
			int[] Signal,String[] Security) {
		super(context, layout, values);
		this.context = context;
		this.values = values;
		this.signal = Signal;
		this.security =Security;
		this.address = null;
		this.c = 'w';
		imgs = new ImageView[values.length];
		for (int i = 0; i < signal.length; i++) {
			signal[i] = signal[i] * -1;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.element_layout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.tvText);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.ivImage);
		textView.setText(values[position]);
		imgs[position] = imageView;

		// Change icon based on signal
		this.setImage(imgs, signal, position,c);
		//
		return rowView;
	}

	void setImage(ImageView[] imgArray, int[] signal, int lenght,char c) {
		if(c == 'w'){
		for (int i = 0; i <= lenght; i++) {
			if(security[i].contains("WPA")|| security[i].contains("WEP")||security[i].contains("PSK")
					||security[i].contains("EAP")){
				//if the network is secured
				if (signal[i] <= 20)
					imgArray[i].setImageResource(R.drawable.wifi_3_secured);
				else if (signal[i] > 20 && signal[i] <= 40)
					imgArray[i].setImageResource(R.drawable.wifi_3_secured);
				else if (signal[i] > 40 && signal[i] <= 60)
					imgArray[i].setImageResource(R.drawable.wifi_2_secured);
				else if (signal[i] > 60 && signal[i] <= 80)
					imgArray[i].setImageResource(R.drawable.wifi_2_secured);
				else if (signal[i] > 80 && signal[i] <= 100)
					imgArray[i].setImageResource(R.drawable.wifi_1_secured);
			
					}else{
						//if the network is not secured
						if (signal[i] <= 20)
							imgArray[i].setImageResource(R.drawable.wifi_3_unsecured);
						else if (signal[i] > 20 && signal[i] <= 40)
							imgArray[i].setImageResource(R.drawable.wifi_3_unsecured);
						else if (signal[i] > 40 && signal[i] <= 60)
							imgArray[i].setImageResource(R.drawable.wifi_2_unsecured);
						else if (signal[i] > 60 && signal[i] <= 80)
							imgArray[i].setImageResource(R.drawable.wifi_2_unsecured);
						else if (signal[i] > 80 && signal[i] <= 100)
							imgArray[i].setImageResource(R.drawable.wifi_1_unsecured);
					}
			

		}
	}
		else if(c == 'b'){
			
		}
	}
}