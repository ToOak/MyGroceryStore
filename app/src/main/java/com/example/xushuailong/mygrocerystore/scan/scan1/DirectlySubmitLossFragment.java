package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.scan.util.DialogUtil;

import java.util.ArrayList;
import com.example.xushuailong.mygrocerystore.R;
import java.util.List;

/**
 * 直接报损
 */
public class DirectlySubmitLossFragment extends BaseFragment implements BarcodeScan2Activity.OnScanCompleteListener, View.OnClickListener {

    public static final int RESULTCODE_SSUCCESS = 100;

    private Button btnSubmitLoss;
    private ListView lvLossGoods;
//    private DirectlySubmitLossAdapter mAdapter;
    private String reqKey;
//    private ReasonsInfo mReasonsInfo;
//    private List<GoodsInfo> mLossGoodsList;
    private TextView tv_consume_reason;
    private boolean hasLossReasons = false;
    private ProgressDialog mProgressDialog;
//    private DirectlySubmitLossSuccessDialog mSuccessDialog;
    private String curRequestRainbowCode = "";

    public static DirectlySubmitLossFragment newInstance() {
        DirectlySubmitLossFragment fragment = new DirectlySubmitLossFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void findViews(View view) {
        btnSubmitLoss = (Button) view.findViewById(R.id.btn_submit_loss);
        lvLossGoods = (ListView) view.findViewById(R.id.lv_loss_goods);
    }

    @Override
    public void setListeners() {
        btnSubmitLoss.setOnClickListener(this);
    }

    @Override
    public void initData() {
        reqKey = hashCode() + "";
        mProgressDialog = DialogUtil.getProgressDialog(mContext);
//        mReasonsInfo = new ReasonsInfo();
//        List<ReasonsInfo.LossReasonBean> reasons = new ArrayList<>();
//        reasons.add(new ReasonsInfo().new LossReasonBean(0, ""));
//        reasons.add(new ReasonsInfo().new LossReasonBean(-1, "请选择损耗原因"));
//        mReasonsInfo.setLossReasonList(reasons);

        getLossReasonList();
//        mLossGoodsList = new ArrayList<>();
//        mAdapter = new DirectlySubmitLossAdapter();
//        lvLossGoods.setAdapter(mAdapter);

        //TODO 测试数据
//        testData();
    }

    private void testData() {

        for (int i = 0; i < 3; i++) {
//            mLossGoodsList.add(new GoodsInfo("大宝萨根发挥广发华福" + i + "号", "23598273" + i, "16248761123" + i, "2016-11-25 09:59:50", "2016-11-25 09:59:57"));
        }

    }

    /**
     * 获取报损原因列表
     */
    private void getLossReasonList() {
//        MapArgs mapArgs = new MapArgs();
//        mapArgs.put("ReqKey", reqKey);// 必填参数
//        mapArgs.put("ReqType", ReqType.LossReason);// 必填参数
//        mapArgs.put("Type","REPORTEDLOSS");
//        DataFetcher.getInstance().requestData(mHandler, mapArgs, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_directly_submit_loss, container, false);
    }

    @Override
    public void submitLossScanSuccess(String barcode, String rainbowCode) {
//
//        if (!hasLossReasons) {
//            getLossReasonList();
//        }
//        if (rainbowCode.equals(curRequestRainbowCode))
//            return;
//        curRequestRainbowCode = rainbowCode;
//        MapArgs mapArgs = new MapArgs();
//        mapArgs.put("ReqKey", reqKey);// 必填参数
//        mapArgs.put("ReqType", ReqType.UserGoodsInfo);// 必填参数
//        mapArgs.put("Barcode", barcode);
//        mapArgs.put("RainbowCode", rainbowCode);
//        DataFetcher.getInstance().requestData(mHandler, mapArgs, false);
    }

    WeakHandler mHandler = new WeakHandler(mContext) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case ReqType.LossReason:
//                    try {
//                        SparseArray<Object> resResult = (SparseArray<Object>) msg.obj;
//                        ReasonsInfo reasonInfo = (ReasonsInfo) resResult.get(0);
//                        if ("OK".equals(reasonInfo.getIsokType()) && !hasLossReasons) {
//                            hasLossReasons = true;
//                            mReasonsInfo.getLossReasonList().remove(0);
//                            for (int i = 0; i < reasonInfo.getLossReasonList().size(); i++) {
//                                mReasonsInfo.getLossReasonList().add(i, reasonInfo.getLossReasonList().get(i));
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        } else {
//                            ToastUtil.toastShort(mContext, reasonInfo.getMsg(mContext));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case ReqType.UserGoodsInfo:
//                    try {
//                        SparseArray<Object> resResult = (SparseArray<Object>) msg.obj;
//                        GoodsInfo goodsInfo = (GoodsInfo) resResult.get(1);
//                        if ("OK".equals(goodsInfo.getIsokType())) {
//                            addLossGoods(goodsInfo);
//                        } else {
//                            curRequestRainbowCode = "";
//                            ToastUtil.toastShort(mContext, goodsInfo.getMsg(mContext));
//                        }
//                    } catch (Exception e) {
//                        curRequestRainbowCode = "";
//                    }
//                    break;
//                case ReqType.DirectlySubmitLoss:
//                    try {
//                        curRequestRainbowCode = "";
//                        SimpleInfo simpleInfo = (SimpleInfo) msg.obj;
//                        if ("OK".equals(simpleInfo.getIsokType())) {
//                            showSubmitLossSuccessDialog();
//                        } else {
//                            ToastUtil.toastShort(mContext, simpleInfo.getMsg(mContext));
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

    /**
     * 显示报损成功的dialog
     */
//    private void showSubmitLossSuccessDialog() {
//        mSuccessDialog = new DirectlySubmitLossSuccessDialog(mContext, R.style.dialog);
//        mSuccessDialog.show();
//        Window dialogWindow = mSuccessDialog.getWindow();
//        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        mSuccessDialog.setCancleOnTouch();
//        mSuccessDialog.setOnViewClickListener(new DirectlySubmitLossSuccessDialog.OnViewClickListener() {
//            @Override
//            public void closeClickListener() {
//                showCheckList();
//            }
//
//            @Override
//            public void toCheckListClickListener() {
//                showCheckList();
//            }
//        });
//    }

//    private void showCheckList() {
//        HybridUtil.startHybridActivity(mContext, JCConstant.HOST_WEBVIEW + "goodsstock?"
//                + "staff_id=" + SpUtil.getString(mContext, "STAFF_ID", "")
//                + "&store_id=" + SpUtil.getString(mContext, "STORE_ID", "")
//                + "&udid=" + CommonUtil.getUdid(mContext)
//                + "&token=" + SpUtil.getToken(mContext)
//                + "&h5title=" + DataConverterUtil.urlEncode("库存报表"));
//        mContext.setResult(RESULTCODE_SSUCCESS);
//        mContext.finish();
//        mSuccessDialog.dismiss();
//    }

    /**
     * 提交报损
     */
//    private void submitLoss() {
//        if (ValidatorUtil.isFastDoubleClick())
//            return;
//
//        if (mLossGoodsList == null || mLossGoodsList.size() == 0)
//            return;
//        for (int i = 0; i < mLossGoodsList.size(); i++) {
//            if (mLossGoodsList.get(i).getLossReasonPos() < 0 || mLossGoodsList.get(i).getLossReasonPos() >= mReasonsInfo.getLossReasonList().size() - 1) {
//                ToastUtil.toastShort(mContext, "请选择第" + (i + 1) + "件商品报损原因");
//                return;
//            }
//        }
//
//        MapArgs mapArgs = new MapArgs();
//        mapArgs.put("ReqKey", reqKey);// 必填参数
//        mapArgs.put("ReqType", ReqType.DirectlySubmitLoss);// 必填参数
//        mapArgs.put("items", getLossList());
//        mapArgs.put("type", "REPORTEDLOSS");
//        DataFetcher.getInstance().requestData(mHandler, mapArgs, true);
//    }

    /**
     * 生成报损商品列表的JSON字符串
     *
     * @return
     */
//    private String getLossList() {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        for (int i = 0; i < mLossGoodsList.size(); i++) {
//            sb.append(mLossGoodsList.get(i).toJsonItem(mReasonsInfo.getLossReasonList()
//                    .get(mLossGoodsList.get(i).getLossReasonPos()).getReasonId()));
//            sb.append(",");
//        }
//        if (sb.length()>1) {
//            sb.deleteCharAt(sb.length() - 1);
//        }
//        sb.append("]");
//        return sb.toString();
//    }

    /**
     * 报损列表添加商品
     *
//     * @param goodsInfo
     */
//    private void addLossGoods(GoodsInfo goodsInfo) {
//        if (!"OK".equals(goodsInfo.getIsokType()))
//            return;
//
//        switch (goodsInfo.getStatus()) {
//            case "SALE":
//                break;
//            case "SOLD":
//                HardWare.ToastShort(mContext, "当前商品已售出，只有在售的商品才可以报损!");
//                return;
//            case "LOSS":
//            case "DCMSOLD":
//                HardWare.ToastShort(mContext, "当前商品已损耗，只有在售的商品才可以报损!");
//                break;
//            case "OFFLINESOLD":
//                HardWare.ToastShort(mContext, "当前商品线下已销售，只有在售的商品才可以报损!");
//                return;
//            default:
//                HardWare.ToastShort(mContext, "只有在售的商品才可以报损!");
//                return;
//        }
//
//        if (mLossGoodsList.size() == 0) {
//            mLossGoodsList.add(0, goodsInfo);
//        } else {
//            for (int i = 0; i < mLossGoodsList.size(); i++) {
//                if (goodsInfo.getRainbowCode().equals(mLossGoodsList.get(i).getRainbowCode())) {
//                    HardWare.ToastShort(mContext, "该商品已添加,请勿重复扫描！");
//                    break;
//                }
//                if (i == mLossGoodsList.size() - 1) {
//                    mLossGoodsList.add(0, goodsInfo);
//                    break;
//                }
//            }
//        }
//        mAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_loss:
//                showSubmitLossDialog();
                break;
        }
    }

//    /**
//     * 显示确认报损的弹窗
//     */
//    private void showSubmitLossDialog() {
//        new AlertDialog.Builder(mContext).setTitle("提示")
//                .setMessage("确认提交报损?")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        submitLoss();
//                    }
//                }).show();
//    }
//
//    /**
//     * 显示损耗商品列表的适配器
//     */
//    private class DirectlySubmitLossAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return mLossGoodsList == null ? 0 : mLossGoodsList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mLossGoodsList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder holder;
////            if (convertView == null) {
//            convertView = View.inflate(mContext, R.layout.item_lv_directly_submit_loss, null);
//            holder = new ViewHolder();
//            holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
//            holder.tvRainbowCode = (TextView) convertView.findViewById(R.id.tv_rainbow_code);
//            holder.tvCreateTime = (TextView) convertView.findViewById(R.id.tv_create_time);
//            holder.tvDeadlineTime = (TextView) convertView.findViewById(R.id.tv_deadline_time);
//            holder.spinner = (AppCompatSpinner) convertView.findViewById(R.id.spinner_reason);
//            holder.lLItem = (LinearLayout) convertView.findViewById(R.id.lL_item);
//            convertView.setTag(holder);
////            } else {
////                holder = (ViewHolder) convertView.getTag();
////            }
//
//            GoodsInfo bean = mLossGoodsList.get(position);
//            holder.tvGoodsName.setText(bean.getGoodsName());
//            holder.tvRainbowCode.setText(bean.getRainbowCode());
//            holder.tvCreateTime.setText(bean.getProductTime());
//            holder.tvDeadlineTime.setText(bean.getExpiredTime());
//            holder.spinner.setSelection(bean.getLossReasonPos());
//
//            if (position % 2 == 0) {
//                holder.lLItem.setBackgroundColor(getResources().getColor(R.color.white));
//            } else {
//                holder.lLItem.setBackgroundColor(getResources().getColor(R.color.wcc_color_22));
//            }
//
//            holder.spinner.setAdapter(new LossReasonAdapter(mContext));
//            holder.spinner.setDropDownVerticalOffset(50);
//            if (bean.getLossReasonPos() == -1) {
//                holder.spinner.setSelection(mReasonsInfo.getLossReasonList().size() - 1);
//            } else {
//                holder.spinner.setSelection(bean.getLossReasonPos());
//            }
//            holder.spinner.setOnItemSelectedListener(new ItemClickSelectListener(position));
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView tvGoodsName, tvRainbowCode, tvCreateTime, tvDeadlineTime;
//            AppCompatSpinner spinner;
//            LinearLayout lLItem;
//        }
//
//        private class ItemClickSelectListener implements AdapterView.OnItemSelectedListener {
//
//            private int lvPostion;
//
//            public ItemClickSelectListener(int postion) {
//                this.lvPostion = postion;
//            }
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (hasLossReasons) {
//                    mLossGoodsList.get(lvPostion).setLossReasonPos(position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        }
//    }

//    /**
//     * 选择损耗原因的适配器
//     */
//    class LossReasonAdapter extends BaseAdapter {
//        private LayoutInflater inflater;
//
//        public LossReasonAdapter(Context context) {
//            inflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return mReasonsInfo == null || mReasonsInfo.getLossReasonList() == null ? 0 : mReasonsInfo.getLossReasonList().size() - 1;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mReasonsInfo.getLossReasonList().get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = inflater.inflate(R.layout.spinner_show, null);
//            tv_consume_reason = (TextView) view.findViewById(R.id.tv_consume_reason);
//            tv_consume_reason.setText(mReasonsInfo.getLossReasonList().get(position).getReasonName());
//            if (!hasLossReasons || (hasLossReasons && position == mReasonsInfo.getLossReasonList().size() - 1)) {
//                tv_consume_reason.setTextColor(getResources().getColor(R.color.wcc_color_24));
//            } else {
//                tv_consume_reason.setTextColor(getResources().getColor(R.color.black_deep));
//            }
//            return view;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            PopViewHolder holder;
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.spinner_common_item, null);
//                holder = new PopViewHolder();
//                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
//                convertView.setTag(holder);
//            } else {
//                holder = (PopViewHolder) convertView.getTag();
//            }
//
//            String title = mReasonsInfo.getLossReasonList().get(position).getReasonName();
//            if (ValidatorUtil.isEffective(title)) {
//                holder.tvTitle.setText(title);
//            }
//            return convertView;
//        }
//
//        class PopViewHolder {
//            TextView tvTitle;
//        }
//    }

}
