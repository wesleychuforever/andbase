package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.activity.AbActivity;
import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageListAdapter;
import com.andbase.global.MyApplication;

public class PullToRefreshListActivity extends AbActivity {
	
	private MyApplication application;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullListView mAbPullListView = null;
	private int currentPage = 1;
	private AbTaskQueue mAbTaskQueue = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private AbTitleBar mAbTitleBar = null;
	private ImageListAdapter myListViewAdapter = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.pull_list);
        application = (MyApplication)abApplication;
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.pull_list_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg"); 
		mAbTaskQueue = AbTaskQueue.getInstance();
	    //��ȡListView����
        mAbPullListView = (AbPullListView)this.findViewById(R.id.mListView);
        
        //�򿪹ر�����ˢ�¼��ظ��๦��
        mAbPullListView.setPullRefreshEnable(true); 
        mAbPullListView.setPullLoadEnable(true);
        
        //ListView����
    	list = new ArrayList<Map<String, Object>>();
    	
    	//ʹ���Զ����Adapter
    	myListViewAdapter = new ImageListAdapter(this, list,R.layout.list_items,
				new String[] { "itemsIcon", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle,R.id.itemsText });
    	mAbPullListView.setAdapter(myListViewAdapter);
    	//item������¼�
    	mAbPullListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
    	});
    	
    	showProgressDialog();

    	//�������ֲ�ѯ���¼�
    	final AbTaskItem item1 = new AbTaskItem();
		item1.callback = new AbTaskCallback() {

			@Override
			public void update() {
				removeProgressDialog();
				list.clear();
				if(newList!=null && newList.size()>0){
	                list.addAll(newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullListView.stopRefresh();
			}

			@Override
			public void get() {
	   		    try {
	   		    	Thread.sleep(1000);
	   		    	currentPage = 1;
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	
	   		    	for (int i = 0; i < 10; i++) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", "item"+i);
		   		    	map.put("itemsText", "item..."+i);
		   		    	newList.add(map);
	   				}
	   		    } catch (Exception e) {
	   		    }
		  };
		};
		
		final AbTaskItem item2 = new AbTaskItem();
		item2.callback = new AbTaskCallback() {

			@Override
			public void update() {
				if(newList!=null && newList.size()>0){
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
					mAbPullListView.stopLoadMore(true);
                }else{
                	//û����������
                	mAbPullListView.stopLoadMore(false);
                }
				
			}

			@Override
			public void get() {
	   		    try {
	   		    	currentPage++;
	   		    	Thread.sleep(1000);
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	
	   		    	for (int i = 0; i < 10; i++) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", "item����"+i);
		   		    	map.put("itemsText", "item����..."+i);
		   		    	newList.add(map);
	   				}
	   		    	
	   		    } catch (Exception e) {
	   		    	currentPage--;
	   		    	newList.clear();
	   		    	showToastInThread(e.getMessage());
	   		    }
		  };
		};
		
		mAbPullListView.setAbOnListViewListener(new AbOnListViewListener(){

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(item1);
			}

			@Override
			public void onLoadMore() {
				mAbTaskQueue.execute(item2);
			}
			
		});
		
    	//��һ����������
		mAbTaskQueue.execute(item1);
		
    }
    

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
   
}

