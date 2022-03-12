package com.example.fallball.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fallball.R;

public class TextDialog extends Dialog {

	TextView titleView;
	TextView textView;
	Button button;

	String title;
	String text;
	String buttonText;

	public TextDialog(@NonNull Context context) {
		super(context);
	}

	public TextDialog(@NonNull Context context, Options options) {
		this(context);

		title = options.title;
		text = options.text;
		buttonText = options.buttonText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_dialog);

		titleView = findViewById(R.id.title);
		textView = findViewById(R.id.text);
		button = findViewById(R.id.button);
		titleView.setText(title);
		textView.setText(text);
		if (buttonText != null) button.setText(buttonText);

		button.setOnClickListener(view -> cancel());
	}

	@Override
	public void show() {
		super.show();

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (titleView != null) titleView.setText(title);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		if (titleView != null) textView.setText(text);
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
		if (titleView != null) button.setText(buttonText);
	}

	public static class Options {

		String title;
		String text;
		String buttonText;

		public Options() {
		}

		public String getTitle() {
			return title;
		}

		public Options setTitle(String title) {
			this.title = title;
			return this;
		}

		public String getText() {
			return text;
		}

		public Options setText(String text) {
			this.text = text;
			return this;
		}

		public String getButtonText() {
			return buttonText;
		}

		public Options setButtonText(String buttonText) {
			this.buttonText = buttonText;
			return this;
		}
	}
}
