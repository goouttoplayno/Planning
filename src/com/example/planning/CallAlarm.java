package com.example.planning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 自定义BroadcastReceiver 当闹钟设置的时间到了的时候，广播会被唤起,并运行onreceive方法
 * @author CxLong
 *
 */
public class CallAlarm extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context, AlarmAlert.class);
		Bundle bundle = new Bundle();
		bundle.putString("STR_CALLER", "");
		i.putExtras(bundle);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
}
