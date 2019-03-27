package com.example.planning;

import java.util.Calendar;

import com.example.SQL.DataBaseHelper;
import com.example.socialplanning.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class AddItem extends Activity {

	Resources r;
	DrawView draw;

	boolean isStart = true;
	boolean isEnd = true;// true未结束 false 结束
	boolean isItem = false;

	private LinearLayout paint = null;
	// private RelativeLayout mainRelative;
	private RelativeLayout bottom;

	private Button startbtn, normalbtn, endbtn, importantbtn, savebtn,
			cancelbtn;
	private String itemname;
	private String liucheng_start = "";
	private String liucheng_important = "";
	private String liucheng_normal = "";
	private String liucheng_end = "";
	private String editor = "a";
	private String belongto;

	private int normalcount = 0;
	private int importantcount = 0;
	private int count = 0;
	static int itemid = 1;

	Calendar calendar = Calendar.getInstance();
	private DataBaseHelper dbhelper;
	private SQLiteDatabase db;
	private Builder ad;
	private Builder ad2;
	private Builder ad3;
	private Builder ad4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		ad = new Builder(AddItem.this, R.style.dialog);
		ad2 = new Builder(AddItem.this, R.style.dialog);
		ad3 = new Builder(AddItem.this, R.style.dialog);
		ad4 = new Builder(AddItem.this, R.style.dialog);
		Intent intent = getIntent();

		editor = intent.getStringExtra("editor");
		if (editor.equals("xiugai")) {
			belongto = intent.getStringExtra("belongto");
		}
		paint = (LinearLayout) findViewById(R.id.addpaint);
		// mainRelative = (RelativeLayout) findViewById(R.id.main);
		bottom = (RelativeLayout) findViewById(R.id.buttom);
		startbtn = (Button) findViewById(R.id.start);
		normalbtn = (Button) findViewById(R.id.normal);
		importantbtn = (Button) findViewById(R.id.important);
		endbtn = (Button) findViewById(R.id.end);
		savebtn = (Button) findViewById(R.id.savebtn);
		cancelbtn = (Button) findViewById(R.id.cancelbtn);

		dbhelper = new DataBaseHelper(this);
		ButtonListener b = new ButtonListener();

		startbtn.setOnClickListener(b);
		startbtn.setOnTouchListener(b);
		normalbtn.setOnClickListener(b);
		normalbtn.setOnTouchListener(b);
		importantbtn.setOnClickListener(b);
		importantbtn.setOnTouchListener(b);
		endbtn.setOnClickListener(b);
		endbtn.setOnTouchListener(b);

		savebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editor.equals("add")) {
					Intent intent = new Intent();
					intent.putExtra("caozuo", "a");
					intent.putExtra("isend", isEnd);// true未结束 false 结束
					intent.setClass(AddItem.this, SetNameAndDate.class);
					AddItem.this.startActivity(intent);
				} else if (editor.equals("xiugai")) {
					db = dbhelper.getWritableDatabase();
					db.execSQL("delete from liucheng where belongto ='"
							+ belongto + "'");
					db.execSQL("insert into liucheng ('liuchengname','leixing','itemcontent','waring','clock','start','end','belongto') select liuchengname,leixing,itemcontent,waring,clock,start,end,belongto from linshi");
					db.execSQL("delete from linshi");

					if (isEnd)// true未结束 false 结束
					{
						db.execSQL("update datanamelist set end='no' where name='"
								+ belongto + "' ");
					} else {
						db.execSQL("update datanamelist set end='yes' where name='"
								+ belongto + "' ");
					}

					db.close();
				}
				finish();
			}
		});
		cancelbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(AddItem.this, R.style.dialog)
						.setTitle("提示")
						.setMessage("确定放弃所有？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										db = dbhelper.getWritableDatabase();
										db.execSQL("delete from linshi;");
										db.close();
										itemid = 1;
										paint.removeAllViews();
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
									}
								}).show();

			}
		});
		addItem(editor);
		draw = new DrawView(AddItem.this);
	}

	public class ButtonListener implements OnClickListener, OnTouchListener {
		public void onClick(View v) {

		}

		public boolean onTouch(View v, MotionEvent event) {

			if (v.getId() == R.id.start) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					bottom.removeView(draw);
					if (event.getX() >= v.getWidth()) {
						AddImage ai = new AddImage();
						ai.Add(0, "");

					}
					normalbtn.setEnabled(true);
					importantbtn.setEnabled(true);
					endbtn.setEnabled(true);

				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {

					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() - (v.getHeight() / 2);
					draw.invalidate();

				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					draw.SetNum(0);
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = startbtn.getY();

					bottom.addView(draw);

					normalbtn.setEnabled(false);
					importantbtn.setEnabled(false);
					endbtn.setEnabled(false);
				}
			} else if (v.getId() == R.id.normal) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					bottom.removeView(draw);
					if (event.getX() >= v.getWidth()) {

						AddImage ai = new AddImage();
						ai.Add(1, "");
						normalcount++;

					}
					startbtn.setEnabled(true);
					importantbtn.setEnabled(true);
					endbtn.setEnabled(true);

				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() + normalbtn.getY()
							- (normalbtn.getHeight() / 2);
					draw.invalidate();

				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					draw.SetNum(1);
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() + normalbtn.getY()
							- (normalbtn.getHeight() / 2);

					bottom.addView(draw);
					startbtn.setEnabled(false);
					importantbtn.setEnabled(false);
					endbtn.setEnabled(false);
				}

			} else if (v.getId() == R.id.important) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					bottom.removeView(draw);
					if (event.getX() >= v.getWidth()) {

						AddImage ai = new AddImage();
						ai.Add(2, "");

					}
					startbtn.setEnabled(true);
					normalbtn.setEnabled(true);
					endbtn.setEnabled(true);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() + importantbtn.getY()
							- (importantbtn.getHeight() / 2);
					draw.invalidate();

				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					draw.SetNum(2);
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() + importantbtn.getY()
							- (importantbtn.getHeight() / 2);

					bottom.addView(draw);
					startbtn.setEnabled(false);
					normalbtn.setEnabled(false);
					endbtn.setEnabled(false);
				}

			} else {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					bottom.removeView(draw);
					if (event.getX() >= v.getWidth()) {
						AddImage ai = new AddImage();
						ai.Add(3, "");
					}
					startbtn.setEnabled(true);
					normalbtn.setEnabled(true);
					importantbtn.setEnabled(true);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() + endbtn.getY()
							- (endbtn.getHeight() / 2);
					draw.invalidate();

				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					draw.SetNum(3);
					draw.currentX = event.getX() - (v.getWidth() / 2);
					draw.currentY = event.getY() + endbtn.getY()
							- (endbtn.getHeight() / 2);

					bottom.addView(draw);
					startbtn.setEnabled(false);
					normalbtn.setEnabled(false);
					importantbtn.setEnabled(false);
				}

			}
			return false;
		}
	}

	public class AddImage {

		public void Add(int i, String liu) {

			if (i == 0) {
				if (isStart) {
					final TextView as = new TextView(AddItem.this);
					as.setTextSize(20);
					as.setGravity(Gravity.CENTER_HORIZONTAL);
					if (editor.equals("add") || editor.equals("xiugai")) {
						if (liu.equals("")) {
							as.setText("开始");
							Log.i("addtext", as.getText().toString());
							setLiuChengName("开始", "start", as.getText()
									.toString());
						} else {
							as.setText(liu);
						}
					}
					as.setEllipsize(TruncateAt.END);
					as.setMaxLines(2);
					as.setMaxEms(8);
					as.setGravity(Gravity.CENTER_HORIZONTAL);
					as.setBackgroundResource(R.drawable.start);
					as.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							LayoutInflater factory = LayoutInflater
									.from(AddItem.this);
							final View view = factory.inflate(
									R.layout.edittext, null);
							final EditText et = (EditText) view
									.findViewById(R.id.innameedittext);
							if (as.getText().toString().equals("")) {
								et.setText("");
							} else {
								et.setText(as.getText().toString());
							}
							ad.setTitle("流程名称")
									.setView(view)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub

													liucheng_start = et
															.getText()
															.toString();
													if (et.getText().toString()
															.contains("'")) {
														Toast.makeText(
																AddItem.this,
																"请勿在编辑框中包含 ' 字符",
																Toast.LENGTH_SHORT)
																.show();
													} else {

														if (setLiuChengName(
																liucheng_start,
																"start",
																as.getText()
																		.toString()))
															as.setText(liucheng_start);

													}

												}
											}).setNegativeButton("取消", null)
									.show();

						}
					});
					as.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(final View arg0) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(AddItem.this,
									R.style.dialog)
									.setTitle("设置")
									.setPositiveButton(
											"删除",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialogInterface,
														int which) {
													// TODO Auto-generated
													// method stub
													ad2.setTitle("删除流程")

															.setPositiveButton(
																	"确定",
																	new DialogInterface.OnClickListener() {
																		@Override
																		public void onClick(
																				DialogInterface dialogInterface,
																				int which) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			Log.i("itemnum",
																					"count:"
																							+ count
																							+ " isend:"
																							+ isEnd);
																			if (count == 0
																					&& isEnd) {
																				paint.removeView(arg0);
																				if (!as.getText()
																						.toString()
																						.equals("")) {
																					deleteLiuChengName(as
																							.getText()
																							.toString());
																				}
																				isStart = true;
																			} else {
																				Toast.makeText(
																						AddItem.this,
																						"不能删除.",
																						Toast.LENGTH_SHORT)
																						.show();
																			}
																		}
																	})
															.setNegativeButton(
																	"取消", null)
															.show();

												}
											})
									.setNegativeButton(
											"修改",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub
													if (as.getText().toString()
															.equals("")
															|| as.getText()
																	.toString() == null) {
														Toast.makeText(
																AddItem.this,
																"请先设置流程名称",
																Toast.LENGTH_SHORT)
																.show();
													} else {

														Intent intent = new Intent();
														intent.putExtra(
																"belongto",
																itemname);
														intent.putExtra(
																"liuchengname",
																as.getText()
																		.toString());
														intent.setClass(
																AddItem.this,
																EditorAdd.class);
														AddItem.this
																.startActivity(intent);

													}
												}
											}).show();

							return false;
						}
					});
					LinearLayout.LayoutParams lParams = new LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					paint.addView(as, lParams);
					isStart = false;
				}

			} else if (i == 1) {
				if ((!isStart) && (isEnd)) {
					normalcount++;
					final TextView as = new TextView(AddItem.this);
					as.setTextSize(20);
					// if (editor.equals("add") || editor.equals("xiugai"))
					// as.setText(liu);

					if (editor.equals("add") || editor.equals("xiugai")) {
						if (liu.equals("")) {
							as.setText("未命名事项" + itemid);
							setLiuChengName("未命名事项" + itemid, "normal_"
									+ normalcount, as.getText().toString());
							itemid++;
						} else {
							as.setText(liu);
						}
					}

					as.setMaxLines(2);
					as.setMaxEms(8);
					as.setEllipsize(TruncateAt.END);
					as.setGravity(Gravity.CENTER_HORIZONTAL);
					LinearLayout.LayoutParams lParams = new LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					as.setBackgroundResource(R.drawable.normal);
					as.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							LayoutInflater factory = LayoutInflater
									.from(AddItem.this);
							final View view = factory.inflate(
									R.layout.edittext, null);
							final EditText et = (EditText) view
									.findViewById(R.id.innameedittext);
							if (as.getText().toString().equals("")) {
								et.setText("");
							} else {
								et.setText(as.getText().toString());
							}
							ad.setTitle("流程名称")
									.setView(view)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub

													liucheng_normal = et
															.getText()
															.toString();
													if (et.getText().toString()
															.contains("'")) {
														Toast.makeText(
																AddItem.this,
																"请勿在编辑框中包含 ' 字符",
																Toast.LENGTH_SHORT)
																.show();
													} else {
														if (setLiuChengName(
																liucheng_normal,
																("normal_" + normalcount)
																		.toString(),
																as.getText()
																		.toString()))
															as.setText(liucheng_normal);
													}

												}
											}).setNegativeButton("取消", null)
									.show();
						}
					});
					as.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(final View arg0) {
							// TODO Auto-generated method stub

							new AlertDialog.Builder(AddItem.this,
									R.style.dialog)
									.setTitle("设置")
									.setPositiveButton(
											"删除",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialogInterface,
														int which) {
													// TODO Auto-generated
													// method
													// stub
													ad2.setTitle("删除流程")

															.setPositiveButton(
																	"确定",
																	new DialogInterface.OnClickListener() {
																		@Override
																		public void onClick(
																				DialogInterface dialogInterface,
																				int which) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub

																			paint.removeView(arg0);
																			count--;
																			if (!as.getText()
																					.toString()
																					.equals("")) {
																				deleteLiuChengName(as
																						.getText()
																						.toString());
																			}
																		}
																	})
															.setNegativeButton(
																	"取消", null)
															.show();

												}
											})
									.setNegativeButton(
											"修改",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method
													// stub
													if (as.getText().toString()
															.equals("")
															|| as.getText()
																	.toString() == null) {
														Toast.makeText(
																AddItem.this,
																"请先设置流程名称",
																Toast.LENGTH_SHORT)
																.show();
														Log.i("addddd", as
																.getText()
																.toString()
																+ "123");
													} else {

														Intent intent = new Intent();
														intent.putExtra(
																"belongto",
																itemname);
														intent.putExtra(
																"liuchengname",
																as.getText()
																		.toString());

														intent.setClass(
																AddItem.this,
																EditorAdd.class);
														AddItem.this
																.startActivity(intent);
													}
												}
											}).show();

							return false;
						}
					});

					paint.addView(as, lParams);
					isItem = true;
					count++;
				} else {
					Toast.makeText(AddItem.this, "请先添加开始", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (i == 2) {
				if ((!isStart) && (isEnd)) {
					importantcount++;
					final TextView as = new TextView(AddItem.this);
					as.setTextSize(20);
					if (editor.equals("add") || editor.equals("xiugai"))
						as.setText(liu);

					if (editor.equals("add") || editor.equals("xiugai")) {
						if (liu.equals("")) {
							as.setText("未命名事项" + itemid);
							setLiuChengName("未命名事项" + itemid, "important_"
									+ importantcount, as.getText().toString());
							itemid++;
						} else {
							as.setText(liu);
						}
					}

					as.setEllipsize(TruncateAt.END);
					as.setMaxEms(8);
					as.setMaxLines(2);
					as.setGravity(Gravity.CENTER_HORIZONTAL);
					LinearLayout.LayoutParams lParams = new LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					as.setBackgroundResource(R.drawable.important);
					as.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							LayoutInflater factory = LayoutInflater
									.from(AddItem.this);
							final View view = factory.inflate(
									R.layout.edittext, null);
							final EditText et = (EditText) view
									.findViewById(R.id.innameedittext);
							if (as.getText().toString().equals("")) {
								et.setText("");
							} else {
								et.setText(as.getText().toString());
							}
							ad.setTitle("流程名称")
									.setView(view)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub

													liucheng_important = et
															.getText()
															.toString();
													if (et.getText().toString()
															.contains("'")) {
														Toast.makeText(
																AddItem.this,
																"请勿在编辑框中包含 ' 字符",
																Toast.LENGTH_SHORT)
																.show();
													} else {
														if (setLiuChengName(
																liucheng_important,
																("important_" + importantcount)
																		.toString(),
																as.getText()
																		.toString()))
															as.setText(liucheng_important);
													}

												}
											}).setNegativeButton("取消", null)
									.show();
						}
					});
					as.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(final View arg0) {
							// TODO Auto-generated method stub

							new AlertDialog.Builder(AddItem.this,
									R.style.dialog)
									.setTitle("设置")
									.setPositiveButton(
											"删除",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialogInterface,
														int which) {
													// TODO Auto-generated
													// method
													// stub
													ad2.setTitle("删除流程")

															.setPositiveButton(
																	"确定",
																	new DialogInterface.OnClickListener() {
																		@Override
																		public void onClick(
																				DialogInterface dialogInterface,
																				int which) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub

																			paint.removeView(arg0);
																			count--;
																			if (!as.getText()
																					.toString()
																					.equals("")) {
																				deleteLiuChengName(as
																						.getText()
																						.toString());
																			}
																		}
																	})
															.setNegativeButton(
																	"取消", null)
															.show();

												}
											})
									.setNegativeButton(
											"修改",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method
													// stub
													if (as.getText().toString()
															.equals("")
															|| as.getText()
																	.toString() == null) {
														Toast.makeText(
																AddItem.this,
																"请先设置流程名称",
																Toast.LENGTH_SHORT)
																.show();
													} else {

														Intent intent = new Intent();
														intent.putExtra(
																"belongto",
																itemname);
														intent.putExtra(
																"liuchengname",
																as.getText()
																		.toString());

														intent.setClass(
																AddItem.this,
																EditorAdd.class);
														AddItem.this
																.startActivity(intent);
													}
												}
											}).show();
							return false;
						}
					});
					paint.addView(as, lParams);
					isItem = true;
					count++;
				} else {
					Toast.makeText(AddItem.this, "请先添加开始", Toast.LENGTH_SHORT)
							.show();
				}

			} else if (i == 3) {
				if ((!isStart) && isEnd) {
					final TextView as = new TextView(AddItem.this);
					as.setTextSize(20);

					if (editor.equals("add") || editor.equals("xiugai")) {
						if (liu.equals("")) {
							as.setText("结束");

							setLiuChengName("结束", "end", as.getText()
									.toString());
						} else {
							as.setText(liu);
						}
					}

					as.setMaxLines(2);
					as.setEllipsize(TruncateAt.END);
					as.setMaxEms(8);
					as.setGravity(Gravity.CENTER_HORIZONTAL);
					LinearLayout.LayoutParams lParams = new LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					as.setBackgroundResource(R.drawable.endtool);
					as.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							LayoutInflater factory = LayoutInflater
									.from(AddItem.this);
							final View view = factory.inflate(
									R.layout.edittext, null);
							final EditText et = (EditText) view
									.findViewById(R.id.innameedittext);
							if (as.getText().toString().equals("")) {
								et.setText("");
							} else {
								et.setText(as.getText().toString());
							}
							ad.setTitle("流程名称")
									.setView(view)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub

													liucheng_end = et.getText()
															.toString();
													if (et.getText().toString()
															.contains("'")) {
														Toast.makeText(
																AddItem.this,
																"请勿在编辑框中包含 ' 字符",
																Toast.LENGTH_SHORT)
																.show();
													} else {
														if (setLiuChengName(
																liucheng_end,
																"end",
																as.getText()
																		.toString()))
															as.setText(liucheng_end);
													}
												}
											}).setNegativeButton("取消", null)
									.show();

						}
					});
					as.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(final View arg0) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(AddItem.this,
									R.style.dialog)
									.setTitle("设置")
									.setPositiveButton(
											"删除",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialogInterface,
														int which) {
													// TODO Auto-generated
													// method
													// stub

													ad2.setTitle("删除流程")

															.setPositiveButton(
																	"确定",
																	new DialogInterface.OnClickListener() {
																		@Override
																		public void onClick(
																				DialogInterface dialogInterface,
																				int which) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			paint.removeView(arg0);
																			if (!as.getText()
																					.toString()
																					.equals("")) {
																				deleteLiuChengName(as
																						.getText()
																						.toString());
																			}
																			isEnd = true;
																		}
																	})
															.setNegativeButton(
																	"取消", null)
															.show();

												}
											})
									.setNegativeButton(
											"修改",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method
													// stub
													if (as.getText().toString()
															.equals("")
															|| as.getText()
																	.toString() == null) {
														Toast.makeText(
																AddItem.this,
																"请先设置流程名称",
																Toast.LENGTH_SHORT)
																.show();
													} else {

														Intent intent = new Intent();
														intent.putExtra(
																"belongto",
																itemname);
														intent.putExtra(
																"liuchengname",
																as.getText()
																		.toString());
														intent.setClass(
																AddItem.this,
																EditorAdd.class);
														AddItem.this
																.startActivity(intent);
													}
												}
											}).show();
							return false;
						}
					});
					paint.addView(as, lParams);
					isEnd = false;
				} else {
					if (isEnd) {
						Toast.makeText(AddItem.this, "请先添加开始",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(AddItem.this, "请移除结束",
								Toast.LENGTH_SHORT).show();
					}
				}

			}

		}
	}

	/**
	 * 
	 * 
	 * **/
	public void deleteLiuChengName(String itemname) {// itemname= liuchengname
		db = dbhelper.getWritableDatabase();
		db.execSQL("delete from linshi where  liuchengname='" + itemname + "';");
		db.close();
	}

	@SuppressWarnings("static-access")
	public boolean setLiuChengName(String itemname, String leixing,
			String oldname) {// 1
								// 插入
								// 2
								// 更新
		int i = 1;
		ContentValues cv = new ContentValues();
		cv.put("liuchengname", itemname);
		int id = getOldId(oldname);
		Cursor cursor = null;
		Log.i("getid", id + "");
		db = dbhelper.getReadableDatabase();
		cursor = db.rawQuery("select * from linshi where _id !=" + id
				+ " and liuchengname = '" + itemname + "'", null);
		if (!cursor.moveToNext()) {
			cursor = null;
			db = null;
			db = dbhelper.getWritableDatabase();
			String str = "select * from linshi where liuchengname ='" + oldname
					+ "';";

			cursor = db.rawQuery(str, null);
			while (cursor.moveToNext()) {

				i = 2;
				db.update("linshi", cv, "liuchengname=?", new String[] { cursor
						.getString(cursor.getColumnIndex("liuchengname")) });
				break;

			}
			if (i == 1) {

				cv.put("leixing", leixing);
				cv.put("start", calendar.HOUR_OF_DAY + "f" + calendar.MINUTE);
				db.insert("linshi", null, cv);

				i = 2;

			}
		} else {
			Toast.makeText(AddItem.this, "改日称已存在，请更换名称", Toast.LENGTH_SHORT)
					.show();
			cursor.close();
			db.close();
			return false;
		}
		if (editor.equals("xiugai")) {
			db.execSQL("update linshi set belongto = '" + belongto + "'");
		}
		// }
		cursor.close();
		db.close();
		return true;// true 更新 false 更换名称
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void addItem(String editor) {
		if (editor.equals("add")) {
			AddImage ai = new AddImage();
			SQLiteDatabase db;
			db = dbhelper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from linshi ", null);
			while (cursor.moveToNext()) {
				if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("start")) {
					ai.Add(0, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					isStart = false;
				} else if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("normal")) {
					String s = cursor.getString(cursor
							.getColumnIndex("leixing"));
					s = s.substring(s.indexOf("_") + 1);

					normalcount = Integer.parseInt(s);
					count++;
					ai.Add(1, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
				} else if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("important")) {

					ai.Add(2, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					String s = cursor.getString(cursor
							.getColumnIndex("leixing"));
					s = s.substring(s.indexOf("_") + 1);

					importantcount = Integer.parseInt(s);

					count++;
				} else if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("end")) {
					ai.Add(3, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					isEnd = false;
				}
			}
			cursor.close();
			db.close();
		} else if (editor.equals("xiugai")) {
			AddImage ai = new AddImage();
			SQLiteDatabase db;
			db = dbhelper.getWritableDatabase();
			// Log.i("xiugailiucheng", belongto);
			db.execSQL("insert into linshi ('liuchengname','leixing','itemcontent','waring','clock','start','end','belongto') select liuchengname,leixing,itemcontent,waring,clock,start,end,belongto from liucheng where belongto='"
					+ belongto + "'");

			db = dbhelper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from linshi ", null);
			// Cursor cursor = db.rawQuery(
			// "select * from liucheng where belongto = '" + belongto
			// + "'", null);
			while (cursor.moveToNext()) {
				if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("start")) {
					ai.Add(0, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					Log.i("xiugailiucheng", cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					isStart = false;
				} else if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("normal")) {
					String s = cursor.getString(cursor
							.getColumnIndex("leixing"));
					s = s.substring(s.indexOf("_") + 1);

					normalcount = Integer.parseInt(s);
					count++;
					ai.Add(1, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
				} else if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("important")) {

					ai.Add(2, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					String s = cursor.getString(cursor
							.getColumnIndex("leixing"));
					s = s.substring(s.indexOf("_") + 1);

					importantcount = Integer.parseInt(s);

					count++;
				} else if (cursor.getString(cursor.getColumnIndex("leixing"))
						.startsWith("end")) {
					ai.Add(3, cursor.getString(cursor
							.getColumnIndex("liuchengname")));
					isEnd = false;
				}
			}

			cursor.close();
			db.close();
		}

	}

	public int getOldId(String oldname) {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from linshi where liuchengname = '" + oldname + "'",
				null);
		if (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			return id;
		} else {
			return -1;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(AddItem.this, R.style.dialog)
					.setTitle("提示")
					.setMessage("确定放弃所有？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									SQLiteDatabase db = dbhelper
											.getWritableDatabase();
									db.execSQL("delete from linshi");
									db.close();
									itemid = 1;
									paint.removeAllViews();
									finish();

								}
							}).setNegativeButton("取消", null).show();

		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, Menu.FIRST, 0, "删除所有").setIcon(R.drawable.menu_deleteall);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST:
			new AlertDialog.Builder(AddItem.this, R.style.dialog)
					.setTitle("提示")
					.setMessage("您确定要删除所有？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									deleteAll();
								}
							}).setNegativeButton("取消", null).show();

			break;
		}

		return false;
	}

	private void deleteAll() {
		// TODO Auto-generated method stub
		db = dbhelper.getWritableDatabase();
		db.execSQL("delete from linshi");
		db.close();
		paint.removeAllViews();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}

}
