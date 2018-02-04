package com.arcsoft.sdk_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import moma.listen.ListenService;
import moma.service.DisCoveryService;
import moma.service.EchoService;

public class MainActivity extends Activity  {
	private final String TAG = this.getClass().toString();

	private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
	private static final int REQUEST_CODE_IMAGE_OP = 2;
	private static final int REQUEST_CODE_OP = 3;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_test);

		setStartService();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void setStartService(){

		Intent echoservice = new Intent(this, EchoService.class);
		startService(echoservice);

		Intent disservice = new Intent(this, DisCoveryService.class);
		startService(disservice);

		Intent listendiscory = new Intent(this, ListenService.class);
		startService(listendiscory);

		new AlertDialog.Builder(this)
				.setTitle("请选择相机")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setItems(new String[]{"后置相机", "前置相机"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startDetector(which);
					}
				})
				.show();


	}


	private void startDetector(int camera) {
		Intent it = new Intent(MainActivity.this, DetecterActivity.class);
		it.putExtra("Camera", camera);
		startActivityForResult(it, REQUEST_CODE_OP);
	}

}

