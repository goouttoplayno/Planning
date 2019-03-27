package com.example.nationstyle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.SQL.GetJianJie;
import com.example.nationstyle.Travels;
import com.example.nationstyle.AphidLog;
import com.example.nationstyle.UI;
import com.example.socialplanning.R;

import java.util.ArrayList;
import java.util.List;

public class TravelAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private GetJianJie getjianjie;
	private int where;
	private boolean b = true;

	private String[] names = new String[] { "阿昌族", "白族", "保安族 ", "布朗族 ", "布依族",
			"朝鲜族 ", "达斡尔族 ", "傣族", "德昂族 ", "东乡族 ", "侗族 ", "独龙族", "俄罗斯族",
			"鄂伦春族 ", "鄂温克族", "高山族 ", "仡佬族 ", "哈尼族 ", "哈萨克族 ", "汉族 ", "赫哲族 ",
			"回族 ", "基诺族 ", "京族 ", "景颇族 ", "柯尔克孜族", "拉祜族 ", "黎族", "僳僳族 ",
			"珞巴族 ", "满族", "毛南族", "门巴族 ", "蒙古族", "苗族", "仫佬族", "纳西族 ", "怒族 ",
			"普米族 ", "羌族", "撒拉族 ", "畲族 ", "水族", "塔吉克族", "塔塔尔族 ", "土家族 ", "土族",
			"佤族 ", "维吾尔族", "乌孜别克族 ", "锡伯族 ", "瑶族 ", "彝族 ", "裕固族 ", "藏族", "壮族" };

	private int[] images = new int[] { R.drawable.achangzub, R.drawable.baizub,
			R.drawable.baoanzub, R.drawable.bulangzub, R.drawable.buyizub,
			R.drawable.chaoxianzub, R.drawable.dawoerzub, R.drawable.daizub,
			R.drawable.deangzub, R.drawable.dongxiangzub, R.drawable.tongzub,
			R.drawable.dulongzub, R.drawable.eluosizub, R.drawable.elunchunzub,
			R.drawable.ewenkezub, R.drawable.gaoshanzub, R.drawable.gelaozub,
			R.drawable.hanizub, R.drawable.hasakezub, R.drawable.hanzub,
			R.drawable.hezhezub, R.drawable.huizub, R.drawable.jinuozub,
			R.drawable.jingzub, R.drawable.jingpozub, R.drawable.keerkezizub,
			R.drawable.lahuzub, R.drawable.lizub, R.drawable.susuzub,
			R.drawable.luobazub, R.drawable.manzub, R.drawable.maonanzub,
			R.drawable.menbazub, R.drawable.mengguzub, R.drawable.miaozub,
			R.drawable.mulaozub, R.drawable.naxizub, R.drawable.nuzub,
			R.drawable.pumizub, R.drawable.qiangzub, R.drawable.salazub,
			R.drawable.shuizub, R.drawable.shuizub, R.drawable.tajikezub,
			R.drawable.tataerzub, R.drawable.tujiazub, R.drawable.tujiazub,
			R.drawable.wazub, R.drawable.weiwuerzub, R.drawable.wuzibiekezub,
			R.drawable.xibozub, R.drawable.yaozub, R.drawable.yizub,
			R.drawable.yuguzub, R.drawable.zangzub, R.drawable.zhuangzub };
	private int repeatCount = 1;

	private List<Travels.Data> travelData;

	public TravelAdapter(Context context, int where) {
		inflater = LayoutInflater.from(context);
		travelData = new ArrayList<Travels.Data>(Travels.IMG_DESCRIPTIONS);
		this.context = context;
		this.getjianjie = new GetJianJie(context);
		this.where = where;
	}

	@Override
	public int getCount() {
		return travelData.size() * repeatCount;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		if (convertView == null) {
			layout = inflater.inflate(R.layout.jianjie, null);
			AphidLog.d("created new view from adapter: %d", position);
		}
		if (b) {
			position = where;
			b = false;
		}
		final int w = position;
		final Travels.Data data = travelData.get(position % travelData.size());

		UI.<TextView> findViewById(layout, R.id.zwqtv).setText(
				getjianjie.getJianJie(data.id));
		UI.<TextView> findViewById(layout, R.id.jianjielotv).setText(
				names[position]);
		UI.<ImageView> findViewById(layout, R.id.jianjieimg)
				.setBackgroundResource(images[position]);

		UI.<Button> findViewById(layout, R.id.jianjiebtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("id", w + "");
						intent.setClass(context,
								com.example.nation.mingzujinji.class);
						inflater.getContext().startActivity(intent);
					}
				});
		UI.<Button> findViewById(layout, R.id.jianjieback).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(context,
								com.example.nation.listViewActivity.class);
						inflater.getContext().startActivity(intent);
					}
				});

		return layout;
	}

	public void removeData(int index) {
		if (travelData.size() > 1) {
			travelData.remove(index);
		}
	}
}
