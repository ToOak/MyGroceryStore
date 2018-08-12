package com.example.xushuailong.mygrocerystore.scan.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanchunfei on 10/28/16.
 */

public class GoodsInfo extends DataModel implements Serializable {
    private String goodsName;
    private String imgUrl;
    private String specification;
    private String productTime;
    private String expiredTime;
    private String batchCode;
    private String barcode;
    private String rainbowCode;
    private String status;
    private String pkid;
    private String storePrice;
    private String discountPrice;
    private int lossReasonPos = -1;

    public String getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(String storePrice) {
        this.storePrice = storePrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public GoodsInfo() {
    }

    public GoodsInfo(String goodsName, String barcode, String rainbowCode, String productTime, String expiredTime) {
        this.goodsName = goodsName;
        this.barcode = barcode;
        this.rainbowCode = rainbowCode;
        this.productTime = productTime;
        this.expiredTime = expiredTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLossReasonPos() {
        return lossReasonPos;
    }

    public void setLossReasonPos(int lossReasonPos) {
        this.lossReasonPos = lossReasonPos;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

//    public static SparseArray<Object> parser(String jsonStr, MapArgs mapArgs) {
//        try {
//            SparseArray<Object> goodsInfoResult = new SparseArray<Object>();
//            jsonStr = jsonStr.replace("\"data\":[]", "\"data\":{}");
//            JSONObject goodsInfoJsonObj = JSONObject.parseObject(jsonStr);
//            if (goodsInfoResult != null) {
//                GoodsInfo goodsInfo = new GoodsInfo();
//                if (goodsInfoJsonObj.containsKey("isok")) {
//                    goodsInfo.setIsokType(goodsInfoJsonObj.getString("isok"));
//                    goodsInfo.setMessage(goodsInfoJsonObj.getString("error"));
//                    goodsInfo.setRescode(goodsInfoJsonObj.getString("errno"));
//                }
//                if (goodsInfoJsonObj.containsKey("data")) {
//                    JSONObject dataObj = goodsInfoJsonObj.getJSONObject("data");
//                    if (dataObj.containsKey("name")) {
//                        goodsInfo.setGoodsName(dataObj.getString("name"));
//                    }
//                    if (dataObj.containsKey("img_url")) {
//                        goodsInfo.setImgUrl(dataObj.getString("img_url"));
//                    }
//                    if (dataObj.containsKey("specification")) {
//                        goodsInfo.setSpecification(dataObj.getString("specification"));
//                    }
//                    if (dataObj.containsKey("product_time")) {
//                        goodsInfo.setProductTime(dataObj.getString("product_time"));
//                    }
//                    if (dataObj.containsKey("expired_time")) {
//                        goodsInfo.setExpiredTime(dataObj.getString("expired_time"));
//                    }
//                    if (dataObj.containsKey("batch_code")) {
//                        goodsInfo.setBatchCode(dataObj.getString("batch_code"));
//                    }
//                    if (dataObj.containsKey("rainbow_code")) {
//                        goodsInfo.setRainbowCode(dataObj.getString("rainbow_code"));
//                    }
//                    if (dataObj.containsKey("status")) {
//                        goodsInfo.setStatus(dataObj.getString("status"));
//                    }
//                    if (dataObj.containsKey("pkid")) {
//                        goodsInfo.setPkid(dataObj.getString("pkid"));
//                    }
//                    if (dataObj.containsKey("store_price")) {
//                        goodsInfo.setStorePrice(dataObj.getString("store_price"));
//                    }
//                    if (dataObj.containsKey("discount_price")) {
//                        goodsInfo.setDiscountPrice(dataObj.getString("discount_price"));
//                    }
//                    goodsInfo.setBarcode(String.valueOf(mapArgs.get("Barcode")));
//                    goodsInfoResult.put(1, goodsInfo);
//                    if (dataObj.containsKey("trace_detail")) {
//                        JSONArray jsonArr = dataObj.getJSONArray("trace_detail");
//                        if (jsonArr != null) {
//                            List<TraceDetailInfo> traceInfoList = new ArrayList<TraceDetailInfo>();
//                            for (int i = 0; i < jsonArr.size(); i++) {
//                                TraceDetailInfo traceInfo = new TraceDetailInfo();
//                                if (TraceDetailInfo.parser((JSONObject) jsonArr.get(i), traceInfo)) {
//                                    traceInfoList.add(traceInfo);
//                                }
//                            }
//                            goodsInfoResult.put(2, traceInfoList);
//                        }
//                    }
//                    if (dataObj.containsKey("promotions")){
//                        JSONArray promotionArr = dataObj.getJSONArray("promotions");
//                        if (promotionArr != null) {
//                            List<PromotionInfo> promotionInfoList = new ArrayList<PromotionInfo>();
//                            for (int i = 0; i < promotionArr.size(); i++) {
//                                PromotionInfo promotionInfo = new PromotionInfo();
//                                if (PromotionInfo.parser((JSONObject) promotionArr.get(i), promotionInfo)) {
//                                    promotionInfoList.add(promotionInfo);
//                                }
//                            }
//                            goodsInfoResult.put(3, promotionInfoList);
//                        }
//                    }
//                    return goodsInfoResult;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }

    public String toJsonItem(int lossReason) {
        return "{\"barcode\":\""+barcode+"\",\"rainbow_code\":\""+rainbowCode+"\",\"pkid\":\""+pkid+"\",\"reason\":"+lossReason+"}";
    }
}
