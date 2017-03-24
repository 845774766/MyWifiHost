package com.liang.mywifihost.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.mywifihost.R;

import static android.R.attr.name;
import static android.R.attr.visible;


public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private EditText editText;
		private TextView textView;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private LayoutInflater inflater;
		private View layout;

		public Builder(Context context) {
			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			layout = inflater.inflate(R.layout.dialog_normal_layout, null);
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 *
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 *
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder showEditTextIp(int visible){
			((EditText)layout.findViewById(R.id.dialog_edit_ip)).setVisibility(visible);
			((CheckBox)layout.findViewById(R.id.dialog_checkbox)).setVisibility(visible);
			return this;
		}

		public Editable getEditTextIp(){
			return ((EditText)layout.findViewById(R.id.dialog_edit_ip)).getText();
		}

		public void setEditTextIp(String string_name){
			((EditText)layout.findViewById(R.id.dialog_edit_ip)).setText(string_name);
		}

		public Builder showEditTextName(int visible){
			((EditText)layout.findViewById(R.id.dialog_edit_name)).setVisibility(visible);
			return this;
		}

		public Editable getEditTextName(){
			return ((EditText)layout.findViewById(R.id.dialog_edit_name)).getText();
		}

		public void setEditTextName(String string_name){
			((EditText)layout.findViewById(R.id.dialog_edit_name)).setText(string_name);
		}

		public Builder showEditTextClass(int visible){
			((EditText)layout.findViewById(R.id.dialog_edit_class)).setVisibility(visible);
			return this;
		}

		public Editable getEditTextClass(){
			return ((EditText)layout.findViewById(R.id.dialog_edit_class)).getText();
		}

		public void setEditTextClass(String string_class){
			((EditText)layout.findViewById(R.id.dialog_edit_class)).setText(string_class);
		}


		public Builder showEditTextNumber(int visible){
			((EditText)layout.findViewById(R.id.dialog_edit_number)).setVisibility(visible);
			return this;
		}

		public Editable getEditTextNumber(){
			return ((EditText)layout.findViewById(R.id.dialog_edit_number)).getText();
		}

		public void setEditTextNumber(String string_number){
			((EditText)layout.findViewById(R.id.dialog_edit_number)).setText(string_number);
		}


		public Builder showTextView(int visible){
			((TextView)layout.findViewById(R.id.message)).setVisibility(visible);
			return this;
		}

		public CheckBox getCheckBoxBmobIsChecked(){
			Log.i("haha","dialog checkbox 执行");
			return ((CheckBox)layout.findViewById(R.id.dialog_checkbox));
		}

		/**
		 * Set the positive button resource and it's listener
		 *
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
