package com.phincode.honnywellintermecpr3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.honeywell.mobility.print.LinePrinter;
import com.honeywell.mobility.print.LinePrinterException;
import com.honeywell.mobility.print.PrintProgressEvent;
import com.honeywell.mobility.print.PrintProgressListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * This sample demonstrates printing on an Android computer using the LinePrinter
 * class. You may enter or scan a Honeywell mobile printer's MAC address and
 * click the Print button to print. The MAC Address text should have the format
 * of "nn:nn:nn:nn:nn:nn" or "nnnnnnnnnnnn" where each n is a hex digit.
 * <p>
 * You may also capture a signature to print by clicking the Sign button. It
 * will display another screen for you to sign and save the signature. After
 * you save the signature, you will see a preview of the signature graphic
 * next to the Sign button.
 * <p>
 * The printing progress will be displayed in the Progress and Status text box.
 */
public class PrintActivity extends Activity {
	private Button buttonPrint;
	private Button buttonSign;
	private TextView textMsg;
	private EditText editPrinterID;
	private EditText editPrinterAddress;
	private ImageView imgSignature;
	private String base64SignaturePng = null;
	private String jsonCmdAttribStr = null;
	private String base64LogoPng = null;
	private Spinner connectionTypes;
	Intent intent;

	ArrayList<String> commande;
	public static final int CAPTURE_SIGNATURE_ACTIVITY = 1;
	public   String DEFAULT_BT_MAC_ADDRESS = "B8:69:C2:25:4F:1B";
	private static final String DEFAULT_SERIAL_PORT = "dev/rs232_dex0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_print );

		//get intent variable
		intent=getIntent();

		textMsg = (TextView) findViewById(R.id.textMsg);

		editPrinterID = (EditText) findViewById(R.id.editPrinterID);
		// Set a default Printer ID
		editPrinterID.setText(intent.getStringExtra("deviceName"));
		DEFAULT_BT_MAC_ADDRESS=intent.getStringExtra("deviceBleutoothMacAdress");
		commande= intent.getStringArrayListExtra ("cmd");

		connectionTypes = (Spinner) findViewById(R.id.spinnerConnectionTypes);
		initConnectionTypesSpinner();

		editPrinterAddress = (EditText) findViewById(R.id.editPrinterAddress);
		imgSignature = (ImageView) findViewById(R.id.imgSignature);

		readAssetFiles();

		buttonPrint = (Button) findViewById(R.id.buttonPrint);
		buttonPrint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				// Create a PrintTask to do printing on a separate thread.
				PrintTask task = new PrintTask();

				// Executes PrintTask with the specified parameter which is passed
				// to the PrintTask.doInBackground method.
				task.execute(editPrinterID.getText().toString(), editPrinterAddress.getText().toString());
			}
		});

		buttonSign = (Button) findViewById(R.id.buttonSign);
		buttonSign.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(PrintActivity.this, CaptureSignatureActivity.class);
				startActivityForResult(intent, CAPTURE_SIGNATURE_ACTIVITY);
			}
		});



	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.action_settings) {
			finish();
		} else {
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}



	private void initConnectionTypesSpinner()
	{
		// Add choices to the connectionTypes spinner.
		String[] connectionTypeOptions = new String[]{
				getResources().getString(R.string.bluetooth_connection),
				getResources().getString(R.string.serial_connection)
		};
		ArrayAdapter<String> connectionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, connectionTypeOptions);
		connectionTypes.setAdapter(connectionAdapter);

		connectionTypes.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if(parent.getItemAtPosition(position).toString().equals(
						getResources().getString(R.string.bluetooth_connection)))
				{
					editPrinterAddress.setHint(R.string.enter_mac_address);
					if (editPrinterAddress.getText().length() == 0 ||
						editPrinterAddress.getText().toString().startsWith("dev"))
					{
						editPrinterAddress.setText(DEFAULT_BT_MAC_ADDRESS);
					}
				}
				else if(parent.getItemAtPosition(position).toString().equals(
						getResources().getString(R.string.serial_connection)))
				{
					editPrinterAddress.setHint(R.string.enter_serial_port_name);
					if (editPrinterAddress.getText().length() == 0 ||
						editPrinterAddress.getText().toString().startsWith("dev") == false)
					{
						editPrinterAddress.setText(DEFAULT_SERIAL_PORT);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	private void readAssetFiles()
	{
		InputStream input = null;
		ByteArrayOutputStream output = null;
		AssetManager assetManager = getAssets();
		String[] files = { "printer_profiles.JSON", "honeywell_logo.bmp" };
		int fileIndex = 0;
		int initialBufferSize;

		try
		{
			for (String filename : files)
			{
				input = assetManager.open(filename);
				initialBufferSize = (fileIndex == 0) ? 8000 : 2500;
				output = new ByteArrayOutputStream(initialBufferSize);

				byte[] buf = new byte[1024];
				int len;
				while ((len = input.read(buf)) > 0)
				{
					output.write(buf, 0, len);
				}
				input.close();
				input = null;

				output.flush();
				output.close();
				switch (fileIndex)
				{
				case 0:
					jsonCmdAttribStr = output.toString();
					break;
				/*case 1:
					base64LogoPng = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT);
					break;*/
				}

				fileIndex++;
				output = null;
			}
		}
		catch (Exception ex)
		{
			textMsg.append("Error reading asset file: " + files[fileIndex]);
		}
		finally
		{
			try
			{
				if (input != null)
				{
					input.close();
					input = null;
				}

				if (output != null)
				{
					output.close();
					output = null;
				}
			}
			catch (IOException e){}
		}
	}

	/**
	 * This exception is thrown by the background thread to halt printing attempts and
	 * return an error to the UI when the printer status indicates conditions that would
	 * prevent successful printing such as "lid open" or "paper out".
	 */
	public class BadPrinterStateException extends Exception
	{
		static final long serialVersionUID = 1;

		public BadPrinterStateException(String message)
		{
			super(message);
		}
	}

	/**
	 * This class demonstrates printing in a background thread and updates
	 * the UI in the UI thread.
	 */
	public class PrintTask extends AsyncTask<String, Integer, String> {
		private static final String PROGRESS_CANCEL_MSG = "Printing cancelled\n";
		private static final String PROGRESS_COMPLETE_MSG = "Printing completed\n";
		private static final String PROGRESS_ENDDOC_MSG = "End of document\n";
		private static final String PROGRESS_FINISHED_MSG = "Printer connection closed\n";
		private static final String PROGRESS_NONE_MSG = "Unknown progress message\n";
		private static final String PROGRESS_STARTDOC_MSG = "Start printing document\n";


		/**
		 * Runs on the UI thread before doInBackground(Params...).
		 */
		@Override
		protected void onPreExecute()
		{
			// Clears the Progress and Status text box.
			textMsg.setText("");

			// Disables the Print button.
			buttonPrint.setEnabled(false);
			// Disables the Sign button.
			buttonSign.setEnabled(false);

			// Shows a progress icon on the title bar to indicate
			// it is working on something.
			setProgressBarIndeterminateVisibility(true);
		}

		/**
		 * This method runs on a background thread. The specified parameters
		 * are the parameters passed to the execute method by the caller of
		 * this task. This method can call publishProgress to publish updates
		 * on the UI thread.
		 */
		@SuppressLint("WrongThread")
		@Override
		protected String doInBackground(String... args)
		{
			LinePrinter lp = null;
			String sResult = null;
			String sPrinterID = args[0];
			String sPrinterAddr = args[1];
			String sDocNumber = "1234567890";
			String sPrinterURI = null;

			if(connectionTypes.getSelectedItem().toString().equals(
					getResources().getString(R.string.bluetooth_connection)))
			{
				// The printer address should be a Bluetooth MAC address.
				if (sPrinterAddr.contains(":") == false && sPrinterAddr.length() == 12)
				{
					// If the MAC address only contains hex digits without the
					// ":" delimiter, then add ":" to the MAC address string.
					char[] cAddr = new char[17];

					for (int i=0, j=0; i < 12; i += 2)
					{
						sPrinterAddr.getChars(i, i+2, cAddr, j);
						j += 2;
						if (j < 17)
						{
							cAddr[j++] = ':';
						}
					}

					sPrinterAddr = new String(cAddr);
				}

				sPrinterURI = "bt://" + sPrinterAddr;
			}
			else if(connectionTypes.getSelectedItem().toString().equals(
					getResources().getString(R.string.serial_connection)))
			{
				// The printer address should be a serial port name.
				sPrinterURI = "serial://" + sPrinterAddr;
			}

			LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();

			exSettings.setContext(PrintActivity.this);

			PrintProgressListener progressListener =
				new PrintProgressListener()
				{
					@Override
					public void receivedStatus(PrintProgressEvent aEvent)
					{
						// Publishes updates on the UI thread.
						publishProgress(aEvent.getMessageType());
					}
				};

			try
			{
				lp = new LinePrinter(
						jsonCmdAttribStr,
						sPrinterID,
						sPrinterURI,
						exSettings);

				// Registers to listen for the print progress events.
				lp.addPrintProgressListener(progressListener);

				//A retry sequence in case the bluetooth socket is temporarily not ready
				int numtries = 0;
				int maxretry = 2;
				while(numtries < maxretry)
				{
					try{
						lp.connect();  // Connects to the printer
						break;
					}
					catch(LinePrinterException ex){
						numtries++;
						Thread.sleep(1000);
					}
				}
				if (numtries == maxretry) lp.connect();//Final retry

				// Check the state of the printer and abort printing if there are
				// any critical errors detected.
				int[] results = lp.getStatus();
				if (results != null)
				{
					for (int err = 0; err < results.length; err++)
					{
						if (results[err] == 223)
						{
							// Paper out.
							throw new BadPrinterStateException("Paper out");
						}
						else if (results[err] == 227)
						{
							// Lid open.
							throw new BadPrinterStateException("Printer lid open");
						}
					}
				}
					int ofset=0;
					int widh=0;
					int heigh=0;

                if(commande!=null){
					for (String cmd : commande) {
						if(cmd.startsWith("write")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							lp.write(v);
						}
						if(cmd.startsWith("newLine")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							int val=Integer.parseInt(v);
							lp.newLine(val);

						}
						if(cmd.startsWith("setBold")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							 if(v.contains("true")){
								 lp.setBold(true);
							 }else{
								 lp.setBold(false);
							 }
						}
						if(cmd.startsWith("setDoubleWide")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							if(v.contains("true")){
								lp.setDoubleWide(true);
							}else{
								lp.setDoubleWide(false);
							}

						}
						if(cmd.startsWith("setDoubleHigh")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							if(v.contains("true")){
								lp.setDoubleHigh(true);
							}else{
								lp.setDoubleHigh(false);
							}
						}
						if(cmd.startsWith("offset")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							ofset=Integer.parseInt(v);

						}
						if(cmd.startsWith("width")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							widh=Integer.parseInt(v);

						}
						if(cmd.startsWith("heigh")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());
							heigh=Integer.parseInt(v);

						}
						if(cmd.startsWith("image")){
							String v=cmd.substring(cmd.indexOf(";")+1,cmd.length());

							lp.writeGraphicBase64(v.toString(),
									LinePrinter.GraphicRotationDegrees.DEGREE_0,
									ofset,  // Offset in printhead dots from the left of the page
									widh, // Desired graphic width on paper in printhead dots
									heigh);
						}
					}

				}
				//signature
				lp.setBold(true);
				lp.write("by phincode");
				lp.setBold(false);
				lp.newLine(2);
				lp.newLine(2);
				lp.write("-------------------------------------------------------");

				// Prints the image  graphic.
				/*lp.writeGraphicBase64(base64LogoPng,
							LinePrinter.GraphicRotationDegrees.DEGREE_0,
							80,  // Offset in printhead dots from the left of the page
							400, // Desired graphic width on paper in printhead dots
							400); // Desired graphic height on paper in printhead dots
				*/

				// Set font style to Bold + Double Wide + Double High.
				/*lp.setBold(true);
				lp.setDoubleWide(true);
				lp.setDoubleHigh(true);
				lp.write("SALES ORDER");
				lp.setDoubleWide(false);
				lp.setDoubleHigh(false);
				lp.newLine(2);*/

				// The following text shall be printed in Bold font style.
				/*lp.write("CUSTOMER: Casual Step");
				lp.setBold(false);  // Returns to normal font.
				lp.newLine(2);*/

				// Set font style to Compressed + Double High.
				/*lp.setDoubleHigh(true);
				lp.setCompress(true);
				lp.write("DOCUMENT#: " + sDocNumber);
				lp.setCompress(false);
				lp.setDoubleHigh(false);
				lp.newLine(2);*/

				// The following text shall be printed in Normal font style.
				/*lp.write(" PRD. DESCRIPT.   PRC.  QTY.    NET.");
				lp.newLine(2);

				lp.write(" 1501 Timer-Md1  13.15     1   13.15");
				lp.newLine(1);
				lp.write(" 1502 Timer-Md2  13.15     3   39.45");
				lp.newLine(1);
				lp.write(" 1503 Timer-Md3  13.15     2   26.30");
				lp.newLine(1);
				lp.write(" 1504 Timer-Md4  13.15     4   52.60");
				lp.newLine(1);
				lp.write(" 1505 Timer-Md5  13.15     5   65.75");
				lp.newLine(1);
				lp.write("                        ----  ------");
				lp.newLine(1);
				lp.write("              SUBTOTAL    15  197.25");
				lp.newLine(2);

				lp.write("          5% State Tax          9.86");
				lp.newLine(2);

				lp.write("                              ------");
				lp.newLine(1);
				lp.write("           BALANCE DUE        207.11");
				lp.newLine(1);
				lp.newLine(1);

				lp.write(" PAYMENT TYPE: CASH");
				lp.newLine(2);

				lp.setDoubleHigh(true);
				lp.write("       SIGNATURE / STORE STAMP");
				lp.setDoubleHigh(false);
				lp.newLine(2);
*/
				// Prints the captured signature if it exists.
				/*if (base64SignaturePng != null)
				{
					lp.writeGraphicBase64(base64SignaturePng,
							LinePrinter.GraphicRotationDegrees.DEGREE_0,
							72,   // Offset in printhead dots from the left of the page
							220,  // Desired graphic width on paper in printhead dots
							100); // Desired graphic height on paper in printhead dots
				}
				lp.newLine(1);

				lp.setBold(true);

				lp.write("          ORIGINAL");
				lp.setBold(false);
				lp.newLine(2);*/

				// Print a Code 39 barcode containing the document number.
				/*lp.writeBarcode(LinePrinter.BarcodeSymbologies.SYMBOLOGY_CODE39,
						sDocNumber,   // Document# to encode in barcode
						60,           // Desired height of the barcode in printhead dots
						40);          // Offset in printhead dots from the left of the page

				lp.newLine(4);*/

				sResult = "Number of bytes sent to printer: " + lp.getBytesWritten();
			}
			catch (BadPrinterStateException ex)
			{
				// Stop listening for printer events.
				lp.removePrintProgressListener(progressListener);
				sResult = "Printer error detected: " + ex.getMessage() + ". Please correct the error and try again.";
			}
			catch (LinePrinterException ex)
			{
				sResult = "LinePrinterException: " + ex.getMessage();
			}
			catch (Exception ex)
			{
				if (ex.getMessage() != null)
					sResult = "Unexpected exception: " + ex.getMessage();
				else
					sResult = "Unexpected exception.";
			}
			finally
			{
				if (lp != null)
				{
					try
					{
						lp.disconnect();  // Disconnects from the printer
						lp.close();  // Releases resources
					}
					catch (Exception ex) {}
				}
			}

			// The result string will be passed to the onPostExecute method
			// for display in the the Progress and Status text box.
			return sResult;
		}

		/**
		 * Runs on the UI thread after publishProgress is invoked. The
		 * specified values are the values passed to publishProgress.
		 */
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// Access the values array.
			int progress = values[0];

			switch (progress)
			{
			case PrintProgressEvent.MessageTypes.CANCEL:
				textMsg.append(PROGRESS_CANCEL_MSG);
				break;
			case PrintProgressEvent.MessageTypes.COMPLETE:
				textMsg.append(PROGRESS_COMPLETE_MSG);
				break;
			case PrintProgressEvent.MessageTypes.ENDDOC:
				textMsg.append(PROGRESS_ENDDOC_MSG);
				break;
			case PrintProgressEvent.MessageTypes.FINISHED:
				textMsg.append(PROGRESS_FINISHED_MSG);
				break;
			case PrintProgressEvent.MessageTypes.STARTDOC:
				textMsg.append(PROGRESS_STARTDOC_MSG);
				break;
			default:
				textMsg.append(PROGRESS_NONE_MSG);
				break;
			}
		}

		/**
		 * Runs on the UI thread after doInBackground method. The specified
		 * result parameter is the value returned by doInBackground.
		 */
		@Override
		protected void onPostExecute(String result)
		{
			// Displays the result (number of bytes sent to the printer or
			// exception message) in the Progress and Status text box.
			if (result != null)
			{
				textMsg.append(result);
			}

			// Dismisses the progress icon on the title bar.
			setProgressBarIndeterminateVisibility(false);

			// Enables the Print button.
			buttonPrint.setEnabled(true);
			// Enables the Sign button.
			buttonSign.setEnabled(true);
		}
	} //endofclass PrintTask

	/**
	 * Called when an activity launched by this activity exits.
	 * @param requestCode The integer request code originally supplied to
	 * startActivityForResult(), allowing you to identify who this result came from.
	 * @param resultCode The integer result code returned by the child activity
	 * through its setResult().
	 * @param data An Intent, which can return result data to the caller
	 * (various data can be attached to Intent "extras").
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Bundle extras;

		switch (requestCode)
		{
		case CAPTURE_SIGNATURE_ACTIVITY:
			if (RESULT_OK == resultCode)
			{
				// Gets the Base64 encoded PNG signature graphic.
				extras = data.getExtras();
				base64SignaturePng = extras.getString(CaptureSignatureActivity.BASE64_SIGNATURE_KEY);
				displaySignature(base64SignaturePng);
			}
			break;
		}
	}

	/**
	 * Displays the specified graphic in the imange view next to the Sign button.
	 * @param base64Png A Base64 encoded PNG image.
	 */
	private void displaySignature(String base64Png)
	{
		if (base64Png != null)
		{
			byte[] signPngBytes = Base64.decode(base64Png, Base64.DEFAULT);
			Bitmap signBitmap = BitmapFactory.decodeByteArray(signPngBytes, 0, signPngBytes.length);
			if (signBitmap != null)
			{
				imgSignature.setImageBitmap(signBitmap);
			}
			else
			{
				// Clears the image view.
				imgSignature.setImageDrawable(null);
			}
		}
		else
		{
			// Clears the image view.
			imgSignature.setImageDrawable(null);
		}
	}
}
