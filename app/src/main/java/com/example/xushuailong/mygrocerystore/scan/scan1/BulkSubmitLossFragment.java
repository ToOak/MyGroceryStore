package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.scan.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量盘点盘损
 */
public class BulkSubmitLossFragment extends BaseFragment implements View.OnClickListener, BarcodeScan2Activity.OnScanCompleteListener {

    //任务
    public static final String CHECK_TYPE_TASK = "check_type_task";
    //入库
    public static final String CHECK_TYPE_STORAGE = "check_type_storage";
    //单SKU盘点
    public static final String CHECK_TYPE_SKU = "check_type_single_sku";
    public static final String KEY_CHECK_TYPE = "check_type";
    //物流码或盘存任务编号
    private static final String KEY_ID = "key_id";
    private static final String KEY_GOODS_NAME = "key_goods_name";
    private static final String KEY_GOODS_SPEC = "key_goods_spec";
    private static final String KEY_GOODS_BARCODE = "key_goods_barcode";
    private static final String KEY_TARGET = "key_target";
    private static final String KEY_TARGET_COUNT = "key_target_count";
    private static final String KEY_PKID = "key_pkid";

    @StringDef({CHECK_TYPE_TASK, CHECK_TYPE_STORAGE, CHECK_TYPE_SKU})
    public @interface CheckType {
    }

    private String lossType, id, goodsName, goodsSpec, targetBarcode, target = "", targetCount, pkid;
    private int targetCt;
    private List<LossGoodsInfo> lossGoodsInfoList;
    private ListView lvLoss;
    private BulkSubmitLossAdapter mAdapter;

    private TextView tvId, tvTarget, tvRealCount, tvTotalCount;
    private Button btnCheckLoss, btnSubmitLoss;
    private LinearLayout lLBottomTask;
    private Button btnBottomStorage;

    private String reqKey;
    private ProgressDialog mProgressDialog;

    private int curPressedBtn;

    public BulkSubmitLossFragment() {
    }

    public static BulkSubmitLossFragment newInstance(@CheckType String lossType, String id, String goodsName, String goodsSpec, String barcode, String pkid, String target, String targetCount) {
        BulkSubmitLossFragment fragment = new BulkSubmitLossFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CHECK_TYPE, lossType);
        args.putString(KEY_ID, id);
        args.putString(KEY_GOODS_NAME, goodsName);
        args.putString(KEY_GOODS_SPEC, goodsSpec);
        args.putString(KEY_GOODS_BARCODE, barcode);
        args.putString(KEY_TARGET, target);
        args.putString(KEY_TARGET_COUNT, targetCount);
        args.putString(KEY_PKID, pkid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lossType = getArguments().getString(KEY_CHECK_TYPE);
            id = getArguments().getString(KEY_ID);
            goodsName = getArguments().getString(KEY_GOODS_NAME);
            goodsSpec = getArguments().getString(KEY_GOODS_SPEC);
            targetBarcode = getArguments().getString(KEY_GOODS_BARCODE);
            target = getArguments().getString(KEY_TARGET);
            targetCount = getArguments().getString(KEY_TARGET_COUNT);
            pkid = getArguments().getString(KEY_PKID);
            if (CHECK_TYPE_TASK.equals(lossType)||CHECK_TYPE_SKU.equals(lossType)) {
                try {
                    targetCt = Integer.parseInt(targetCount);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bulk_submit_loss, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void findViews(View view) {
        tvId = (TextView) view.findViewById(R.id.tv_task_id);
        tvTarget = (TextView) view.findViewById(R.id.tv_target);
        tvRealCount = (TextView) view.findViewById(R.id.tv_real_count);
        tvTotalCount = (TextView) view.findViewById(R.id.tv_total_count);
        btnCheckLoss = (Button) view.findViewById(R.id.btn_check_loss);
        btnSubmitLoss = (Button) view.findViewById(R.id.btn_submit_loss);
        lLBottomTask = (LinearLayout) view.findViewById(R.id.lL_bottom_task);
        btnBottomStorage = (Button) view.findViewById(R.id.btn_bottom_storage);
        lvLoss = (ListView) view.findViewById(R.id.lv_scan_list);
    }

    @Override
    public void setListeners() {
        btnCheckLoss.setOnClickListener(this);
        btnSubmitLoss.setOnClickListener(this);
        btnBottomStorage.setOnClickListener(this);
    }

    @Override
    public void initData() {
        reqKey = hashCode() + "";
        mProgressDialog = DialogUtil.getProgressDialog(mContext);

        lossGoodsInfoList = new ArrayList<>();
        mAdapter = new BulkSubmitLossAdapter();
        lvLoss.setAdapter(mAdapter);

        switch (lossType) {
            case CHECK_TYPE_TASK:
                lLBottomTask.setVisibility(View.VISIBLE);
                btnBottomStorage.setVisibility(View.GONE);
                break;
            case CHECK_TYPE_STORAGE:
                lLBottomTask.setVisibility(View.GONE);
                btnBottomStorage.setVisibility(View.VISIBLE);
                break;
            case CHECK_TYPE_SKU:
                btnCheckLoss.setText("提交盘点结果");
                tvId.setVisibility(View.GONE);
                tvTarget.setVisibility(View.GONE);
                break;
        }
        tvId.setText("盘点任务编号：" + id);
        tvTarget.setText("盘点目标：" + target);
        tvTotalCount.setText("应有库存：" + targetCount + "件");
    }

    @Override
    public void onClick(View v) {
        curPressedBtn = v.getId();
        switch (v.getId()) {
//            case R.id.btn_check_loss:
//                if (CHECK_TYPE_TASK.equals(lossType)) {
//                    showSubmitLossDialog("确认提交报损？");
//                } else if (CHECK_TYPE_SKU.equals(lossType)) {
//                    showSubmitLossDialog("确认提交盘点结果？");
//                }
//                break;
//            case R.id.btn_submit_loss:
//                showSubmitLossDialog("确认提交盘点任务？");
//                break;
//            case R.id.btn_bottom_storage:
//                showSubmitLossDialog("确认提交报损？");
//                break;
        }
    }

//    private void showSubmitLossDialog(String s) {
//        new AlertDialog.Builder(mContext).setTitle("提示")
//                .setMessage(s)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        submitLossGoods();
//                    }
//                }).show();
//    }

//    private void submitLossGoods() {
//        if (ValidatorUtil.isFastDoubleClick())
//            return;
//
//        if (lossGoodsInfoList == null)
//            return;
//
//        setBtnEnable(false);
//        MapArgs mapArgs = new MapArgs();
//        mapArgs.put("ReqKey", reqKey);// 必填参数
//        mapArgs.put("ReqType", ReqType.BulkSubmitLoss);// 必填参数
//        mapArgs.put("pkid", pkid);
//        mapArgs.put("items", getLossList());
//        switch (lossType) {
//            case CHECK_TYPE_TASK:
//                mapArgs.put("type", "TASKANALYSIS");
//                break;
//            case CHECK_TYPE_STORAGE:
//                mapArgs.put("type", "TRACEANALYSIS");
//                break;
//            case CHECK_TYPE_SKU:
//                mapArgs.put("type", "SKUANALYSIS");
//                break;
//        }
//        mapArgs.put("id", id);
//        DataFetcher.getInstance().requestData(mHandler, mapArgs, true);
//    }

//    private void setBtnEnable(boolean enable) {
//        btnSubmitLoss.setEnabled(enable);
//        btnCheckLoss.setEnabled(enable);
//        btnBottomStorage.setEnabled(enable);
//    }

//    private String getLossList() {
//        if (lossGoodsInfoList.size() == 0) {
//            return "[]";
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        for (int i = 0; i < lossGoodsInfoList.size(); i++) {
//            sb.append(lossGoodsInfoList.get(i).toJsonItem());
//            sb.append(",");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append("]");
//        return sb.toString();
//    }
//
    @Override
    public void submitLossScanSuccess(String barcode, String rainbowCode) {
//        if (!barcode.equals(targetBarcode)) {
//            JCToast.getInstance(getContext()).toastShort( "条码不匹配！");
//            return;
//        }
//        if (lossGoodsInfoList.size() == 0) {
//            lossGoodsInfoList.add(0, new LossGoodsInfo(barcode, rainbowCode, VeDateUtil.getCurDayTime(), pkid));
//        } else {
//            for (int i = 0; i < lossGoodsInfoList.size(); i++) {
//                if (rainbowCode.equals(lossGoodsInfoList.get(i).getRainbowCode())) {
//                    HardWare.ToastShort(mContext, "该商品已添加,请勿重复扫描！");
//                    break;
//                }
//                if (i == lossGoodsInfoList.size() - 1) {
//                    lossGoodsInfoList.add(0, new LossGoodsInfo(barcode, rainbowCode, VeDateUtil.getCurDayTime(), pkid));
//                    break;
//                }
//            }
//        }
//        mAdapter.notifyDataSetChanged();
//        if (CHECK_TYPE_TASK.equals(lossType)) {
//            tvRealCount.setText(lossGoodsInfoList.size() + "件");
//            if (lossGoodsInfoList.size() == targetCt) {
//                btnCheckLoss.setVisibility(View.GONE);
//                btnSubmitLoss.setVisibility(View.VISIBLE);
//            } else {
//                btnCheckLoss.setVisibility(View.VISIBLE);
//                btnSubmitLoss.setVisibility(View.GONE);
//            }
//        } else if (CHECK_TYPE_SKU.equals(lossType)) {
//            tvRealCount.setText(lossGoodsInfoList.size() + "件");
//        }
    }

    //todo
    WeakHandler mHandler = new WeakHandler(mContext) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case ReqType.BulkSubmitLoss:
//                    setBtnEnable(true);
//                    try {
//                        BulkLossInfo bulkLossInfo = (BulkLossInfo) msg.obj;
//                        if ("OK".equals(bulkLossInfo.getIsokType())) {
//                            switch (curPressedBtn) {
//                                case R.id.btn_submit_loss:
//                                    showCheckList();
//                                    break;
//                                case R.id.btn_check_loss:
//                                case R.id.btn_bottom_storage:
//                                    showSubmitLossActivity(curPressedBtn, bulkLossInfo.getAnalysisId());
//                                    break;
//                            }
//                        } else {
//                            ToastUtil.toastShort(mContext, bulkLossInfo.getMsg(mContext));
//                        }
//                    } catch (Exception e) {
//                    }
//                    break;
//                case ReqType.OpenDialog:
//                    if (!mContext.isFinishing()) {
//                        DialogUtil.show(mProgressDialog);
//                    }
//                    break;
//                case ReqType.CloseDialog:
//                    DialogUtil.close(mProgressDialog);
//                    break;
            }
        }
    };

//    private void showSubmitLossActivity(int curPressedBtn, String analysisId) {
//        switch (curPressedBtn) {
//            case R.id.btn_check_loss:
//                if (CHECK_TYPE_TASK.equals(lossType)) {
//                    HybridUtil.startHybridActivity(mContext, JCConstant.HOST_WEBVIEW + "checkfaulty?"
//                            + "staff_id=" + SpUtil.getString(mContext, "STAFF_ID", "")
//                            + "&store_id=" + SpUtil.getString(mContext, "STORE_ID", "")
//                            + "&udid=" + CommonUtil.getUdid(mContext)
//                            + "&token=" + SpUtil.getToken(mContext)
//                            + "&analysis_id=" + analysisId
//                            + "&barcode=" + targetBarcode
//                            + "&pkid=" + pkid
//                            + "&h5title=" + DataConverterUtil.urlEncode("损耗商品提交"));
//                    mContext.finish();
//                } else if (CHECK_TYPE_SKU.equals(lossType)) {
//                    HybridUtil.startHybridActivity(mContext, JCConstant.HOST_WEBVIEW + "checkfaulty?"
//                            + "staff_id=" + SpUtil.getString(mContext, "STAFF_ID", "")
//                            + "&store_id=" + SpUtil.getString(mContext, "STORE_ID", "")
//                            + "&udid=" + CommonUtil.getUdid(mContext)
//                            + "&token=" + SpUtil.getToken(mContext)
//                            + "&analysis_id=" + analysisId
//                            + "&barcode=" + targetBarcode
//                            + "&pkid=" + pkid
//                            + "&h5title=" + DataConverterUtil.urlEncode("损耗商品提交"));
//                    mContext.finish();
//                }
//                break;
//            case R.id.btn_bottom_storage:
//                HybridUtil.startHybridActivity(mContext, JCConstant.HOST_WEBVIEW + "checkfaulty?"
//                        + "staff_id=" + SpUtil.getString(mContext, "STAFF_ID", "")
//                        + "&store_id=" + SpUtil.getString(mContext, "STORE_ID", "")
//                        + "&udid=" + CommonUtil.getUdid(mContext)
//                        + "&token=" + SpUtil.getToken(mContext)
//                        + "&logistics_code=" + id
//                        + "&analysis_id=" + analysisId
//                        + "&barcode=" + targetBarcode
//                        + "&pkid=" + pkid
//                        + "&h5title=" + DataConverterUtil.urlEncode("损耗商品提交"));
//                mContext.finish();
//                break;
//        }
//    }
//
//
//    private void showCheckList() {
//        HybridUtil.startHybridActivity(mContext, JCConstant.HOST_WEBVIEW + "checklist?"
//                + "staff_id=" + SpUtil.getString(mContext, "STAFF_ID", "")
//                + "&store_id=" + SpUtil.getString(mContext, "STORE_ID", "")
//                + "&udid=" + CommonUtil.getUdid(mContext)
//                + "&token=" + SpUtil.getToken(mContext)
//                + "&h5title=" + DataConverterUtil.urlEncode("存货盘点"));
//        mContext.finish();
//    }

    private class BulkSubmitLossAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lossGoodsInfoList == null ? 0 : lossGoodsInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return lossGoodsInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_lv_bulk_submit_loss, null);
                holder = new ViewHolder();
                holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
                holder.tvGoodsSpec = (TextView) convertView.findViewById(R.id.tv_specification);
                holder.tvRainbowCode = (TextView) convertView.findViewById(R.id.tv_rainbow_code);
                holder.tvScanTime = (TextView) convertView.findViewById(R.id.tv_scan_time);
                holder.lLItem = (LinearLayout) convertView.findViewById(R.id.lL_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            LossGoodsInfo bean = lossGoodsInfoList.get(position);
            holder.tvGoodsName.setText(goodsName);
            holder.tvGoodsSpec.setText(goodsSpec);
            holder.tvRainbowCode.setText(bean.getRainbowCode());
            holder.tvScanTime.setText(bean.getScanTime());

            if (position % 2 == 0) {
                holder.lLItem.setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                holder.lLItem.setBackgroundColor(getResources().getColor(R.color.wcc_color_22));
            }

            return convertView;
        }

        class ViewHolder {
            LinearLayout lLItem;
            TextView tvGoodsName, tvGoodsSpec, tvRainbowCode, tvScanTime;
        }
    }

    class LossGoodsInfo {
        private String barcode;
        private String rainbowCode;
        private String scanTime;
        private String pkid;

        public LossGoodsInfo(String barcode, String rainbowCode, String scanTime, String pkid) {
            this.barcode = barcode;
            this.rainbowCode = rainbowCode;
            this.scanTime = scanTime;
            this.pkid = pkid;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getRainbowCode() {
            return rainbowCode;
        }

        public void setRainbowCode(String rainbowCode) {
            this.rainbowCode = rainbowCode;
        }

        public String getScanTime() {
            return scanTime;
        }

        public void setScanTime(String scanTime) {
            this.scanTime = scanTime;
        }

        public String toJsonItem() {
            return "{\"barcode\":\"" + barcode + "\",\"rainbow_code\":\"" + rainbowCode + "\",\"pkid\":\"" + pkid + "\"}";
//            return "{\"barcode\":\"" + barcode + "\",\"rainbow_code\":\"" + rainbowCode + "\"}";
        }
    }
}
