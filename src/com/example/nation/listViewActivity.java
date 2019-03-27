package com.example.nation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.example.planning.MyApp;
import com.example.socialplanning.R;
import com.uitl.ToPinYin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class listViewActivity extends Activity {

	private SideBar indexBar;
	private ListView mylistview;
	private Button backbtn;
	private ImageView deleteall;
	private EditText searchname;
	List<Map<String, Object>> ListItems;
	SimpleAdapter simpleAdapter;
	private View head;
	private WindowManager mWindowManager;
	private MyApp myapp;

	private TextView mDialogText;
	private ProgressDialog p_bar;
	private boolean search = true;

	private String[] names = new String[] { "阿昌族", "白族", "保安族 ", "布朗族 ", "布依族",
			"朝鲜族 ", "达斡尔族 ", "傣族", "德昂族 ", "东乡族 ", "侗族 ", "独龙族", "俄罗斯族",
			"鄂伦春族 ", "鄂温克族", "高山族 ", "仡佬族 ", "哈尼族 ", "哈萨克族 ", "汉族 ", "赫哲族 ",
			"回族 ", "基诺族 ", "京族 ", "景颇族 ", "柯尔克孜族", "拉祜族 ", "黎族", "僳僳族 ",
			"珞巴族 ", "满族", "毛南族", "门巴族 ", "蒙古族", "苗族", "仫佬族", "纳西族 ", "怒族 ",
			"普米族 ", "羌族", "撒拉族 ", "畲族 ", "水族", "塔吉克族", "塔塔尔族 ", "土家族 ", "土族",
			"佤族 ", "维吾尔族", "乌孜别克族 ", "锡伯族 ", "瑶族 ", "彝族 ", "裕固族 ", "藏族", "壮族" };

	@SuppressWarnings("unused")
	private int[] images = new int[] { R.drawable.achangzu, R.drawable.baizu,
			R.drawable.baoanzu, R.drawable.bulangzu, R.drawable.buyizu,
			R.drawable.chaoxianzuu, R.drawable.dawoerzu, R.drawable.daizu,
			R.drawable.deangzu, R.drawable.dongxiangzu, R.drawable.tongzu,
			R.drawable.dulong, R.drawable.eluosizu, R.drawable.elunchunzu,
			R.drawable.ewenkezu, R.drawable.gaoshanzu, R.drawable.gelaozu,
			R.drawable.hanizu, R.drawable.hasakezu, R.drawable.hanzu,
			R.drawable.hezhezu, R.drawable.huizu, R.drawable.jinuozu,
			R.drawable.jingzu, R.drawable.jingpozu, R.drawable.keerkezizu,
			R.drawable.lakuzu, R.drawable.lizu, R.drawable.lisuzu,
			R.drawable.luobazu, R.drawable.manzu, R.drawable.maonanzu,
			R.drawable.menbazu, R.drawable.mengguzu, R.drawable.miaozu,
			R.drawable.mulaozu, R.drawable.naxizu, R.drawable.nuzu,
			R.drawable.pumiu, R.drawable.qiangzu, R.drawable.salazu,
			R.drawable.shuizu, R.drawable.shuizu, R.drawable.tajikezu,
			R.drawable.tataerzu, R.drawable.tujiazu, R.drawable.tujia,
			R.drawable.wazu, R.drawable.weiwuerzu, R.drawable.wuzibiekezu,
			R.drawable.xibozu, R.drawable.yaozu, R.drawable.yizu,
			R.drawable.yuguzu, R.drawable.zangzu, R.drawable.zhuangzu };
	List<Content> list = new ArrayList<Content>();
	private Handler searchhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == InfoHandler.OVER) {
				// mylistview.setAdapter(simpleAdapter);
				Collections.sort(list, new PinyinComparator());
				MyAdapter adapter = new MyAdapter(listViewActivity.this, list);
				mylistview.setAdapter(adapter);
				indexBar.setListView(mylistview);
			} else if (msg.what == InfoHandler.ISNULL) {
				Toast.makeText(listViewActivity.this, "未找到指定民族", 4000).show();
			}
		}
	};

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		myapp = (MyApp) getApplication();
		indexBar = (SideBar) findViewById(R.id.sideBar);
		mylistview = (ListView) findViewById(R.id.zshow);
		head = LayoutInflater.from(this).inflate(R.layout.head, null);
		searchname = (EditText) findViewById(R.id.searchname);
		deleteall = (ImageView) findViewById(R.id.deleteallbtn);
		backbtn = (Button) findViewById(R.id.nationcancel);
		ListItems = new ArrayList<Map<String, Object>>();
		deleteall.setVisibility(View.INVISIBLE);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.list_position, null);

		mylistview.addHeaderView(head);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);

		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				// TODO Auto-generated method stub
				TextView nationnametv = (TextView) arg1
						.findViewById(R.id.sidetitle);

				int w = 0;
				for (int i = 0; i <= 55; i++) {
					if (nationnametv.getText().toString().equals(names[i])) {
						w = i;
						break;
					}
				}
				myapp.setWhere(w);

				Intent intent = new Intent();
				intent.putExtra("id", w + "");
				intent.setClass(
						listViewActivity.this,
						com.example.nationstyle.FlipHorizontalLayoutActivity.class);
				listViewActivity.this.startActivity(intent);
				deleteall.setVisibility(View.INVISIBLE);
				searchname.removeTextChangedListener(textwacher);
				searchname.setText("");
				searchname.addTextChangedListener(textwacher);
				finish();
			}

		});
		deleteall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getAllNation();
				searchname.removeTextChangedListener(textwacher);
				searchname.setText("");
				searchname.addTextChangedListener(textwacher);
				deleteall.setVisibility(View.INVISIBLE);
				search = true;
			}
		});
		searchname.addTextChangedListener(textwacher);
		getAllNation();

	}

	public void getAllNation() {
		List<Content> list = new ArrayList<Content>();

		for (int i = 0; i < names.length; i++) {
			try {
				Content c = new Content(String.valueOf(ToPinYin
						.getPinYin(names[i]).toLowerCase().charAt(0)), names[i]);
				list.add(c);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				// TODO: handle exception
			}

		}

		Collections.sort(list, new PinyinComparator());
		// 实例化自定义内容适配类
		MyAdapter adapter = new MyAdapter(this, list);
		// 为listView设置适配
		mylistview.setAdapter(adapter);
		// 设置SideBar的ListView内容实现点击a-z中任意一个进行定位
		indexBar.setListView(mylistview);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getAllNation();

	}

	TextWatcher textwacher = new TextWatcher() {
		String name = "";

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			name = s.toString();

			if (name.equals("") || name == null) {
				deleteall.setVisibility(View.INVISIBLE);
				getAllNation();
				search = true;
			} else {
				if (search) {
					p_bar = ProgressDialog.show(listViewActivity.this, null,
							"正在查找...");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							search(name);
						}
					}).start();
					p_bar.dismiss();
					deleteall.setVisibility(View.VISIBLE);
				}

			}
		}
	};

	public void search(String nationname) {
		list.clear();
		for (int i = 0; i <= 55; i++) {
			if (names[i].contains(nationname)) {
				try {
					Content c = new Content(String.valueOf(ToPinYin
							.getPinYin(names[i]).toLowerCase().charAt(0)),
							names[i]);
					list.add(c);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// TODO: handle exception
				}

			}
		}
		Message msg = new Message();
		msg.what = InfoHandler.OVER;
		if (list.isEmpty()) {
			msg.what = InfoHandler.ISNULL;
			search = false;

		}

		searchhandler.sendMessage(msg);

	}
}
