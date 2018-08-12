package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.Constant.*;
import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant.*;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant;
import com.example.xushuailong.mygrocerystore.scan.util.Validator;
import com.example.xushuailong.mygrocerystore.scan.util.WccConstant;


import java.util.List;

/**
 * accept extra: int-ScanType
 */
public class BarcodeInputActivity extends AppCompatActivity {
    private final static String TAG = "BarcodeInputActivity";
    private Context context = BarcodeInputActivity.this;
    private AutoCompleteTextView input;
    private TextView tvSwitch;
    private TextView tvCancel;
    private TextView tvCompare;
    private WccTabBar tabBar;
    private TextView inputInfo;

    private TextView inputColorInfo;
    private AutoCompleteTextView inputColor;
    private TextView tvCompareColor;
    private LinearLayout layout_color;
    private ImageView color_demo;
    private RelativeLayout button_layout;

    private String barcode_format;
    private List<String> historyBarcodes;
    private ArrayAdapter<String> keywordsAdapter;
    private int scanType;

    private int inputType = Constant.InputType.GOODS;
    //private int mSearchType = Constant.SearchKeyType.BARCODE_INPUT;
    private Handler handler;
    //	private boolean auto;
    private final static String ScanTypeGoods = "1";
    private final static String ScanTypeExpress = "2";
    private final static String ScanTypeDrugs = "3";
    private final static String ScanTypeColor = "4";
    private String curScanType = "0";
    private boolean hasColor = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.barcodeinput);
        Intent data = getIntent();
        scanType = data.getIntExtra("ScanType", Constant.ScanType.ALL);
        hasColor = data.getBooleanExtra("hasColor", false);
        findViews();
        setListeners();

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                try {
                    switch (msg.what) {
                        case MessageConstant.ACTIVITY_CLOSE:
                            finish();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        };
        barcode_format = ScanResult.BFORMAT_EAN13;
        input.setText("");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

//		if (QcStartupActivity.getQcMode(context)) {
//			input.addTextChangedListener(new TextWatcher() {
//				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//					auto = (charSequence != null && charSequence.length() == 11);
//				}
//	
//				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//					if (auto && charSequence.length() == 12 && barcode_format.equals(ScanResult.BFORMAT_EAN13)) {
//						input.setText(charSequence + getlastnumber(charSequence));
//						input.setSelection(13);
//					}
//				}
//	
//				public void afterTextChanged(Editable editable) {
//				}
//			});
//		}

//		historyBarcodes = DataBaseHelper.getInstance(context).getKeywordsData(mSearchType);
//		keywordsAdapter = new ArrayAdapter<String>(this, R.layout.historyitem, historyBarcodes);
//		input.setAdapter(keywordsAdapter);
        HardWare.getInstance(context).RegisterHandler(handler, hashCode());
    }

    private void findViews() {
        tvCompare = (TextView) findViewById(R.id.tv_compare);
        tvSwitch = (TextView) findViewById(R.id.tv_switchscan);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        inputInfo = (TextView) findViewById(R.id.input_info);
        tabBar = (WccTabBar) findViewById(R.id.tab_bar);
        input = (AutoCompleteTextView) findViewById(R.id.input);

        inputColorInfo = (TextView) findViewById(R.id.input_info_color);
        inputColor = (AutoCompleteTextView) findViewById(R.id.input_color);
        tvCompareColor = (TextView) findViewById(R.id.tv_compare_color);
        layout_color = (LinearLayout) findViewById(R.id.layout_color);
        color_demo = (ImageView) findViewById(R.id.color_demo);
        button_layout = (RelativeLayout) findViewById(R.id.button_layout);

//        if (ScanType.PRICETREND == scanType) {
//            tabBar.addTab("商品", -1, ScanTypeGoods, new MyTabClickListener(tabBar, ScanTypeGoods));
//        } else if (ScanType.EXP == scanType || ScanType.RETURNGOODSEXPRESS == scanType) {
//            tabBar.addTab("快递", -1, ScanTypeExpress, new MyTabClickListener(tabBar, ScanTypeExpress));
//        } else {
//            tabBar.addTab("商品", -1, ScanTypeGoods, new MyTabClickListener(tabBar, ScanTypeGoods));
//            tabBar.addTab("快递", -1, ScanTypeExpress, new MyTabClickListener(tabBar, ScanTypeExpress));
//            tabBar.addTab("药品码", -1, ScanTypeDrugs, new MyTabClickListener(tabBar, ScanTypeDrugs));
//            if (hasColor)
//                tabBar.addTab("彩虹码", -1, ScanTypeColor, new MyTabClickListener(tabBar, ScanTypeColor));
//        }
//
//        if (ScanType.EXP == scanType || ScanType.RETURNGOODSEXPRESS == scanType) {
//            if (WccConstant.DEBUG) Log.e(TAG, "Constant.ScanType.EXP == scanType");
//            tabBar.setFillTabDone(ScanTypeExpress);
//        } else {
//            if (WccConfigure.getColorMode(context) && hasColor)
//                tabBar.setFillTabDone(ScanTypeColor);
//            else
//                tabBar.setFillTabDone(ScanTypeGoods);
//
//        }
        // 强生App只展示彩虹码输入
        if (hasColor)
            tabBar.addTab("彩虹码", -1, ScanTypeColor, new MyTabClickListener(tabBar, ScanTypeColor));
        tabBar.setFillTabDone(ScanTypeColor);
    }

    private void setListeners() {
        tvCompare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                closeSoftkb();
                CharSequence value = "";

                if (input.getText() != null)
                    value = input.getText();
                if (value.length() == 0) {
                    HardWare.ToastShort(context, getResources().getString(R.string.barinput_empty));
                    return;
                }

                if (inputType == Constant.InputType.GOODS || inputType == Constant.InputType.DRUG) {
                    if (value.length() == 8)
                        barcode_format = ScanResult.BFORMAT_EAN8;
                    else if (value.length() == 13)
                        barcode_format = ScanResult.BFORMAT_EAN13;
                    else if (value.length() == 12)
                        barcode_format = ScanResult.BFORMAT_UPC12;
                    else if (value.length() != 4 && value.length() != 6) {
                        //invalidLength();
                        startCompareInvalidBarcode(value.toString());
                        return;
                    }
                } else if (inputType == Constant.InputType.EXPRESS) {
                    if (value.length() > 20 || value.length() < 7) {
                        invalidLength();
                        return;
                    } else {
                        barcode_format = ScanResult.BFORMAT_EXPRESS;
                    }
                }

                String str = value.toString();
                int strLen = str.length();
                /*商品条码只接受数字，快递和药品接受数字或字母*/
                if (inputType == Constant.InputType.GOODS || inputType == Constant.InputType.DRUG) {
                    if (!Validator.IsNumber(str)) {
                        HardWare.ToastShort(context, "当前输入条码只接受数字!请检查~");
                        return;
                    }
                } else {
                    if (!Validator.IsLettersOrNumbers(str)) {
                        HardWare.ToastShort(context, "当前输入条码只接受数字或字母!请检查~");
                        return;
                    }
                }

				/*UPC规则，输入12位数字后，在最前面加0*/
                if (barcode_format == ScanResult.BFORMAT_UPC12 || barcode_format == ScanResult.BFORMAT_UPC8) {
                    if (strLen == 12 || strLen == 8) {
                        if (strLen == 12) {
                            str = "0" + str;
                            // 补0后条码格式改为BFORMAT_EAN13
                            barcode_format = ScanResult.BFORMAT_EAN13;
                        }
                        /*strLen = str.length();
                        c1 = str.charAt(strLen - 1) + "";
						c2 = getlastnumber(str.substring(0, strLen-1));*/
                    }
                }


//				if (DataBaseHelper.getInstance(context).hasKeyword(str, mSearchType) == false) {
//					if (WccConstant.DEBUG) Log.e(TAG, "to record number:" + str);
//					DataBaseHelper.getInstance(context).addKeywords(str, mSearchType);
//					historyBarcodes = DataBaseHelper.getInstance(context).getKeywordsData(mSearchType);
//                    keywordsAdapter = new ArrayAdapter<String>(context,R.layout.historyitem, historyBarcodes);
//					input.setAdapter(keywordsAdapter);
//				}

                switch (scanType) {
                    case ScanType.PRICETREND:
//						Intent intent = new Intent();
//	                	intent.putExtra(PriceTrendFragment.Barcode, str);
//	                	intent.putExtra(PriceTrendFragment.Format, barcode_format);
//	                	HardWare.getInstance(context).sendMessage(BarcodeDecodeMsg.ScanResult, intent);
//	                	HardWare.sendMessage(BarcodeScanActivity.getMainHandler(), MessageConstant.ACTIVITY_CLOSE, intent);
//	                	finish();
                        break;
                    default:
                        HardWare.sendMessage(BarcodeScanActivity.getMainHandler(), MessageConstant.ACTIVITY_CLOSE);
                        DecodeResultProcessor processor = new DecodeResultProcessor(BarcodeInputActivity.this);
                        processor.process("", str, barcode_format, null, inputType);
                        finish();
                        break;
                }
            }
        });

        tvCompareColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WccConstant.DEBUG)
                    Log.v(TAG, "btnConfirm closeSoftkb");
                closeSoftkb();
                CharSequence value = "";

                if (input.getText() != null)
                    value = input.getText();
                if (value.length() == 0) {
                    HardWare.ToastShort(context, "请输入条码");
                    return;
                }
                if (value.length() != 13) {
                    invalidLength();
                    return;
                }

                String str = value.toString();
                if (!Validator.IsNumber(str)) {
                    HardWare.ToastShort(context, "当前输入条码只接受数字!请检查~");
                    return;
                }

//				if (DataBaseHelper.getInstance(context).hasKeyword(str, mSearchType) == false) {
//					if (WccConstant.DEBUG) Log.e(TAG, "to record number:" + str);
//					DataBaseHelper.getInstance(context).addKeywords(str, mSearchType);
//					historyBarcodes = DataBaseHelper.getInstance(context).getKeywordsData(mSearchType);
//                    keywordsAdapter = new ArrayAdapter<String>(context, R.layout.historyitem, historyBarcodes);
//					input.setAdapter(keywordsAdapter);
//				}

                CharSequence colorValue = "";

                if (inputColor.getText() != null)
                    colorValue = inputColor.getText();
                if (colorValue.length() == 0) {
                    HardWare.ToastShort(context, "请输入彩虹码");
                    return;
                }

                String color = colorValue.toString();
                if (!Validator.IsNumber(color)) {
                    HardWare.ToastShort(context, "当前输入条码只接受数字!请检查~");
                    return;
                }

                HardWare.sendMessage(BarcodeScanActivity.getMainHandler(), MessageConstant.ACTIVITY_CLOSE);
//                DecodeResultProcessor processor = new DecodeResultProcessor(BarcodeInputActivity.this);
//                processor.process(color, str, barcode_format, null, inputType);

                if (WccConstant.DEBUG) {
                    HardWare.ToastShort(BarcodeInputActivity.this, "barcode:" + str + "\nColorCode:" + color);
                }

                /**
                 * 销售商品时，手输彩虹码跳转商品详情页
                 */
                //Todo
//                Intent intent = new Intent(BarcodeInputActivity.this, GoodsDetailActivity.class);
//                intent.putExtra("Barcode", str);
//                intent.putExtra("ColorCode", color);
//                startActivity(intent);
//                finish();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HardWare.sendMessage(BarcodeScanActivity.getMainHandler(), MessageConstant.ACTIVITY_CLOSE);
                if (WccConstant.DEBUG) {
                    Log.v(TAG, "lLCancel closeSoftkb");
                }
                closeSoftkb();
                finish();
            }
        });

        tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (WccConstant.DEBUG) {
                    Log.v(TAG, "lLScan closeSoftkb");
                }
                closeSoftkb();
                Intent intent = new Intent(context, BarcodeScanActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ScanType", scanType);
                startActivity(intent);

                finish();
            }
        });
    }

    private void invalidLength() {
        HardWare.ToastShort(context, "对应条码格式输入长度有误!请检查~");
    }

    private void startCompareInvalidBarcode(String barcode) {
//		HardWare.sendMessage(BarcodeScanActivity.getMainHandler(), MessageConstant.ACTIVITY_CLOSE);
//		Intent intent;
//		intent = new Intent(context, CommodityCompareResultActivity.class);
//		intent.putExtra(ScanResult.kScanResult, barcode);
//		intent.putExtra("isValidBarcode", false);
//
//		if(DataBaseHelper.getInstance(context).hasKeyword(barcode, mSearchType) == false) {
//			if(WccConstant.DEBUG) Log.e(TAG, "to record number:" + barcode);
//			DataBaseHelper.getInstance(context).addKeywords(barcode, mSearchType);
//			historyBarcodes = DataBaseHelper.getInstance(context).getKeywordsData(mSearchType);
//            keywordsAdapter = new ArrayAdapter<String>(context,R.layout.historyitem, historyBarcodes);
//			input.setAdapter(keywordsAdapter);
//		}
//
//		startActivity(intent);
    }

    protected void onDestroy() {
        try {
            super.onDestroy();
            HardWare.getInstance(context).UnRegisterHandler(hashCode());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void closeSoftkb() {
        try {
            //	inputMethod.hideSoftInputFromWindow(input.getWindowToken(), 0);
        } catch (Exception e) {
            if (WccConstant.DEBUG) {
                Log.v(TAG, "catch lLScan closeSoftkb");
            }
            e.printStackTrace();
        }
    }

//	private static String getlastnumber(CharSequence s) {
//		int prenum = 0;
//		if (s.length() == 7) {
//			s = "00000" + s;
//		}
//		for (int i = 0; i < 11; i += 2) {
//			prenum += isNumeric(s.charAt(i));
//		}
//		int lastNum = 0;
//		for (int i = 1; i < 12; i += 2) {
//			lastNum += isNumeric(s.charAt(i));
//		}
//		return Integer.toString((10 - (prenum + lastNum * 3) % 10) % 10);
//	}

    public static int isNumeric(Character c) {
        return Character.isDigit(c) ? Integer.valueOf(Character.toString(c)) : 0;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class MyTabClickListener extends WccTabBar.TabClickListener {

        protected MyTabClickListener(WccTabBar wccTabBar, String TAG) {
            wccTabBar.super(TAG);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void showContent(String Tag) {
            // TODO Auto-generated method stub
            if (curScanType.equals(Tag)) {
                return;
            }
            curScanType = Tag;
            if (ScanTypeGoods.equals(Tag)) {
                input.setText("");
                inputInfo.setText("请输入您要查询的商品条码:");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                inputType = Constant.InputType.GOODS;

                tvCompare.setVisibility(View.VISIBLE);
                layout_color.setVisibility(View.GONE);
                color_demo.setVisibility(View.GONE);

                LayoutParams lp = (LayoutParams) button_layout.getLayoutParams();
                lp.topMargin = HardWare.dip2px(context, 100);
                button_layout.setLayoutParams(lp);
            } else if (ScanTypeExpress.equals(Tag)) {
                //原来的逻辑代码暂时保留
//				input.setText("");
//				inputInfo.setText("请输入需要查询的快递条码:");
//				input.setInputType(InputType.TYPE_CLASS_TEXT);
//				input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
//				inputType = Constant.InputType.EXPRESS;
                //V8.2跳快递查询页面
//				Intent intent = new Intent(BarcodeInputActivity.this, ExpressMainActivity.class);
//				startActivity(intent);
//				finish();
            } else if (ScanTypeDrugs.equals(Tag)) {
                input.setText("");
                inputInfo.setText("请输入您要查询的药品码:");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                inputType = Constant.InputType.DRUG;

                tvCompare.setVisibility(View.VISIBLE);
                layout_color.setVisibility(View.GONE);
                color_demo.setVisibility(View.GONE);

                LayoutParams lp = (LayoutParams) button_layout.getLayoutParams();
                lp.topMargin = HardWare.dip2px(context, 100);
                button_layout.setLayoutParams(lp);
            } else if (ScanTypeColor.equals(Tag)) {
                input.setText("");
                inputInfo.setText("请输入您要查询的商品条码:");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                inputType = Constant.InputType.COLOR;

                tvCompare.setVisibility(View.GONE);
                layout_color.setVisibility(View.VISIBLE);
                color_demo.setVisibility(View.VISIBLE);
                color_demo.setImageResource(R.drawable.color_demo);

                inputColor.setText("");
                inputColorInfo.setText("请输入您要查询的商品的彩虹码:");
                inputColor.setInputType(InputType.TYPE_CLASS_NUMBER);

                LayoutParams lp = (LayoutParams) button_layout.getLayoutParams();
                lp.topMargin = HardWare.dip2px(context, 10);
                button_layout.setLayoutParams(lp);
            }

        }

    }

}
