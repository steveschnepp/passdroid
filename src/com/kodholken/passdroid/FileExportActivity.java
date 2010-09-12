/*    
    This file is part of the Passdroid password management software.
    
    Copyright (C) 2009-2010  Magnus Eriksson <eriksson.mag@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.kodholken.passdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FileExportActivity extends Activity {
	private Button cancelButton;
	private Button exportButton;
	private EditText exportFilename;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.file_export);
		
		exportFilename = (EditText) this.findViewById(R.id.filename);
		String filename = "/passdroid_db.xml";
		if (Environment.getExternalStorageDirectory() != null) {
			filename = Environment.getExternalStorageDirectory() + "/passdroid_db.xml";
		}
		exportFilename.setText(filename);
		
		cancelButton = (Button) this.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		exportButton = (Button) this.findViewById(R.id.export_button);
		exportButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doExport();
			}
		});
	}
	
	private void doExport() {
		FileExporter exporter = new FileExporter(
										exportFilename.getText().toString(),
										Utils.getVersion(this));
		try {
			boolean res = exporter.export(PasswordModel.getInstance(this).getPasswords(), true);
			if (res) {
				showDialog("Success", "Database successfully exported to " + exportFilename.getText().toString());
			} else {
				showDialog("Failure", "Export failed");
			}
		} catch (ExportException e) {
			Utils.alertDialog(this, "Failure", "Export failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void showDialog(String title, String message) {
		AlertDialog alertDialog;
		
	    alertDialog = new AlertDialog.Builder(this).create();
	    alertDialog.setTitle(title);
	    alertDialog.setMessage(message);
	    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	        finish();
	      } }); 
	    alertDialog.show();
	}
}