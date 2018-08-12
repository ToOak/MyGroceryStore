package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.xushuailong.mygrocerystore.R;

import java.util.HashMap;
import java.util.Map;

public class WccTabBar extends LinearLayout {
	Context mcontext;
	int tabs;
	String curTag;
	Map<String, View> mapTabViews;
	private int bg_hor_left;
	private int bg_hor_mid;
	private int bg_hor_right;
	private LinearLayout layoutTab;
	private View divideLine;
	
	public static interface SelectType {
		public static final String NOR = "nor";
		public static final String SEL = "sel";
	}
	
	public WccTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mcontext = context;
		tabs = 0;
		mapTabViews = new HashMap<String, View>();
		bg_hor_left = 0;
		bg_hor_right = 0;
		bg_hor_mid = 0;
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(context.getResources().getColor(R.color.white));
		layoutTab = new LinearLayout(context);
		layoutTab.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		addView(layoutTab, lp);
		divideLine = new View(context);
		divideLine.setBackgroundColor(getResources().getColor(R.color.wcc_color_2));
		lp = new LayoutParams(LayoutParams.FILL_PARENT, 1);
		addView(divideLine, lp);
	}

	/** 
	 * @param tabTitle
	 * @param colorResId -1 for default
	 * @param tag to tag each tab
	 * @param listener
	 */
	public View addTab(String tabTitle, int colorResId, String tag, TabClickListener listener) {
		return addTab(tabTitle, colorResId, tag, listener, null);
	}
 
	
	/**
	 * call setFillTabDone at the end
	 * add for show top right number
	 * 
	 * @param TabTitle
	 * @param ColorResId -1 for default
	 * @param Tag to tag each tab
	 * @param listener 
	 * @param textTopRight the top right view content
	 */
	public View addTab(String TabTitle, int ColorResId, String Tag,
                       TabClickListener listener, String textTopRight) {
		return addTab(TabTitle, ColorResId, Tag, listener, textTopRight, false);
	}
	
	/**
	 * call setFillTabDone at the end
	 * add for show top right number
	 * 
	 * @param TabTitle
	 * @param ColorResId -1 for default
	 * @param Tag to tag each tab
	 * @param listener 
	 * @param textTopRight the top right view content
	 * @param hasDirectionView(up and down)
	 */
	
	public View addTab(String TabTitle, int ColorResId, String Tag,
                       TabClickListener listener, String textTopRight, boolean hasDirectionView) {
		int pos = tabs;
		View view = LayoutInflater.from(mcontext).inflate(R.layout.tab_bar_item, null);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		TextView tv_topright = (TextView)view.findViewById(R.id.red_topright);
		View divideLineLeft = view.findViewById(R.id.divideline_left);
		LinearLayout lLDirection = (LinearLayout)view.findViewById(R.id.lL_upanddown);
		ImageView imgUp = (ImageView)view.findViewById(R.id.img_up);
		ImageView imgDown = (ImageView)view.findViewById(R.id.img_down);
		
		if (ColorResId != -1) {
			tv_title.setTextColor(getResources().getColorStateList(ColorResId));
		}
			
		tv_title.setText(TabTitle);
		 
		if(textTopRight != null) {
			tv_topright.setText(textTopRight);
			tv_topright.setVisibility(View.VISIBLE);
		} else {
			tv_topright.setVisibility(View.INVISIBLE);
		}
		
		view.setOnClickListener(listener);
		view.setTag(Tag); 

		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.weight = 1.0f;
		view.setLayoutParams(layout);

		if (pos == 0) {
			view.setBackgroundResource(bg_hor_left);
		} else{
			view.setBackgroundResource(bg_hor_mid);
			divideLineLeft.setVisibility(View.VISIBLE);
		}
		if(hasDirectionView) {
			lLDirection.setVisibility(View.VISIBLE);
			imgUp.setTag(SelectType.NOR);
			imgDown.setTag(SelectType.NOR);
		}else {
			lLDirection.setVisibility(View.GONE);
			imgUp.setTag(SelectType.NOR);
			imgDown.setTag(SelectType.NOR);
		}
		
		layoutTab.addView(view, pos);

		mapTabViews.put(Tag, view);
		tabs++;
		return view;
	}
	
  
	
	/**
	 * Default perform click the first child;
	 */
	public void setFillTabDone() {
		if (tabs > 1)
			layoutTab.getChildAt(tabs - 1).setBackgroundResource(bg_hor_right);
		if (tabs > 0)
			layoutTab.getChildAt(0).performClick();
	}
	
	/**
	 * Default perform click the child with tag;
	 * @param tag
	 */
	public void setFillTabDone(String tag) {
        if (tabs > 1)
        	layoutTab.getChildAt(tabs - 1).setBackgroundResource(bg_hor_right);
        performClick(tag);
    }

	public void performClick(String tag) {
		if (tag == null)
			return;
		for (int i = 0; i < tabs; i++) {
			View item = layoutTab.getChildAt(i);
			String tmpTag = (String) item.getTag();

			if (tag.equals(tmpTag)) {
				layoutTab.getChildAt(i).performClick();
				break;
			}
		}
	}

	public abstract class TabClickListener implements OnClickListener {
		String tag;

		protected TabClickListener(String TAG) {
			tag = TAG;
			if (tag == null)
				tag = "";
		}

		@Override
		final public void onClick(View v) {
			// TODO Auto-generated method stub
			if(couldSelect(tag)) {
				for (int i = 0; i < tabs; i++) {
					View item = layoutTab.getChildAt(i);
					String tmpTag = (String) item.getTag();
					if (tag.equals(tmpTag)) {
						item.setSelected(true);
					} else {
						if(tmpTag.equals(curTag))
							unSelected(tmpTag);
						item.setSelected(false);
					}
				}
				curTag = tag;
				showContent(tag);
			}
 			
		}

		public abstract void showContent(String Tag);
		public boolean couldSelect(String Tag) {
			return true;
		}
		public boolean unSelected(String Tag) {
			return true;
		}
	}

	/**
	 * 
	 * @param set
	 *            other background
	 * @param use
	 *            it before addTab
	 */
	public void setBackgroundResource(int resIdLeft, int resIdRight,
			int resIdMid) {
		bg_hor_left = resIdLeft;
		bg_hor_right = resIdRight;
		bg_hor_mid = resIdMid;
	}
	
	/**
	 * @param use it after addTab
	 */
	public void setClickable(boolean clickable) {
		for(int i=0; i < getChildCount(); i++) {
			View view = layoutTab.getChildAt(i);
			view.setClickable(clickable);
			if(!clickable)
				view.setSelected(false);
		}
    }
	
	public String getCurTabTag() {
		return curTag;
	}
  	
	public View getCurTabView() {
		return getTabView(curTag); 		 
	}
	
	public void setDivideLineVisible(int visibility) {
		if (divideLine != null) {
			divideLine.setVisibility(visibility);
		}
	}
	
	public TextView getTabTopRightView(String tag) {
		TextView view = null;
		try{
			ViewGroup viewgroup = (ViewGroup) getTabView(tag);
			if(viewgroup != null){
			  return (TextView) viewgroup.findViewById(R.id.red_topright);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;		 
	}
	
	public TextView getTabTitleView(String tag) {
		TextView view = null;
		try{
			ViewGroup viewgroup = (ViewGroup) getTabView(tag);
			if(viewgroup != null){
			  return (TextView) viewgroup.findViewById(R.id.tv_title);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;		 
	}
	
	public View getTabView(String tag) {
		View view = null;
		try{
			return mapTabViews.get(tag);		 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;		 
	}
	
	public String getCurTabTitle() {
		return getTabTitle(curTag); 
	}
	
	public String getTabTitle(String tag) {
		String title = "";
		if(tag != null)
		try {			 
			TextView textview = getTabTitleView(tag);
			if(textview != null && tag.equals(textview.getTag()))
				title = textview.getText().toString();			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;		 
	}
	
	public void updateTopRightContent(String content, String tag) {
			TextView textView = (TextView) getTabTopRightView(tag);
			if(textView != null){ 
				if(content != null){
					textView.setText(content);
					textView.setVisibility(View.VISIBLE);
				} else {
					textView.setVisibility(View.INVISIBLE);
				}
			}
		 
	}  
	
	public void updateCurTab(String tag, boolean isClick) {
		if (tag == null)
			return;
		if(isClick) {
			performClick(tag);
			return;
		}
		for (int i = 0; i < tabs; i++) {
			View item = layoutTab.getChildAt(i);
			String tmpTag = (String) item.getTag();
			if (tag.equals(tmpTag)) {
				item.setSelected(true);
			} else {
				item.setSelected(false);
			}
		}
		curTag = tag;
	}
	
	
	public int getTabSize() {		 
		return tabs;
	}
 
 
	
	public void setCurTab(String tag, boolean isClick) {
		if (tag == null)
			return;
		if(isClick){
			performClick(tag);
			return;
		}
		for (int i = 0; i < tabs; i++) {
			View item = layoutTab.getChildAt(i);
			String tmpTag = (String) item.getTag();

			if (tag.equals(tmpTag)) {
				item.setSelected(true);
			}else
				item.setSelected(false);
		}
		curTag = tag;
	}
 	
}
