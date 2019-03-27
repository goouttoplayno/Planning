package com.example.nation;

import java.util.List;

import com.example.socialplanning.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements SectionIndexer {

	private List<Content> list = null;
	private Context mContext;
	// private SectionIndexer mIndexer;

	private String[] names = new String[] { "阿昌族", "白族", "保安族 ", "布朗族 ", "布依族",
			"朝鲜族 ", "达斡尔族 ", "傣族", "德昂族 ", "东乡族 ", "侗族 ", "独龙族", "俄罗斯族",
			"鄂伦春族 ", "鄂温克族", "高山族 ", "仡佬族 ", "哈尼族 ", "哈萨克族 ", "汉族 ", "赫哲族 ",
			"回族 ", "基诺族 ", "京族 ", "景颇族 ", "柯尔克孜族", "拉祜族 ", "黎族", "僳僳族 ",
			"珞巴族 ", "满族", "毛南族", "门巴族 ", "蒙古族", "苗族", "仫佬族", "纳西族 ", "怒族 ",
			"普米族 ", "羌族", "撒拉族 ", "畲族 ", "水族", "塔吉克族", "塔塔尔族 ", "土家族 ", "土族",
			"佤族 ", "维吾尔族", "乌孜别克族 ", "锡伯族 ", "瑶族 ", "彝族 ", "裕固族 ", "藏族", "壮族" };

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

	public MyAdapter(Context mContext, List<Content> list) {
		this.mContext = mContext;
		this.list = list;

	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
			viewHolder.iv = (ImageView) view.findViewById(R.id.sideimg);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.sidetitle);
			viewHolder.tvLetter = (TextView) view
					.findViewById(R.id.sidecatalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Content mContent = list.get(position);
		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getLetter());
		} else {
			String lastCatalog = list.get(position - 1).getLetter();
			if (mContent.getLetter().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getLetter());
			}
		}
		int w = 0;
		for (int i = 0; i <= 55; i++) {
			if (this.list.get(position).getName() == names[i]) {
				w = i;
				break;
			}
		}
		viewHolder.iv.setBackgroundResource(images[w]);
		viewHolder.tvTitle.setText(this.list.get(position).getName());

		return view;

	}

	final static class ViewHolder {
		TextView tvTitle;
		TextView tvLetter;
		ImageView iv;
	}

	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSectionForPosition(int position) {

		return 0;
	}

	public int getPositionForSection(int section) {
		Content mContent;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mContent = (Content) list.get(i);
				l = mContent.getLetter();
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i + 1;
				}

			}
		}
		mContent = null;
		l = null;
		return -1;
	}
}